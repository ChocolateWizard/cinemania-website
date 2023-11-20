/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MovieDirectorResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.DirectorJPA;
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

    public MovieDirectorResponseDTO jdbcToDto(DirectorJDBC jdbc) throws IllegalArgumentException {
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

    public MovieDirectorResponseDTO jpaToDto(DirectorJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieDirectorResponseDTO director = new MovieDirectorResponseDTO();
        director.setId(jpa.getId());
        director.setFirstName(jpa.getFirstName());
        director.setLastName(jpa.getLastName());
        director.setGender(jpa.getGender());
        if (jpa.getProfilePhoto() != null && !jpa.getProfilePhoto().isEmpty()) {
            director.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getProfilePhoto());
        }
        return director;
    }

    public List<MovieDirectorResponseDTO> jdbcToDto(List<DirectorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieDirectorResponseDTO> list = new ArrayList<>();
        for (DirectorJDBC jd : jdbcList) {
            list.add(jdbcToDto(jd));
        }
        return list;
    }

    public List<MovieDirectorResponseDTO> jpaToDto(List<DirectorJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieDirectorResponseDTO> list = new ArrayList<>();
        for (DirectorJPA jp : jpaList) {
            list.add(jpaToDto(jp));
        }
        return list;
    }

}
