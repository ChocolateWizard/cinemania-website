/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.media;

import com.borak.kinweb.backend.domain.enums.MediaType;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
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
    "mediaType",
    "genres"})
public class MediaResponseDTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    private String title;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "cover_image_url")
    private String coverImageUrl;

    @JsonView(JsonVisibilityViews.Medium.class)
    private String description;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy")
    private LocalDate releaseDate;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonProperty(value = "critics_rating")
    private Integer criticsRating;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "media_type")
    private MediaType mediaType;

    @JsonView(JsonVisibilityViews.Lite.class)
    private List<MediaResponseDTO.Genre> genres = new ArrayList<>();

    public MediaResponseDTO() {
    }

    public MediaResponseDTO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer audienceRating, Integer criticsRating, MediaType mediaType) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.audienceRating = audienceRating;
        this.criticsRating = criticsRating;
        this.mediaType = mediaType;
    }

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonPropertyOrder({"id", "name"})
    public static class Genre {

        private Long id;
        private String name;

        public Genre() {
        }

        public Genre(Long id, String name) {
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

    public List<MediaResponseDTO.Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<MediaResponseDTO.Genre> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

}
