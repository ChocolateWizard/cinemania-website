/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MovieWriterResponseDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowWriterResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
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

    public MovieWriterResponseDTO toMovieWriterResponseDTO(WriterJDBC jdbc) throws IllegalArgumentException {
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

    public List<MovieWriterResponseDTO> toMovieWriterResponseDTO(List<WriterJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieWriterResponseDTO> list = new ArrayList<>();
        for (WriterJDBC jd : jdbcList) {
            list.add(toMovieWriterResponseDTO(jd));
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
