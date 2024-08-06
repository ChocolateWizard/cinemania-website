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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({"id", "critic", "rating", "description", "createdAt", "numberOfLikes", "numberOfDislikes", "comments"})
public class MovieCritiqueResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;
    @JsonView(JsonVisibilityViews.Lite.class)
    private MovieCritiqueUserResponseDTO critic;
    @JsonView(JsonVisibilityViews.Lite.class)
    private Integer rating;
    @JsonView(JsonVisibilityViews.Lite.class)
    private String description;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "number_of_likes")
    private Integer numberOfLikes;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "number_of_dislikes")
    private Integer numberOfDislikes;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonView(JsonVisibilityViews.Lite.class)
    private List<MovieCritiqueCommentResponseDTO> comments = new ArrayList<>();

    public MovieCritiqueResponseDTO() {
    }

    public MovieCritiqueResponseDTO(Long id, MovieCritiqueUserResponseDTO critic, Integer rating, String description, Integer numberOfLikes, Integer numberOfDislikes, LocalDateTime createdAt) {
        this.id = id;
        this.critic = critic;
        this.rating = rating;
        this.description = description;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieCritiqueUserResponseDTO getCritic() {
        return critic;
    }

    public void setCritic(MovieCritiqueUserResponseDTO critic) {
        this.critic = critic;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<MovieCritiqueCommentResponseDTO> getComments() {
        return comments;
    }

    public void setComments(List<MovieCritiqueCommentResponseDTO> comments) {
        this.comments = comments;
    }

}
