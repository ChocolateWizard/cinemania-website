/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.movie.MovieActorResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowActorResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.ActingRoleJPA;
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

    public MovieActorResponseDTO toMovieActorResponseFromJDBC(ActingJDBC jdbc) throws IllegalArgumentException {
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

    public List<MovieActorResponseDTO> toMovieActorResponseFromJDBC(List<ActingJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieActorResponseDTO> list = new ArrayList<>();
        for (ActingJDBC jd : jdbcList) {
            list.add(ActingTransformer.this.toMovieActorResponseFromJDBC(jd));
        }
        return list;
    }

    public MovieActorResponseDTO toMovieActorResponseFromJPA(ActingJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        if (jpa.getActor() == null) {
            throw new IllegalArgumentException("Null actor passed as method parameter");
        }
        MovieActorResponseDTO actor = new MovieActorResponseDTO();
        actor.setId(jpa.getActor().getPersonId());
        actor.setFirstName(jpa.getActor().getPerson().getFirstName());
        actor.setLastName(jpa.getActor().getPerson().getLastName());
        actor.setGender(jpa.getActor().getPerson().getGender());
        if (jpa.getActor().getPerson().getProfilePhoto() != null && !jpa.getActor().getPerson().getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getActor().getPerson().getProfilePhoto());
        }
        actor.setStar(jpa.getActor().getStar());
        actor.setStarring(jpa.getStarring());
        for (ActingRoleJPA role : jpa.getRoles()) {
            actor.getRoles().add(new MovieActorResponseDTO.Role(role.getId().getId(), role.getName()));
        }
        return actor;
    }

    public List<MovieActorResponseDTO> toMovieActorResponseFromJPA(List<ActingJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieActorResponseDTO> list = new ArrayList<>();
        for (ActingJPA jp : jpaList) {
            list.add(ActingTransformer.this.toMovieActorResponseFromJPA(jp));
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
