/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.critique.CritiqueCommentResponseDTO;
import com.borak.cwb.backend.domain.dto.critique.CritiqueUserResponseDTO;
import com.borak.cwb.backend.domain.jpa.CommentJPA;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CommentTransformer {

    @Autowired
    private ConfigProperties config;

    public CritiqueCommentResponseDTO toCritiqueCommentResponse(CommentJPA comment) {
        CritiqueUserResponseDTO user = new CritiqueUserResponseDTO();
        user.setProfileName(comment.getUser().getProfileName());
        if (comment.getUser().getProfileImage() != null) {
            user.setProfileImageUrl(config.getUserImagesBaseUrl() + comment.getUser().getProfileImage());
        }
        return new CritiqueCommentResponseDTO(comment.getId(), user, comment.getContent(), comment.getCreatedAt());
    }

}
