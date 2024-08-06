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

    private final String PERSON_IMAGES_BASE_URL;

    @Autowired
    public PersonTransformer(ConfigProperties config) {
        this.PERSON_IMAGES_BASE_URL = config.getPersonImagesBaseUrl();
    }

//=================================================================================================================================
//JPA
    public PersonResponseDTO jpaToPersonResponse(PersonJPA person) {
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setGender(person.getGender());
        if (person.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + person.getProfilePhoto());
        }
        return response;
    }
//-----------------------------------------------------------------------------------------

    public List<PersonResponseDTO> jpaToPersonResponse(List<PersonJPA> persons) {
        List<PersonResponseDTO> list = new ArrayList<>(persons.size());
        for (PersonJPA person : persons) {
            list.add(jpaToPersonResponse(person));
        }
        return list;
    }

//=================================================================================================================================
//JDBC
    public PersonResponseDTO jdbcToPersonResponse(PersonJDBC person) {
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setGender(person.getGender());
        if (person.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + person.getProfilePhoto());
        }
        return response;
    }

//------------------------------------------------------------------------------------------
    public List<PersonResponseDTO> jdbcToPersonResponse(List<PersonJDBC> persons) {
        List<PersonResponseDTO> list = new ArrayList<>(persons.size());
        for (PersonJDBC person : persons) {
            list.add(jdbcToPersonResponse(person));
        }
        return list;
    }

}
