/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public abstract class MediaDTO implements DTO {

    private Long id;

    private String title;

    private String coverImageUrl;

    private String description;

    private LocalDate releaseDate;
    private DateTimeFormatter releaseDateFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    private Integer audienceRating;
    
    private Integer criticRating;

    private List<GenreDTO> genres = new ArrayList<>();

    private List<CritiqueDTO> critiques = new ArrayList<>();

    private List<DirectorDTO> directors = new ArrayList<>();

    private List<WriterDTO> writers = new ArrayList<>();

    private List<ActingDTO> actings = new ArrayList<>();

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
    
    public String getReleaseDateAsString() {
        if (releaseDate == null) {
            return null;
        }
        return releaseDate.format(releaseDateFormatter);
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

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }

    public List<CritiqueDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueDTO> critiques) {
        this.critiques = critiques;
    }

    public List<DirectorDTO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorDTO> directors) {
        this.directors = directors;
    }

    public List<WriterDTO> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterDTO> writers) {
        this.writers = writers;
    }

    public List<ActingDTO> getActings() {
        return actings;
    }

    public void setActings(List<ActingDTO> actings) {
        this.actings = actings;
    }

   

    
    
    

}
