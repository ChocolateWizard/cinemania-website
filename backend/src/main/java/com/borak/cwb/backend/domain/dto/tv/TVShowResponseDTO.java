/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.tv;

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
    "numberOfSeasons",
    "genres", "directors", "writers", "actors", "critiques"})
public class TVShowResponseDTO implements DTO {

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
    @JsonProperty(value = "number_of_seasons")
    private Integer numberOfSeasons;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonProperty(value = "critics_rating")
    private Integer criticsRating;

    @JsonView(JsonVisibilityViews.Lite.class)
    private List<TVShowGenreResponseDTO> genres = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<TVShowDirectorResponseDTO> directors = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<TVShowWriterResponseDTO> writers = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<TVShowActorResponseDTO> actors = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<TVShowCritiqueResponseDTO> critiques = new ArrayList<>();

    public TVShowResponseDTO() {
    }

    public TVShowResponseDTO(Long id, String title, LocalDate releaseDate, String coverImageUrl, String description, Integer numberOfSeasons, Integer audienceRating, Integer criticsRating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.numberOfSeasons = numberOfSeasons;
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

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
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

    public List<TVShowGenreResponseDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<TVShowGenreResponseDTO> genres) {
        this.genres = genres;
    }

    public List<TVShowDirectorResponseDTO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<TVShowDirectorResponseDTO> directors) {
        this.directors = directors;
    }

    public List<TVShowWriterResponseDTO> getWriters() {
        return writers;
    }

    public void setWriters(List<TVShowWriterResponseDTO> writers) {
        this.writers = writers;
    }

    public List<TVShowActorResponseDTO> getActors() {
        return actors;
    }

    public void setActors(List<TVShowActorResponseDTO> actors) {
        this.actors = actors;
    }

    public List<TVShowCritiqueResponseDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<TVShowCritiqueResponseDTO> critiques) {
        this.critiques = critiques;
    }

}
