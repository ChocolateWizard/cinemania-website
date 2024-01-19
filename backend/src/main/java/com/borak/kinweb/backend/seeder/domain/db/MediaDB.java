/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.db;

import com.borak.kinweb.backend.domain.classes.MyImage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class MediaDB {

    private Long id;

    private String title;

    private String coverImageName;
    private String coverImagePath;
    private MyImage coverImage;

    private String description;

    private LocalDate releaseDate;

    private Integer audienceRating;

    private List<GenreDB> genres = new ArrayList<>();

    private List<DirectorDB> directors = new ArrayList<>();

    private List<WriterDB> writers = new ArrayList<>();

    private List<ActingDB> actings = new ArrayList<>();

    public MediaDB() {
    }

    public MediaDB(Long id, String title, String coverImageName, String coverImagePath, MyImage coverImage, String description, LocalDate releaseDate, Integer audienceRating) {
        this.id = id;
        this.title = title;
        this.coverImageName = coverImageName;
        this.coverImagePath = coverImagePath;
        this.coverImage = coverImage;
        this.description = description;
        this.releaseDate = releaseDate;
        this.audienceRating = audienceRating;
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

    public String getCoverImageName() {
        return coverImageName;
    }

    public void setCoverImageName(String coverImageName) {
        this.coverImageName = coverImageName;
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

    public List<GenreDB> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDB> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

    public List<DirectorDB> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorDB> directors) {
        if (directors == null) {
            this.directors = new ArrayList<>();
        } else {
            this.directors = directors;
        }
    }

    public List<WriterDB> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterDB> writers) {
        if (writers == null) {
            this.writers = new ArrayList<>();
        } else {
            this.writers = writers;
        }
    }

    public List<ActingDB> getActings() {
        return actings;
    }

    public void setActings(List<ActingDB> actings) {
        if (actings == null) {
            this.actings = new ArrayList<>();
        } else {
            this.actings = actings;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final MediaDB other = (MediaDB) obj;
        return Objects.equals(this.id, other.id);
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

}
