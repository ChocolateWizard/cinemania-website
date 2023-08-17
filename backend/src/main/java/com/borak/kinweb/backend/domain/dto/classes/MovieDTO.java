/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;

/**
 *
 * @author Mr Poyo
 */
public class MovieDTO extends MediaDTO {

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

}
