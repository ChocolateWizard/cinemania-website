/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.pojo.classes.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class MediaDTO implements DTO {

    private Long id;

    private String title;

    private String coverImageUrl;

    private String description;

    private LocalDate releaseDate;

    private Integer rating;

    private List<GenreDTO> genres = new ArrayList<>();

    private List<CritiqueDTO> critiques = new ArrayList<>();

    private MediaCastDTO cast;
    private MediaCrewDTO crew;

    public MediaDTO() {
    }

    public MediaDTO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, MediaCastDTO cast, MediaCrewDTO crew) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.cast = cast;
        this.crew = crew;
    }

    public MediaCastDTO getCast() {
        return cast;
    }

    public void setCast(MediaCastDTO cast) {
        this.cast = cast;
    }

    public MediaCrewDTO getCrew() {
        return crew;
    }

    public void setCrew(MediaCrewDTO crew) {
        this.crew = crew;
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

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }

    public List<CritiqueDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueDTO> critiques) {
        this.critiques = critiques;
    }

}
