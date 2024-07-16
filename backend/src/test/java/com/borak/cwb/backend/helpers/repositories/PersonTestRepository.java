/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers.repositories;

import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.PersonJPA;
import com.borak.cwb.backend.repository.jpa.PersonRepositoryJPA;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class PersonTestRepository {

    private final PersonRepositoryJPA personRepo;

    @Autowired
    public PersonTestRepository(PersonRepositoryJPA personRepo) {
        this.personRepo = personRepo;
    }

    public List<PersonJPA> findAll() {
        List<PersonJPA> persons = personRepo.findAll();
        //initialize lazy attributes
        for (PersonJPA person : persons) {
            if (person.getDirectorInfo() != null) {
                person.getDirectorInfo().getMedias().size();
            }
            if (person.getWriterInfo() != null) {
                person.getWriterInfo().getMedias().size();
            }
            if (person.getActorInfo() != null) {
                for (ActingJPA acting : person.getActorInfo().getActings()) {
                    acting.getRoles().size();
                }
            }
        }
        return persons;
    }

    public Optional<PersonJPA> findById(Long id) {
        Optional<PersonJPA> person = personRepo.findById(id);
        if (person.isPresent()) {
            if (person.get().getDirectorInfo() != null) {
                person.get().getDirectorInfo().getMedias().size();
            }
            if (person.get().getWriterInfo() != null) {
                person.get().getWriterInfo().getMedias().size();
            }
            if (person.get().getActorInfo() != null) {
                for (ActingJPA acting : person.get().getActorInfo().getActings()) {
                    acting.getRoles().size();
                }
            }
        }
        return person;
    }

}
