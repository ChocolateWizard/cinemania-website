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
@Transactional(readOnly = true)
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
            if (person.getDirector() != null) {
                person.getDirector().getMedias().size();
            }
            if (person.getWriter() != null) {
                person.getWriter().getMedias().size();
            }
            if (person.getActor() != null) {
                for (ActingJPA acting : person.getActor().getActings()) {
                    acting.getRoles().size();
                }
            }
        }
        return persons;
    }

    public Optional<PersonJPA> findById(Long id) {
        Optional<PersonJPA> person = personRepo.findById(id);
        if (person.isPresent()) {
            if (person.get().getDirector() != null) {
                person.get().getDirector().getMedias().size();
            }
            if (person.get().getWriter() != null) {
                person.get().getWriter().getMedias().size();
            }
            if (person.get().getActor() != null) {
                for (ActingJPA acting : person.get().getActor().getActings()) {
                    acting.getRoles().size();
                }
            }
        }
        return person;
    }

}
