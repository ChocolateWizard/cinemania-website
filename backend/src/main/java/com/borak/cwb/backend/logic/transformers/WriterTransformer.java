/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.movie.MovieWriterResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowWriterResponseDTO;
import com.borak.cwb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.cwb.backend.domain.jpa.PersonJPA;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
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

    public MovieWriterResponseDTO toMovieWriterResponseFromJDBC(WriterJDBC jdbc) throws IllegalArgumentException {
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

    public MovieWriterResponseDTO toMovieWriterResponseFromJPA(WriterJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieWriterResponseDTO writer = new MovieWriterResponseDTO();
        writer.setId(jpa.getPersonId());
        writer.setFirstName(jpa.getPerson().getFirstName());
        writer.setLastName(jpa.getPerson().getLastName());
        writer.setGender(jpa.getPerson().getGender());
        if (jpa.getPerson().getProfilePhoto() != null && !jpa.getPerson().getProfilePhoto().isEmpty()) {
            writer.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getPerson().getProfilePhoto());
        }
        return writer;
    }

    public List<MovieWriterResponseDTO> toMovieWriterResponseFromJDBC(List<WriterJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieWriterResponseDTO> list = new ArrayList<>();
        for (WriterJDBC jd : jdbcList) {
            list.add(WriterTransformer.this.toMovieWriterResponseFromJDBC(jd));
        }
        return list;
    }

    public List<MovieWriterResponseDTO> toMovieWriterResponseFromJPA(List<WriterJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieWriterResponseDTO> list = new ArrayList<>();
        for (WriterJPA jp : jpaList) {
            list.add(toMovieWriterResponseFromJPA(jp));
        }
        return list;
    }

    public TVShowWriterResponseDTO toTVShowWriterResponseDTO(WriterJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        TVShowWriterResponseDTO writer = new TVShowWriterResponseDTO();
        writer.setId(jdbc.getId());
        writer.setFirstName(jdbc.getFirstName());
        writer.setLastName(jdbc.getLastName());
        writer.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            writer.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        return writer;
    }

    public List<TVShowWriterResponseDTO> toTVShowWriterResponseDTO(List<WriterJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowWriterResponseDTO> list = new ArrayList<>();
        for (WriterJDBC jd : jdbcList) {
            list.add(toTVShowWriterResponseDTO(jd));
        }
        return list;
    }

}
