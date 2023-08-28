/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.logic.transformers.serializers.media.MediaActingsJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.media.MediaCritiquesJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.media.MediaDirectorsJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.media.MediaGenresJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.media.MediaWritersJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr Poyo
 */
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonPropertyOrder({
    "id",
    "title",
    "releaseDate",
    "coverImageUrl",
    "description",
    "audienceRating",
    "criticRating",
    "genres", "directors", "writers", "actings", "critiques"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.ALWAYS)
public abstract class MediaDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @NotNull(message = "Title must not be null")
    @JsonView(JsonVisibilityViews.Lite.class)
    private String title;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "cover_image_url")
    private String coverImageUrl;

    @NotNull(message = "Description must not be null")
    @JsonView(JsonVisibilityViews.Medium.class)
    private String description;

    @NotNull(message = "Release date must not be null")
    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy")
    private LocalDate releaseDate;

    @NotNull(message = "Audience rating must not be null")
    @Min(value = 0, message = "Audience rating must be greater than or equal to 0")
    @Max(value = 100, message = "Audience rating must be less than or equal to 100")
    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonProperty(value = "critic_rating")
    private Integer criticRating;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonSerialize(using = MediaGenresJsonSerializer.class)
    private List<GenreDTO> genres = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonSerialize(using = MediaCritiquesJsonSerializer.class)
    private List<CritiqueDTO> critiques = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonSerialize(using = MediaDirectorsJsonSerializer.class)
    private List<DirectorDTO> directors = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonSerialize(using = MediaWritersJsonSerializer.class)
    private List<WriterDTO> writers = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonProperty(value = "actors")
    @JsonSerialize(using = MediaActingsJsonSerializer.class)
    private List<ActingDTO> actings = new ArrayList<>();

    @Override
    public String toString() {
        return "MediaDTO{" + "id=" + id + ", title=" + title + ", coverImage=" + coverImageUrl + ", description=" + description + ", releaseDate=" + releaseDate + ", audienceRating=" + audienceRating + ", criticRating=" + criticRating + ", genres=" + genres + ", critiques=" + critiques + ", directors=" + directors + ", writers=" + writers + ", actings=" + actings + '}';
    }
    
    

    public MediaDTO() {
    }

    public MediaDTO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.audienceRating = audienceRating;
        this.criticRating = criticRating;
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

    public Integer getCriticRating() {
        return criticRating;
    }

    public void setCriticRating(Integer criticRating) {
        this.criticRating = criticRating;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    
    

    public void setGenres(List<GenreDTO> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }

    }

    public List<CritiqueDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueDTO> critiques) {
        if (critiques == null) {
            this.critiques = new ArrayList<>();
        } else {
            this.critiques = critiques;
        }
    }

    public List<DirectorDTO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorDTO> directors) {
        if (directors == null) {
            this.directors = new ArrayList<>();
        } else {
            this.directors = directors;
        }
    }

    public List<WriterDTO> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterDTO> writers) {
        if (writers == null) {
            this.writers = new ArrayList<>();
        } else {
            this.writers = writers;
        }
    }

    public List<ActingDTO> getActings() {
        return actings;
    }

    public void setActings(List<ActingDTO> actings) {
        if (actings == null) {
            this.actings = new ArrayList<>();
        } else {
            this.actings = actings;
        }
    }

}
