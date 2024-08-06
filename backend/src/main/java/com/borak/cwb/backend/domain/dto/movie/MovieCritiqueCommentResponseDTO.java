/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.movie;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDateTime;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({"id", "user", "content", "createdAt", "numberOfLikes", "numberOfDislikes"})
public class MovieCritiqueCommentResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;
    @JsonView(JsonVisibilityViews.Lite.class)
    private String content;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "number_of_likes")
    private Integer numberOfLikes;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "number_of_dislikes")
    private Integer numberOfDislikes;

    @JsonView(JsonVisibilityViews.Lite.class)
    private MovieCritiqueUserResponseDTO user;

    public MovieCritiqueCommentResponseDTO() {
    }

    public MovieCritiqueCommentResponseDTO(Long id, String content, LocalDateTime createdAt, Integer numberOfLikes, Integer numberOfDislikes, MovieCritiqueUserResponseDTO user) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getNumberOfDislikes() {
        return numberOfDislikes;
    }

    public void setNumberOfDislikes(Integer numberOfDislikes) {
        this.numberOfDislikes = numberOfDislikes;
    }

    public MovieCritiqueUserResponseDTO getUser() {
        return user;
    }

    public void setUser(MovieCritiqueUserResponseDTO user) {
        this.user = user;
    }

}
