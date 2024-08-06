/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.tv;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({"id", "name"})
public class TVShowGenreResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;
    @JsonView(JsonVisibilityViews.Lite.class)
    private String name;

    public TVShowGenreResponseDTO() {
    }

    public TVShowGenreResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
