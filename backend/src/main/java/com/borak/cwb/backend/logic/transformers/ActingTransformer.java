/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.ActorResponseDTO;
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

    public ActorResponseDTO jdbcToActorResponse(ActingJDBC acting) {
        ActorResponseDTO response = new ActorResponseDTO();
        response.setId(acting.getActor().getId());
        response.setFirstName(acting.getActor().getFirstName());
        response.setLastName(acting.getActor().getLastName());
        response.setGender(acting.getActor().getGender());
        if (acting.getActor().getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + acting.getActor().getProfilePhoto());
        }
        response.setStar(acting.getActor().isStar());
        response.setStarring(acting.isStarring());
        for (ActingRoleJDBC role : acting.getRoles()) {
            response.getRoles().add(new ActorResponseDTO.Role(role.getId(), role.getName()));
        }
        return response;
    }

    public ActorResponseDTO jpaToActorResponse(ActingJPA acting) {
        ActorResponseDTO response = new ActorResponseDTO();
        response.setId(acting.getActor().getPersonId());
        response.setFirstName(acting.getActor().getPerson().getFirstName());
        response.setLastName(acting.getActor().getPerson().getLastName());
        response.setGender(acting.getActor().getPerson().getGender());
        if (acting.getActor().getPerson().getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + acting.getActor().getPerson().getProfilePhoto());
        }
        response.setStar(acting.getActor().getStar());
        response.setStarring(acting.getStarring());
        for (ActingRoleJPA role : acting.getRoles()) {
            response.getRoles().add(new ActorResponseDTO.Role(role.getId().getId(), role.getName()));
        }
        return response;
    }

//=================================================================================================================================
    public List<ActorResponseDTO> jdbcToActorResponse(List<ActingJDBC> actings) {
        List<ActorResponseDTO> list = new ArrayList<>(actings.size());
        for (ActingJDBC acting : actings) {
            list.add(jdbcToActorResponse(acting));
        }
        return list;
    }

    public List<ActorResponseDTO> jpaToActorResponse(List<ActingJPA> actings) {
        List<ActorResponseDTO> list = new ArrayList<>(actings.size());
        for (ActingJPA acting : actings) {
            list.add(jpaToActorResponse(acting));
        }
        return list;
    }

}
