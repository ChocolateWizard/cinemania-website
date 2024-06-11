/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.cwb.backend.domain.jdbc.classes.PersonJDBC;
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

    public PersonResponseDTO toPersonResponseDTO(PersonJDBC jdbc) throws IllegalArgumentException {
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

    public List<PersonResponseDTO> toPersonResponseDTO(List<PersonJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<PersonResponseDTO> list = new ArrayList<>();
        for (PersonJDBC jd : jdbcList) {
            list.add(toPersonResponseDTO(jd));
        }
        return list;
    }

}
