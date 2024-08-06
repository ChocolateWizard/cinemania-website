/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.user;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.domain.enums.MediaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "id",
    "title",
    "releaseDate",
    "coverImageUrl",
    "audienceRating",
    "criticsRating",
    "genres"
})
public class UserLibraryMediaResponseDTO implements DTO {

    private Long id;
    private String title;

    @JsonProperty(value = "release_date")
    private LocalDate releaseDate;

    @JsonProperty(value = "cover_image_url")
    private String coverImageUrl;

    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    private List<UserLibraryMediaGenreResponseDTO> genres = new ArrayList<>();

    public UserLibraryMediaResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Integer getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(Integer audienceRating) {
        this.audienceRating = audienceRating;
    }

    public List<UserLibraryMediaGenreResponseDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<UserLibraryMediaGenreResponseDTO> genres) {
        this.genres = genres;
    }
    
    

}
