/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.constants.Constants;
import com.borak.kinweb.backend.domain.dto.classes.ActorDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.ActorJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class ActorTransformer implements GenericTransformer<ActorDTO, ActorJDBC, ActorJPA> {

    @Autowired
    private ConfigProperties config;
    
    @Override
    public ActorDTO jdbcToDto(ActorJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        ActorDTO actor = new ActorDTO();
        actor.setId(jdbc.getId());
        actor.setFirstName(jdbc.getFirstName());
        actor.setLastName(jdbc.getLastName());
        actor.setGender(jdbc.getGender());
         if (jdbc.getProfilePhoto() != null && !jdbc.getProfilePhoto().isEmpty()) {
            actor.setProfilePhoto(config.getPersonImagesBaseUrl() + jdbc.getProfilePhoto());
        }   
        actor.setStar(jdbc.isStar());
        return actor;
    }

    @Override
    public ActorDTO jpaToDto(ActorJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        ActorDTO actor = new ActorDTO();
        actor.setId(jpa.getId());
        actor.setFirstName(jpa.getFirstName());
        actor.setLastName(jpa.getLastName());
        actor.setGender(jpa.getGender());
        actor.setProfilePhoto(jpa.getProfilePhotoURL());
        actor.setStar(jpa.isStar());
        return actor;
    }

}
