/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.cwb.backend.domain.dto.critique.CritiqueResponseDTO;
import com.borak.cwb.backend.domain.dto.critique.CritiqueUserResponseDTO;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CritiqueTransformer {

    @Autowired
    private ConfigProperties config;

    public CritiqueJDBC toCritiqueJDBC(CritiqueRequestDTO request, Long userId) {
        CritiqueJDBC critique = new CritiqueJDBC();
        UserJDBC user = new UserJDBC();
        MediaJDBC media = new MediaJDBC();
        critique.setDescription(request.getDescription());
        critique.setRating(request.getRating());
        user.setId(userId);
        media.setId(request.getMediaId());
        critique.setMedia(media);
        critique.setCritic(user);
        return critique;
    }

    public CritiqueJPA toCritiqueJPA(CritiqueRequestDTO request, UserJPA user) {
        return new CritiqueJPA(
                request.getId(),
                user,
                new MediaJPA(request.getMediaId()),
                request.getDescription(),
                request.getRating(),
                LocalDateTime.now());
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
        return new CritiqueJPA(new UserJPA(userId), new MediaJPA(mediaId));
    }

    public CritiqueResponseDTO toCritiqueResponse(CritiqueJPA critique) {
        CritiqueResponseDTO response = new CritiqueResponseDTO();
        CritiqueUserResponseDTO critic = new CritiqueUserResponseDTO();
        response.setId(critique.getId());
        response.setMediaId(critique.getMedia().getId());
        response.setDescription(critique.getDescription());
        response.setRating(critique.getRating());
        response.setCreatedAt(critique.getCreatedAt());
        critic.setProfileName(critique.getUser().getProfileName());
        if (critique.getUser().getProfileImage() != null) {
            critic.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getUser().getProfileImage());
        }
        response.setCritic(critic);
        return response;
    }

}
