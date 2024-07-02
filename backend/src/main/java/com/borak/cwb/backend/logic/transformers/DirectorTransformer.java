/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.movie.MovieDirectorResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowDirectorResponseDTO;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
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
public class DirectorTransformer {

    @Autowired
    private ConfigProperties config;

    public MovieDirectorResponseDTO toMovieDirectorResponseFromJDBC(DirectorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieDirectorResponseDTO director = new MovieDirectorResponseDTO();
        director.setId(jdbc.getId());
        director.setFirstName(jdbc.getFirstName());
        director.setLastName(jdbc.getLastName());
        director.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            director.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        return director;
    }

    public MovieDirectorResponseDTO toMovieDirectorResponseFromJPA(DirectorJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieDirectorResponseDTO director = new MovieDirectorResponseDTO();
        director.setId(jpa.getPersonId());
        director.setFirstName(jpa.getPerson().getFirstName());
        director.setLastName(jpa.getPerson().getLastName());
        director.setGender(jpa.getPerson().getGender());
        if (jpa.getPerson().getProfilePhoto() != null && !jpa.getPerson().getProfilePhoto().isEmpty()) {
            director.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getPerson().getProfilePhoto());
        }
        return director;
    }

    public List<MovieDirectorResponseDTO> toMovieDirectorResponseFromJDBC(List<DirectorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieDirectorResponseDTO> list = new ArrayList<>();
        for (DirectorJDBC jd : jdbcList) {
            list.add(DirectorTransformer.this.toMovieDirectorResponseFromJDBC(jd));
        }
        return list;
    }

    public List<MovieDirectorResponseDTO> toMovieDirectorResponseFromJPA(List<DirectorJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieDirectorResponseDTO> list = new ArrayList<>();
        for (DirectorJPA jp : jpaList) {
            list.add(toMovieDirectorResponseFromJPA(jp));
        }
        return list;
    }

    public TVShowDirectorResponseDTO toTVShowDirectorResponseDTO(DirectorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        TVShowDirectorResponseDTO director = new TVShowDirectorResponseDTO();
        director.setId(jdbc.getId());
        director.setFirstName(jdbc.getFirstName());
        director.setLastName(jdbc.getLastName());
        director.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            director.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        return director;
    }

    public List<TVShowDirectorResponseDTO> toTVShowDirectorResponseDTO(List<DirectorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowDirectorResponseDTO> list = new ArrayList<>();
        for (DirectorJDBC jd : jdbcList) {
            list.add(toTVShowDirectorResponseDTO(jd));
        }
        return list;
    }

}
