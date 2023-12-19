/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MovieActorResponseDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowActorResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class ActingTransformer {

    @Autowired
    private ConfigProperties config;

    public MovieActorResponseDTO toMovieActorResponseDTO(ActingJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        if (jdbc.getActor() == null) {
            throw new IllegalArgumentException("Null actor passed as method parameter");
        }
        MovieActorResponseDTO actor = new MovieActorResponseDTO();
        actor.setId(jdbc.getActor().getId());
        actor.setFirstName(jdbc.getActor().getFirstName());
        actor.setLastName(jdbc.getActor().getLastName());
        actor.setGender(jdbc.getActor().getGender());
        if (jdbc.getActor().getProfilePhoto() != null && !jdbc.getActor().getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getActor().getProfilePhoto());
        }
        actor.setStar(jdbc.getActor().isStar());
        actor.setStarring(jdbc.isStarring());
        for (ActingRoleJDBC role : jdbc.getRoles()) {
            actor.getRoles().add(new MovieActorResponseDTO.Role(role.getId(), role.getName()));
        }
        return actor;
    }

    public List<MovieActorResponseDTO> toMovieActorResponseDTO(List<ActingJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieActorResponseDTO> list = new ArrayList<>();
        for (ActingJDBC jd : jdbcList) {
            list.add(toMovieActorResponseDTO(jd));
        }
        return list;
    }

    public TVShowActorResponseDTO toTVShowActorResponseDTO(ActingJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        if (jdbc.getActor() == null) {
            throw new IllegalArgumentException("Null actor passed as method parameter");
        }
        TVShowActorResponseDTO actor = new TVShowActorResponseDTO();
        actor.setId(jdbc.getActor().getId());
        actor.setFirstName(jdbc.getActor().getFirstName());
        actor.setLastName(jdbc.getActor().getLastName());
        actor.setGender(jdbc.getActor().getGender());
        if (jdbc.getActor().getProfilePhoto() != null && !jdbc.getActor().getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getActor().getProfilePhoto());
        }
        actor.setStar(jdbc.getActor().isStar());
        actor.setStarring(jdbc.isStarring());
        for (ActingRoleJDBC role : jdbc.getRoles()) {
            actor.getRoles().add(new TVShowActorResponseDTO.Role(role.getId(), role.getName()));
        }
        return actor;
    }

    public List<TVShowActorResponseDTO> toTVShowActorResponseDTO(List<ActingJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowActorResponseDTO> list = new ArrayList<>();
        for (ActingJDBC jd : jdbcList) {
            list.add(toTVShowActorResponseDTO(jd));
        }
        return list;
    }

}
