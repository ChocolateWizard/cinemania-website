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
    "mediaId",
    "critic",
    "description",
    "rating",
    "createdAt",
    "likedBy",
    "dislikedBy",
    "comments"})
public class CritiqueResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "media_id")
    private Long mediaId;

    @JsonView(JsonVisibilityViews.Lite.class)
    private CritiqueUserResponseDTO critic;

    @JsonView(JsonVisibilityViews.Lite.class)
    private String description;

    @JsonView(JsonVisibilityViews.Lite.class)
    private Integer rating;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonProperty(value = "liked_by")
    private List<CritiqueUserResponseDTO> likedBy = new ArrayList<>();

    @JsonProperty(value = "disliked_by")
    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<CritiqueUserResponseDTO> dislikedBy = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<CritiqueCommentResponseDTO> comments = new ArrayList<>();

    public CritiqueResponseDTO() {
    }

    public CritiqueResponseDTO(Long id, Long mediaId, CritiqueUserResponseDTO critic, String description, Integer rating, LocalDateTime createdAt) {
        this.id = id;
        this.mediaId = mediaId;
        this.critic = critic;
        this.description = description;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public CritiqueUserResponseDTO getCritic() {
        return critic;
    }

    public void setCritic(CritiqueUserResponseDTO critic) {
        this.critic = critic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public List<CritiqueCommentResponseDTO> getComments() {
        return comments;
    }

    public void setComments(List<CritiqueCommentResponseDTO> comments) {
        this.comments = comments;
    }

}
