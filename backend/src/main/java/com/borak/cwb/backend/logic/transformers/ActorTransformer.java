/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.movie.MovieActorResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowActorResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jpa.ActorJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class ActorTransformer {

    @Autowired
    private ConfigProperties config;

    public MovieActorResponseDTO toMovieActorResponseFromJDBC(ActorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieActorResponseDTO actor = new MovieActorResponseDTO();
        actor.setId(jdbc.getId());
        actor.setFirstName(jdbc.getFirstName());
        actor.setLastName(jdbc.getLastName());
        actor.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        actor.setStar(jdbc.isStar());
        return actor;
    }

    public List<MovieActorResponseDTO> toMovieActorResponseFromJDBC(List<ActorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieActorResponseDTO> list = new ArrayList<>();
        for (ActorJDBC jd : jdbcList) {
            list.add(ActorTransformer.this.toMovieActorResponseFromJDBC(jd));
        }
        return list;
    }

    public MovieActorResponseDTO toMovieActorResponseFromJPA(ActorJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieActorResponseDTO actor = new MovieActorResponseDTO();
        actor.setId(jpa.getPerson().getId());
        actor.setFirstName(jpa.getPerson().getFirstName());
        actor.setLastName(jpa.getPerson().getLastName());
        actor.setGender(jpa.getPerson().getGender());
        if (jpa.getPerson().getProfilePhoto() != null && !jpa.getPerson().getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getPerson().getProfilePhoto());
        }
        actor.setStar(jpa.getStar());
        return actor;
    }

    public List<MovieActorResponseDTO> toMovieActorResponseFromJPA(List<ActorJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieActorResponseDTO> list = new ArrayList<>();
        for (ActorJPA jp : jpaList) {
            list.add(toMovieActorResponseFromJPA(jp));
        }
        return list;
    }

    public TVShowActorResponseDTO toTVShowActorResponseDTO(ActorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        TVShowActorResponseDTO actor = new TVShowActorResponseDTO();
        actor.setId(jdbc.getId());
        actor.setFirstName(jdbc.getFirstName());
        actor.setLastName(jdbc.getLastName());
        actor.setGender(jdbc.getGender());
        if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }
        actor.setStar(jdbc.isStar());
        return actor;
    }

    public List<TVShowActorResponseDTO> toTVShowActorResponseDTO(List<ActorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowActorResponseDTO> list = new ArrayList<>();
        for (ActorJDBC jd : jdbcList) {
            list.add(toTVShowActorResponseDTO(jd));
        }
        return list;
    }

}
