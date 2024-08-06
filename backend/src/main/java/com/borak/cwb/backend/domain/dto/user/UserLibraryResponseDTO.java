/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.user;

import com.borak.cwb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "movies",
    "tvShows"
})
public class UserLibraryResponseDTO implements DTO {

    private List<UserLibraryMediaResponseDTO> movies = new ArrayList<>();

    @JsonProperty(value = "tv_shows")
    private List<UserLibraryMediaResponseDTO> tvShows = new ArrayList<>();

    public UserLibraryResponseDTO() {
    }

    public List<UserLibraryMediaResponseDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<UserLibraryMediaResponseDTO> movies) {
        this.movies = movies;
    }

    public List<UserLibraryMediaResponseDTO> getTvShows() {
        return tvShows;
    }

    public void setTvShows(List<UserLibraryMediaResponseDTO> tvShows) {
        this.tvShows = tvShows;
    }

}
