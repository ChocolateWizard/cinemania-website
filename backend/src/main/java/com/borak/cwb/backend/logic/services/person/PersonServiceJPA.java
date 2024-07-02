/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.person;

import com.borak.cwb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.PersonJPA;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.PersonTransformer;
import com.borak.cwb.backend.logic.transformers.PersonWrapperTransformer;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.PersonRepositoryJPA;
import com.borak.cwb.backend.repository.file.FileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class PersonServiceJPA implements IPersonService<PersonRequestDTO> {

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private PersonRepositoryJPA personRepo;
    @Autowired
    private MediaRepositoryJPA mediaRepo;
    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private PersonTransformer personTransformer;
    @Autowired
    private PersonWrapperTransformer personWrapperTransformer;

//=====================================================================================
    @Override
    public ResponseEntity getAllPersonsPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<PersonJPA> persons = personRepo.findAll(p);
        return new ResponseEntity(personTransformer.toResponseFromJPA(persons.getContent()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllPersonsWithDetailsPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<PersonJPA> persons = personRepo.findAll(p);
        return new ResponseEntity(personWrapperTransformer.toResponseFromJPA(persons.getContent(), "workedOn"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getPersonWithProfessions(Long id) {
        Optional<PersonJPA> person = personRepo.findById(id);
        if (person.isPresent()) {
            return new ResponseEntity(personWrapperTransformer.toResponseFromJPA(person.get(), ""), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No person found with id: " + id);
    }

    @Override
    public ResponseEntity getPersonWithDetails(Long id) {
        Optional<PersonJPA> person = personRepo.findById(id);
        if (person.isPresent()) {
            return new ResponseEntity(personWrapperTransformer.toResponseFromJPA(person.get(), "workedOn"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No person found with id: " + id);
    }

    @Override
    public ResponseEntity deletePersonById(long id) {
        Optional<PersonJPA> person = personRepo.findById(id);
        if (person.isEmpty()) {
            throw new ResourceNotFoundException("No person found with id: " + id);
        }
        personRepo.deleteById(id);
        if (person.get().getProfilePhoto() != null && !person.get().getProfilePhoto().isEmpty()) {
            fileRepo.deleteIfExistsPersonPhotoImage(person.get().getProfilePhoto());
        }
        return new ResponseEntity(personWrapperTransformer.toResponseFromJPA(person.get(), "workedOn"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity postPerson(PersonRequestDTO personClient) {
        if (personClient.getProfessions() != null) {
            for (PersonRequestDTO.Profession profession : personClient.getProfessions()) {
                if (profession instanceof PersonRequestDTO.Director director) {
                    for (Long id : director.getWorkedOn()) {
                        if (!mediaRepo.existsById(id)) {
                            throw new ResourceNotFoundException("Directors worked on media, with id: " + id + " does not exist in database!");
                        }
                    }
                }
                if (profession instanceof PersonRequestDTO.Writer writer) {
                    for (Long id : writer.getWorkedOn()) {
                        if (!mediaRepo.existsById(id)) {
                            throw new ResourceNotFoundException("Writers worked on media, with id: " + id + " does not exist in database!");
                        }
                    }
                }
                if (profession instanceof PersonRequestDTO.Actor actor) {
                    for (PersonRequestDTO.Actor.Acting acting : actor.getWorkedOn()) {
                        if (!mediaRepo.existsById(acting.getMediaId())) {
                            throw new ResourceNotFoundException("Actors worked on media, with id: " + acting.getMediaId() + " does not exist in database!");
                        }
                    }
                }
            }
        }
        PersonJPA personToSave = personWrapperTransformer.toPersonJPA(personClient);
        personToSave.setProfilePhoto(null);
        PersonJPA person;
        if (personClient.getProfilePhoto() != null) {
            person = personRepo.save(personToSave);
            personClient.getProfilePhoto().setName("" + person.getId());
            person.setProfilePhoto(personClient.getProfilePhoto().getFullName());
            if (person.getDirectorInfo() != null) {
                person.getDirectorInfo().setPersonId(person.getId());
            }
            if (person.getWriterInfo() != null) {
                person.getWriterInfo().setPersonId(person.getId());
            }
            if (person.getActorInfo() != null) {
                person.getActorInfo().setPersonId(person.getId());
            }
            personRepo.saveAndFlush(person);
            manager.refresh(person);
            fileRepo.savePersonProfilePhoto(personClient.getProfilePhoto());
        } else {
            person = personRepo.save(personToSave);
            if (person.getDirectorInfo() != null) {
                person.getDirectorInfo().setPersonId(person.getId());
            }
            if (person.getWriterInfo() != null) {
                person.getWriterInfo().setPersonId(person.getId());
            }
            if (person.getActorInfo() != null) {
                person.getActorInfo().setPersonId(person.getId());
            }
            personRepo.saveAndFlush(person);
            manager.refresh(person);
        }
        return new ResponseEntity<>(personWrapperTransformer.toResponseFromJPA(person, "workedOn"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putPerson(PersonRequestDTO request) {
        Optional<PersonJPA> personDB = personRepo.findById(request.getId());
        if (!personDB.isPresent()) {
            throw new ResourceNotFoundException("Person with id: " + request.getId() + " does not exist in database!");
        }
        if (request.getProfessions() != null) {
            for (PersonRequestDTO.Profession profession : request.getProfessions()) {
                if (profession instanceof PersonRequestDTO.Director director) {
                    for (Long id : director.getWorkedOn()) {
                        if (!mediaRepo.existsById(id)) {
                            throw new ResourceNotFoundException("Directors worked on media, with id: " + id + " does not exist in database!");
                        }
                    }
                }
                if (profession instanceof PersonRequestDTO.Writer writer) {
                    for (Long id : writer.getWorkedOn()) {
                        if (!mediaRepo.existsById(id)) {
                            throw new ResourceNotFoundException("Writers worked on media, with id: " + id + " does not exist in database!");
                        }
                    }
                }
                if (profession instanceof PersonRequestDTO.Actor actor) {
                    for (PersonRequestDTO.Actor.Acting acting : actor.getWorkedOn()) {
                        if (!mediaRepo.existsById(acting.getMediaId())) {
                            throw new ResourceNotFoundException("Actors worked on media, with id: " + acting.getMediaId() + " does not exist in database!");
                        }
                    }
                }
            }
        }
        PersonJPA person;
        String profilePhotoDB = personDB.get().getProfilePhoto();
        if (request.getProfilePhoto() != null) {
            //MyImage != null
            //client provided a profile photo in PUT request so he wished to replace the current one if it's present
            request.getProfilePhoto().setName("" + request.getId());
            PersonJPA personToSave = personWrapperTransformer.toPersonJPA(request);
            for (ActingJPA acting : personToSave.getActorInfo().getActings()) {
                for (ActingJPA actingDB : personDB.get().getActorInfo().getActings()) {
                    if (Objects.equals(actingDB.getMedia().getId(), acting.getMedia().getId())) {
                        acting.setId(actingDB.getId());
                    }
                }
            }
            person = personRepo.saveAndFlush(personToSave);
            manager.refresh(person);
            if (profilePhotoDB != null) {
                fileRepo.deleteIfExistsPersonPhotoImage(profilePhotoDB);
            }
            fileRepo.savePersonProfilePhoto(request.getProfilePhoto());
        } else {
            //MyImage == null
            //client provided no profile photo in PUT request so he wished to delete the exisitng one if present
            PersonJPA personToSave = personWrapperTransformer.toPersonJPA(request);
            for (ActingJPA acting : personToSave.getActorInfo().getActings()) {
                for (ActingJPA actingDB : personDB.get().getActorInfo().getActings()) {
                    if (Objects.equals(actingDB.getMedia().getId(), acting.getMedia().getId())) {
                        acting.setId(actingDB.getId());
                    }
                }
            }
            person = personRepo.saveAndFlush(personToSave);
            manager.refresh(person);
            if (profilePhotoDB != null) {
                fileRepo.deleteIfExistsPersonPhotoImage(profilePhotoDB);
            }
        }
        return new ResponseEntity<>(personWrapperTransformer.toResponseFromJPA(person, "workedOn"), HttpStatus.OK);

    }

}
