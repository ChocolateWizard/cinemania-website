/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.JDBC;
import com.borak.kinweb.backend.domain.jpa.classes.DirectorJPA;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class DirectorTransformer implements GenericTransformer<DirectorDTO, DirectorJDBC, DirectorJPA> {

    @Override
    public DirectorDTO jdbcToDto(DirectorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        DirectorDTO director = new DirectorDTO();
        director.setId(jdbc.getId());
        director.setFirstName(jdbc.getFirstName());
        director.setLastName(jdbc.getLastName());
        director.setGender(jdbc.getGender());
        director.setProfilePhotoURL(jdbc.getProfilePhotoURL());
        return director;
    }

    @Override
    public DirectorDTO jpaToDto(DirectorJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        DirectorDTO director = new DirectorDTO();
        director.setId(jpa.getId());
        director.setFirstName(jpa.getFirstName());
        director.setLastName(jpa.getLastName());
        director.setGender(jpa.getGender());
        director.setProfilePhotoURL(jpa.getProfilePhotoURL());
        return director;
    }

}
