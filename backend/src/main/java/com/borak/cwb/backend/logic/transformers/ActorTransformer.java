/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.ActorResponseDTO;
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

    public ActorResponseDTO jdbcToActorResponse(ActorJDBC actor) {
        ActorResponseDTO response = new ActorResponseDTO();
        response.setId(actor.getId());
        response.setFirstName(actor.getFirstName());
        response.setLastName(actor.getLastName());
        response.setGender(actor.getGender());
        if (actor.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + actor.getProfilePhoto());
        }
        response.setStar(actor.isStar());
        return response;
    }

    public ActorResponseDTO jpaToActorResponse(ActorJPA actor) {
        ActorResponseDTO response = new ActorResponseDTO();
        response.setId(actor.getPerson().getId());
        response.setFirstName(actor.getPerson().getFirstName());
        response.setLastName(actor.getPerson().getLastName());
        response.setGender(actor.getPerson().getGender());
        if (actor.getPerson().getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + actor.getPerson().getProfilePhoto());
        }
        response.setStar(actor.getStar());
        return response;
    }

//=================================================================================================================================
    public List<ActorResponseDTO> jdbcToActorResponse(List<ActorJDBC> actors) {
        List<ActorResponseDTO> list = new ArrayList<>(actors.size());
        for (ActorJDBC actor : actors) {
            list.add(jdbcToActorResponse(actor));
        }
        return list;
    }

    public List<ActorResponseDTO> jpaToActorResponse(List<ActorJPA> actors) {
        List<ActorResponseDTO> list = new ArrayList<>(actors.size());
        for (ActorJPA actor : actors) {
            list.add(jpaToActorResponse(actor));
        }
        return list;
    }

}
