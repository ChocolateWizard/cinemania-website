/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.actor.ActorResponseDTO;
import com.borak.kinweb.backend.domain.dto.movie.MovieActorResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
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

    public ActorResponseDTO toActorResponseDTO(ActorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        ActorResponseDTO actor = new ActorResponseDTO();
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

    public List<ActorResponseDTO> toActorResponseDTO(List<ActorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<ActorResponseDTO> list = new ArrayList<>();
        for (ActorJDBC jd : jdbcList) {
            list.add(toActorResponseDTO(jd));
        }
        return list;
    }

    public MovieActorResponseDTO toMovieActorResponseDTO(ActorJDBC jdbc) throws IllegalArgumentException {
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

    public List<MovieActorResponseDTO> toMovieActorResponseDTO(List<ActorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieActorResponseDTO> list = new ArrayList<>();
        for (ActorJDBC jd : jdbcList) {
            list.add(toMovieActorResponseDTO(jd));
        }
        return list;
    }

}
