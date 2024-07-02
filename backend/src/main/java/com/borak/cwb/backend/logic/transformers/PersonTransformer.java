/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.cwb.backend.domain.jdbc.PersonJDBC;
import com.borak.cwb.backend.domain.jpa.PersonJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class PersonTransformer {

    @Autowired
    private ConfigProperties config;

    public PersonResponseDTO toResponseFromJDBC(PersonJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        PersonResponseDTO person = new PersonResponseDTO();
        person.setId(jdbc.getId());
        person.setFirstName(jdbc.getFirstName());
        person.setLastName(jdbc.getLastName());
        person.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            person.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        return person;
    }
  

    public PersonResponseDTO toResponseFromJPA(PersonJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        PersonResponseDTO person = new PersonResponseDTO();
        person.setId(jpa.getId());
        person.setFirstName(jpa.getFirstName());
        person.setLastName(jpa.getLastName());
        person.setGender(jpa.getGender());
        if (jpa.getProfilePhoto() != null && !jpa.getProfilePhoto().isEmpty()) {
            person.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getProfilePhoto());
        }
        return person;
    }
    
    //=========================================================================================================
    
    public List<PersonResponseDTO> toResponseFromJDBC(List<PersonJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<PersonResponseDTO> list = new ArrayList<>();
        for (PersonJDBC jd : jdbcList) {
            list.add(PersonTransformer.this.toResponseFromJDBC(jd));
        }
        return list;
    }
    
    public List<PersonResponseDTO> toResponseFromJPA(List<PersonJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<PersonResponseDTO> list = new ArrayList<>();
        for (PersonJPA jp : jpaList) {
            list.add(toResponseFromJPA(jp));
        }
        return list;
    }
    
}
