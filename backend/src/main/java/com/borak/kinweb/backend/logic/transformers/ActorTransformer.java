/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.constants.Constants;
import com.borak.kinweb.backend.domain.dto.actor.ActorResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.ActorJPA;
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

    public ActorResponseDTO jdbcToDto(ActorJDBC jdbc) throws IllegalArgumentException {
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

    public ActorResponseDTO jpaToDto(ActorJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        ActorResponseDTO actor = new ActorResponseDTO();
        actor.setId(jpa.getId());
        actor.setFirstName(jpa.getFirstName());
        actor.setLastName(jpa.getLastName());
        actor.setGender(jpa.getGender());
        if (jpa.getProfilePhoto() != null && !jpa.getProfilePhoto().isEmpty()) {
            actor.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + jpa.getProfilePhoto());
        }
        actor.setStar(jpa.isStar());
        return actor;
    }

    public List<ActorResponseDTO> jdbcToDto(List<ActorJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<ActorResponseDTO> list = new ArrayList<>();
        for (ActorJDBC jd : jdbcList) {
            list.add(jdbcToDto(jd));
        }
        return list;
    }

    public List<ActorResponseDTO> jpaToDto(List<ActorJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<ActorResponseDTO> list = new ArrayList<>();
        for (ActorJPA jp : jpaList) {
            list.add(jpaToDto(jp));
        }
        return list;
    }

}
