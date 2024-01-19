/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGenreResponse {

    private List<ApiGenre> genres = new ArrayList<>();

    public ApiGenreResponse() {
    }

    public List<ApiGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<ApiGenre> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

}
