/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 *
 * @author Mr Poyo
 */
@JsonPropertyOrder({
    "id",
    "title",
    "releaseDate",
    "coverImageUrl",
    "description",
    "audienceRating",
    "criticRating",
    "length",
    "genres", "directors", "writers", "actings", "critiques"})
public class MovieDTO extends MediaDTO {

    @NotNull(message = "Length must not be null")
    @JsonView(JsonVisibilityViews.Medium.class)
    private Integer length;

    public MovieDTO() {

    }

    public MovieDTO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer length) {
        super(id, title, coverImageUrl, description, releaseDate, audienceRating, criticRating);
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return super.toString() + "length=" + length;
    }

    

    
    
}
