/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.WhereJoinTable;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "Media")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "media")
@Access(AccessType.FIELD)
public abstract class MediaJPA implements JPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "audience_rating")
    private Integer audienceRating;

    @Column(name = "critic_rating", insertable = false, updatable = false)
    private Integer criticRating;

    @ManyToMany(targetEntity = GenreJPA.class)
    @JoinTable(
            name = "media_genres",
            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private List<GenreJPA> genres = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "media")
    private List<CritiqueJPA> critiques = new ArrayList<>();

    @ManyToMany(targetEntity = DirectorJPA.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "media_directors",
            joinColumns = {
                @JoinColumn(name = "media_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "director_id", referencedColumnName = "person_id")}, uniqueConstraints = {
                @UniqueConstraint(columnNames = {"media_id", "director_id"})}) 
    private List<DirectorJPA> directors = new ArrayList<DirectorJPA>();

    @ManyToMany(targetEntity = WriterJPA.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "media_writers",
            joinColumns = {
                @JoinColumn(name = "media_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "writer_id", referencedColumnName = "person_id")}, uniqueConstraints = {
                @UniqueConstraint(columnNames = {"media_id", "writer_id"})})  
    private List<WriterJPA> writers = new ArrayList<WriterJPA>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "media")
    private List<ActingJPA> actings = new ArrayList<ActingJPA>();

    public MediaJPA() {
    }

    public MediaJPA(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating) {
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

}
