/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.critique;

import com.borak.cwb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Mr. Poyo
 */
public class CritiqueRequestDTO implements DTO {

    @JsonIgnore
    private Long id;

    @NotNull
    @Min(value = 1, message = "Critique media id must be greater than or equal to 1")
    @JsonProperty(value = "media_id")
    private Long mediaId;

    @NotBlank(message = "Description must not be null or empty")
    @Size(max = 500, message = "Description must have less than 500 characters")
    @JsonProperty(value = "description")
    private String description;

    @NotNull(message = "Media rating must not be null")
    @Min(value = 0, message = "Media rating must be greater than or equal to 0")
    @Max(value = 100, message = "Media rating must be lower than or equal to 100")
    @JsonProperty(value = "rating")
    private Integer rating;

    public CritiqueRequestDTO() {
    }

    public CritiqueRequestDTO(Long mediaId, String description, Integer rating) {
        this.mediaId = mediaId;
        this.description = description;
        this.rating = rating;
    }

    public CritiqueRequestDTO(Long id, Long mediaId, String description, Integer rating) {
        this.id = id;
        this.mediaId = mediaId;
        this.description = description;
        this.rating = rating;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
