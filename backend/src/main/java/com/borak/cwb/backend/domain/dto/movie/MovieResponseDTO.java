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
    "description",
    "audienceRating",
    "criticsRating",
    "length",
    "genres", "directors", "writers", "actors", "critiques"})
public class MovieResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    private String title;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy")
    private LocalDate releaseDate;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "cover_image_url")
    private String coverImageUrl;

    @JsonView(JsonVisibilityViews.Medium.class)
    private String description;

    @JsonView(JsonVisibilityViews.Medium.class)
    private Integer length;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonProperty(value = "critics_rating")
    private Integer criticsRating;

    @JsonView(JsonVisibilityViews.Lite.class)
    private List<MovieGenreResponseDTO> genres = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<MovieDirectorResponseDTO> directors = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<MovieWriterResponseDTO> writers = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<MovieActorResponseDTO> actors = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<MovieCritiqueResponseDTO> critiques = new ArrayList<>();

    public MovieResponseDTO() {
    }

    public MovieResponseDTO(Long id, String title, LocalDate releaseDate, String coverImageUrl, String description, Integer length, Integer audienceRating, Integer criticsRating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.length = length;
        this.audienceRating = audienceRating;
        this.criticsRating = criticsRating;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(Integer audienceRating) {
        this.audienceRating = audienceRating;
    }

    public Integer getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(Integer criticsRating) {
        this.criticsRating = criticsRating;
    }

    public List<MovieGenreResponseDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieGenreResponseDTO> genres) {
        this.genres = genres;
    }

    public List<MovieDirectorResponseDTO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<MovieDirectorResponseDTO> directors) {
        this.directors = directors;
    }

    public List<MovieWriterResponseDTO> getWriters() {
        return writers;
    }

    public void setWriters(List<MovieWriterResponseDTO> writers) {
        this.writers = writers;
    }

    public List<MovieActorResponseDTO> getActors() {
        return actors;
    }

    public void setActors(List<MovieActorResponseDTO> actors) {
        this.actors = actors;
    }

    public List<MovieCritiqueResponseDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<MovieCritiqueResponseDTO> critiques) {
        this.critiques = critiques;
    }

}
