/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.constants.Constants;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.JDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;
import com.borak.kinweb.backend.domain.jpa.classes.WriterJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class WriterTransformer implements GenericTransformer<WriterDTO, WriterJDBC, WriterJPA> {

    @Autowired
    private ConfigProperties config;
    
    @Override
    public WriterDTO jdbcToDto(WriterJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        WriterDTO writer = new WriterDTO();
        writer.setId(jdbc.getId());
        writer.setFirstName(jdbc.getFirstName());
        writer.setLastName(jdbc.getLastName());
        writer.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            writer.setProfilePhoto(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }   
        return writer;
    }

    @Override
    public WriterDTO jpaToDto(WriterJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        WriterDTO writer = new WriterDTO();
        writer.setId(jpa.getId());
        writer.setFirstName(jpa.getFirstName());
        writer.setLastName(jpa.getLastName());
        writer.setGender(jpa.getGender());
        writer.setProfilePhoto(jpa.getProfilePhotoURL());
        return writer;
    }

}
