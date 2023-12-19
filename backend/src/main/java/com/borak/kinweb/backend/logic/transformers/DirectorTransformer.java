/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MovieDirectorResponseDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowDirectorResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
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

    public MovieDirectorResponseDTO toMovieDirectorResponseDTO(DirectorJDBC jdbc) throws IllegalArgumentException {
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

    public List<MovieDirectorResponseDTO> toMovieDirectorResponseDTO(List<DirectorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieDirectorResponseDTO> list = new ArrayList<>();
        for (DirectorJDBC jd : jdbcList) {
            list.add(toMovieDirectorResponseDTO(jd));
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
