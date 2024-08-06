/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Media")
@Table(name = "media")
@Inheritance(strategy = InheritanceType.JOINED)
@Access(AccessType.FIELD)
public class MediaJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title must not be empty")
    @Size(max = 300, message = "Title must be less than or equal to 300 characters")
    @Column(name = "title", nullable = false, length = 300)
    private String title;

    @Size(max = 30, message = "Cover image must be less than or equal to 30 characters")
    @Column(name = "cover_image", length = 30)
    private String coverImage;

    @NotBlank(message = "Description must not be empty")
    @Size(max = 1000, message = "Description must be less than or equal to 1000 characters")
    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @NotNull(message = "Release date must not be null")
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @NotNull(message = "Created at must not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @NotNull(message = "Audience rating must not be null")
    @Min(value = 0, message = "Audience rating must be greater than or equal to 0")
    @Max(value = 100, message = "Audience rating must be less than or equal to 100")
    @Column(name = "audience_rating", nullable = false)
    private Integer audienceRating;

    @Column(name = "critics_rating", insertable = false, updatable = false)
    private Integer criticsRating;

    @ManyToMany(targetEntity = GenreJPA.class, fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "media_genres",
            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false))
    private List<GenreJPA> genres = new ArrayList<>();

    @ManyToMany(targetEntity = DirectorJPA.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "media_directors",
            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "director_id", referencedColumnName = "person_id", nullable = false))
    private List<DirectorJPA> directors = new ArrayList<>();

    @ManyToMany(targetEntity = WriterJPA.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "media_writers",
            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "person_id", nullable = false))
    private List<WriterJPA> writers = new ArrayList<>();

    @OneToMany(mappedBy = "media", targetEntity = ActingJPA.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActingJPA> actings = new ArrayList<>();

    @OneToMany(mappedBy = "media", targetEntity = CritiqueJPA.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CritiqueJPA> critiques = new ArrayList<>();

    public MediaJPA() {
    }

    public MediaJPA(Long id) {
        this.id = id;
    }

    public MediaJPA(String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticsRating) {
        this.title = title;
        this.coverImage = coverImage;
        this.description = description;
        this.releaseDate = releaseDate;
        this.audienceRating = audienceRating;
        this.criticsRating = criticsRating;
    }

    public MediaJPA(Long id, String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticsRating) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.description = description;
        this.releaseDate = releaseDate;
        this.audienceRating = audienceRating;
        this.criticsRating = criticsRating;
    }

    public MediaJPA(Long id, String title, String coverImage, String description, LocalDate releaseDate, LocalDateTime createdAt, LocalDateTime updatedAt, Integer audienceRating, Integer criticsRating) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.description = description;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
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

    public Integer getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(Integer criticsRating) {
        this.criticsRating = criticsRating;
    }

    public List<GenreJPA> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreJPA> genres) {
        this.genres = genres;
    }

    public List<CritiqueJPA> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueJPA> critiques) {
        this.critiques = critiques;
    }

    public List<DirectorJPA> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorJPA> directors) {
        this.directors = directors;
    }

    public List<WriterJPA> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterJPA> writers) {
        this.writers = writers;
    }

    public List<ActingJPA> getActings() {
        return actings;
    }

    public void setActings(List<ActingJPA> actings) {
        this.actings = actings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        if (!(obj instanceof MediaJPA)) {
            return false;
        }
        final MediaJPA other = (MediaJPA) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return title + (releaseDate != null ? "(" + releaseDate.getYear() + ")" : "");
    }

}
