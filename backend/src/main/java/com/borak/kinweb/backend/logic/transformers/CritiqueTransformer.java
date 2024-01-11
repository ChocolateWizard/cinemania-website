/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
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

}
