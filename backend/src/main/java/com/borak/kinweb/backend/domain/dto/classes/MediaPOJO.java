/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class MediaPOJO implements Serializable {

    private Long id;

    private String title;

    private String coverImageUrl;

    private String description;

    private LocalDate releaseDate;

    private Integer rating;

    private List<GenrePOJO> genres = new ArrayList<>();

    private List<CritiquePOJO> critiques = new ArrayList<>();

    private MediaCastPOJO cast;
    private MediaCrewPOJO crew;

    public MediaPOJO() {
    }

    public MediaPOJO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, MediaCastPOJO cast, MediaCrewPOJO crew) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.cast = cast;
        this.crew = crew;
    }

    public MediaCastPOJO getCast() {
        return cast;
    }

    public void setCast(MediaCastPOJO cast) {
        this.cast = cast;
    }

    public MediaCrewPOJO getCrew() {
        return crew;
    }

    public void setCrew(MediaCrewPOJO crew) {
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

    public List<GenrePOJO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenrePOJO> genres) {
        this.genres = genres;
    }

    public List<CritiquePOJO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiquePOJO> critiques) {
        this.critiques = critiques;
    }

}
