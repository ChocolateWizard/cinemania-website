/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CritiqueTransformer {

    public CritiqueJDBC toCritiqueJDBC(CritiqueRequestDTO critiqueRequest, Long userId) {
        CritiqueJDBC critique = new CritiqueJDBC();
        UserJDBC user = new UserJDBC();
        MediaJDBC media = new MediaJDBC();
        critique.setDescription(critiqueRequest.getDescription());
        critique.setRating(critiqueRequest.getRating());
        user.setId(userId);
        media.setId(critiqueRequest.getMediaId());
        critique.setMedia(media);
        critique.setCritic(user);
        return critique;
    }

    public CritiqueJPA toCritiqueJPA(CritiqueRequestDTO critiqueRequest, Long userId) {
        CritiqueJPA critique = new CritiqueJPA();
        UserJPA user = new UserJPA();
        MediaJPA media = new MediaJPA();
        critique.setDescription(critiqueRequest.getDescription());
        critique.setRating(critiqueRequest.getRating());
        user.setId(userId);
        media.setId(critiqueRequest.getMediaId());
        critique.setId(new CritiqueJPA.ID(user, media));
        return critique;
    }

    public CritiqueJDBC toCritiqueJDBC(Long mediaId, Long userId) {
        CritiqueJDBC critique = new CritiqueJDBC();
        UserJDBC user = new UserJDBC();
        MediaJDBC media = new MediaJDBC();
        user.setId(userId);
        media.setId(mediaId);
        critique.setMedia(media);
        critique.setCritic(user);
        return critique;
    }

    public CritiqueJPA toCritiqueJPA(Long mediaId, Long userId) {
        CritiqueJPA critique = new CritiqueJPA();
        UserJPA user = new UserJPA();
        MediaJPA media = new MediaJPA();
        user.setId(userId);
        media.setId(mediaId);
        critique.setId(new CritiqueJPA.ID(user, media));
        return critique;
    }

}
