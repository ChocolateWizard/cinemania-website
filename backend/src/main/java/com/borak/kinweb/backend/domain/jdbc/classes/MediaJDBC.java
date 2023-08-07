/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public abstract class MediaJDBC implements JDBC {

    private Long id;

    private String title;

    private String coverImageUrl;

    private String description;

    private LocalDate releaseDate;

    private Integer audienceRating;

    private Integer criticRating;

    private List<GenreJDBC> genres = new ArrayList<>();

    private List<CritiqueJDBC> critiques = new ArrayList<>();

    private List<DirectorJDBC> directors = new ArrayList<>();

    private List<WriterJDBC> writers = new ArrayList<>();

    private List<ActingJDBC> actings = new ArrayList<>();

    public MediaJDBC() {
    }

    public MediaJDBC(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating) {
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

    public Integer getCriticRating() {
        return criticRating;
    }

    public void setCriticRating(Integer criticRating) {
        this.criticRating = criticRating;
    }

    public List<GenreJDBC> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreJDBC> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

    public List<CritiqueJDBC> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueJDBC> critiques) {
        if (critiques == null) {
            this.critiques = new ArrayList<>();
        } else {
            this.critiques = critiques;
        }
    }

    public List<DirectorJDBC> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorJDBC> directors) {
        if (directors == null) {
            this.directors = new ArrayList<>();
        } else {
            this.directors = directors;
        }
    }

    public List<WriterJDBC> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterJDBC> writers) {
        if (writers == null) {
            this.writers = new ArrayList<>();
        } else {
            this.writers = writers;
        }
    }

    public List<ActingJDBC> getActings() {
        return actings;
    }

    public void setActings(List<ActingJDBC> actings) {
        if (actings == null) {
            this.actings = new ArrayList<>();
        } else {
            this.actings = actings;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MediaJDBC other = (MediaJDBC) obj;
        return Objects.equals(this.id, other.id);
    }

}
