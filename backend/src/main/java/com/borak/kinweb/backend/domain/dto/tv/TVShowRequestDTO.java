/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.tv;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author Mr. Poyo
 */
@Validated
public class TVShowRequestDTO implements DTO {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "TV show title must not be null or empty!")
    @Size(max = 300, message = "TV show title must have less than 300 characters!")
    @JsonProperty(value = "title")
    private String title;

    @JsonIgnore
    private MyImage coverImage;

    @NotBlank(message = "TV show description must not be null or empty!")
    @Size(max = 500, message = "TV show description must have less than 500 characters!")
    @JsonProperty(value = "description")
    private String description;

    @NotNull(message = "TV show release date must not be null!")
    @JsonProperty(value = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy")
    private LocalDate releaseDate;

    @NotNull(message = "TV show audience rating must not be null!")
    @Min(value = 0, message = "TV show audience rating must be greater than or equal to 0!")
    @Max(value = 100, message = "TV show audience rating must be lower than or equal to 100!")
    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    @NotNull(message = "TV show number of seasons must not be null!")
    @Min(value = 0, message = "TV show number of seasons must be greater than or equal to 0!")
    @JsonProperty(value = "number_of_seasons")
    private Integer numberOfSeasons;

    @NotEmpty(message = "TV show genres must not be null or empty!")
    private List<
            @NotNull(message = "TV show genre id must not be null!")
            @Min(value = 1, message = "TV show genre id must be greater than or equal to 1!") Long> genres = new ArrayList<>();

    @NotEmpty(message = "TV show directors must not be null or empty!")
    private List<
            @NotNull(message = "TV show director id must not be null!")
            @Min(value = 1, message = "TV show director id must be greater than or equal to 1!") Long> directors = new ArrayList<>();

    @NotEmpty(message = "TV show writers must not be null or empty!")
    private List<
            @NotNull(message = "TV show writer id must not be null!")
            @Min(value = 1, message = "TV show writer id must be greater than or equal to 1!") Long> writers = new ArrayList<>();

    @Valid
    @NotEmpty(message = "TV show actors must not be null or empty!")
    private List<
            @NotNull(message = "TV show actor must not be null!") Actor> actors = new ArrayList<>();

    public TVShowRequestDTO() {
    }

    public static class Actor {

        @NotNull(message = "Actor id must not be null!")
        private Long id;

        @NotNull(message = "Actor starring value must not be null!")
        @JsonProperty(value = "is_starring")
        private Boolean starring;

        @NotEmpty(message = "Actors roles field must not be null or empty!")
        private List<
                @NotEmpty(message = "Actors role name must not be null or empty!")
                @Size(max = 300, message = "Actors role name must have less than 300 characters!") String> roles = new ArrayList<>();

        public Actor() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getStarring() {
            return starring;
        }

        public void setStarring(Boolean starring) {
            this.starring = starring;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
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

    public MyImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(MyImage coverImage) {
        this.coverImage = coverImage;
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

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public List<Long> getGenres() {
        return genres;
    }

    public void setGenres(List<Long> genres) {
        this.genres = genres;
    }

    public List<Long> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Long> directors) {
        this.directors = directors;
    }

    public List<Long> getWriters() {
        return writers;
    }

    public void setWriters(List<Long> writers) {
        this.writers = writers;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

}
