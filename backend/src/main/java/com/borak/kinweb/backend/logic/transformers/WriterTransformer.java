/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MovieWriterResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.WriterJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class WriterTransformer {

    @Autowired
    private ConfigProperties config;

    public MovieWriterResponseDTO jdbcToDto(WriterJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieWriterResponseDTO writer = new MovieWriterResponseDTO();
        writer.setId(jdbc.getId());
        writer.setFirstName(jdbc.getFirstName());
        writer.setLastName(jdbc.getLastName());
        writer.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            writer.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        return writer;
    }

    public MovieWriterResponseDTO jpaToDto(WriterJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieWriterResponseDTO writer = new MovieWriterResponseDTO();
        writer.setId(jpa.getId());
        writer.setFirstName(jpa.getFirstName());
        writer.setLastName(jpa.getLastName());
        writer.setGender(jpa.getGender());
        if (jpa.getProfilePhoto() != null && !jpa.getProfilePhoto().isEmpty()) {
            writer.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getProfilePhoto());
        }
        return writer;
    }

    public List<MovieWriterResponseDTO> jdbcToDto(List<WriterJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieWriterResponseDTO> list = new ArrayList<>();
        for (WriterJDBC jd : jdbcList) {
            list.add(jdbcToDto(jd));
        }
        return list;
    }

    public List<MovieWriterResponseDTO> jpaToDto(List<WriterJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieWriterResponseDTO> list = new ArrayList<>();
        for (WriterJPA jp : jpaList) {
            list.add(jpaToDto(jp));
        }
        return list;
    }

}
