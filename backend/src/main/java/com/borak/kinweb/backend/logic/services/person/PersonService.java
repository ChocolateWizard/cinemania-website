/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.person;

import com.borak.kinweb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.kinweb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.logic.transformers.PersonTransformer;
import com.borak.kinweb.backend.logic.transformers.PersonWrapperTransformer;
import com.borak.kinweb.backend.repository.api.IMediaRepository;
import com.borak.kinweb.backend.repository.api.IPersonRepository;
import com.borak.kinweb.backend.repository.api.IPersonWrapperRepository;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PersonService implements IPersonService<PersonRequestDTO> {

    @Autowired
    private IPersonRepository<PersonJDBC, Long> personRepo;
    @Autowired
    private IPersonWrapperRepository<PersonWrapperJDBC, Long> personWrapperRepo;
    @Autowired
    private IMediaRepository<MediaJDBC, Long> mediaRepo;
    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private PersonTransformer personTransformer;
    @Autowired
    private PersonWrapperTransformer personWrapperTransformer;

//=====================================================================================
    @Override
    public ResponseEntity getAllPersonsPaginated(int page, int size) {
        List<PersonJDBC> persons = personRepo.findAllPaginated(page, size);
        return new ResponseEntity(personTransformer.toPersonResponseDTO(persons), HttpStatus.OK);
    }

    @Override
    public ResponseEntity deletePersonById(long id) {
        Optional<PersonWrapperJDBC> wrapper = personWrapperRepo.findByIdWithRelations(id);
        if (wrapper.isEmpty()) {
            throw new ResourceNotFoundException("No person found with id: " + id);
        }
        personRepo.deleteById(id);
        if (wrapper.get().getPerson().getProfilePhoto() != null && !wrapper.get().getPerson().getProfilePhoto().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(wrapper.get().getPerson().getProfilePhoto());
        }
        return new ResponseEntity(personWrapperTransformer.toPersonResponseDTO(wrapper.get()), HttpStatus.OK);
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
        PersonWrapperJDBC wrapperJDBC = personWrapperTransformer.toPersonWrapperJDBC(personClient);
        wrapperJDBC.getPerson().setProfilePhoto(null);
        PersonWrapperJDBC wrapperDB = personWrapperRepo.insert(wrapperJDBC);
        Optional<PersonWrapperJDBC> person;
        if (personClient.getProfilePhoto() != null) {
            personClient.getProfilePhoto().setName("" + wrapperDB.getPerson().getId());
            personRepo.updateProfilePhoto(wrapperDB.getPerson().getId(), personClient.getProfilePhoto().getFullName());
            person = personWrapperRepo.findByIdWithRelations(wrapperDB.getPerson().getId());
            fileRepo.savePersonProfilePhoto(personClient.getProfilePhoto());
        } else {
            person = personWrapperRepo.findByIdWithRelations(wrapperDB.getPerson().getId());
        }
        return new ResponseEntity<>(personWrapperTransformer.toPersonResponseDTO(person.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putPerson(PersonRequestDTO request) {
        if (!personRepo.existsById(request.getId())) {
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
        Optional<PersonWrapperJDBC> response;
        //retreives profile photo name of person before its update
        Optional<String> beforeUpdatePhotoName = personRepo.findByIdProfilePhoto(request.getId());
        if (request.getProfilePhoto() != null) {
            //MyImage != null
            //replace profile photo if existed. Otherwise create new
            request.getProfilePhoto().setName("" + request.getId());
            PersonWrapperJDBC wrapperToStore = personWrapperTransformer.toPersonWrapperJDBC(request);
            personWrapperRepo.update(wrapperToStore);
            response = personWrapperRepo.findByIdWithRelations(request.getId());
            if (beforeUpdatePhotoName.isPresent()) {
                fileRepo.deleteIfExistsPersonPhotoImage(beforeUpdatePhotoName.get());
            }
            fileRepo.savePersonProfilePhoto(request.getProfilePhoto());
        } else {
            //MyImage == null
            //delete profile photo if existed
            PersonWrapperJDBC wrapperToStore = personWrapperTransformer.toPersonWrapperJDBC(request);
            personWrapperRepo.update(wrapperToStore);
            response = personWrapperRepo.findByIdWithRelations(request.getId());
            if (beforeUpdatePhotoName.isPresent()) {
                fileRepo.deleteIfExistsPersonPhotoImage(beforeUpdatePhotoName.get());
            }
        }
        return new ResponseEntity<>(personWrapperTransformer.toPersonResponseDTO(response.get()), HttpStatus.OK);

    }

    @Override
    public ResponseEntity getAllPersonsWithDetailsPaginated(int page, int size) {
        List<PersonWrapperJDBC> persons = personWrapperRepo.findAllWithRelationsPaginated(page, size);
        return new ResponseEntity(personWrapperTransformer.toPersonResponseDTO(persons), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getPersonWithProfessions(Long id) {
        Optional<PersonWrapperJDBC> person = personWrapperRepo.findById(id);
        if (person.isPresent()) {
            return new ResponseEntity(personWrapperTransformer.toPersonResponseDTO(person.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No person found with id: " + id);
    }

    @Override
    public ResponseEntity getPersonWithDetails(Long id) {
        Optional<PersonWrapperJDBC> person = personWrapperRepo.findByIdWithRelations(id);
        if (person.isPresent()) {
            return new ResponseEntity(personWrapperTransformer.toPersonResponseDTO(person.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No person found with id: " + id);
    }

}
