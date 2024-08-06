/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.critique;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "id",
    "user",
    "content",
    "createdAt",
    "likedBy",
    "dislikedBy"})
public class CritiqueCommentResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    private CritiqueUserResponseDTO user;

    @JsonView(JsonVisibilityViews.Lite.class)
    private String content;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonProperty(value = "liked_by")
    private List<CritiqueUserResponseDTO> likedBy = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonProperty(value = "disliked_by")
    private List<CritiqueUserResponseDTO> dislikedBy = new ArrayList<>();

    public CritiqueCommentResponseDTO() {
    }

    public CritiqueCommentResponseDTO(Long id, CritiqueUserResponseDTO user, String content, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CritiqueUserResponseDTO getUser() {
        return user;
    }

    public void setUser(CritiqueUserResponseDTO user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CritiqueUserResponseDTO> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<CritiqueUserResponseDTO> likedBy) {
        this.likedBy = likedBy;
    }

    public List<CritiqueUserResponseDTO> getDislikedBy() {
        return dislikedBy;
    }

    public void setDislikedBy(List<CritiqueUserResponseDTO> dislikedBy) {
        this.dislikedBy = dislikedBy;
    }

}
