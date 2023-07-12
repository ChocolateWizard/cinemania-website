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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "Media")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "media")
public class MediaJPA implements Serializable{

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

    private Integer rating;

    @ManyToMany
    @JoinTable(
            name = "media_genres",
            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private List<GenreJPA> genres=new ArrayList<>();
    
//    @ManyToMany
//    @JoinTable(
//            name = "media_directors",
//            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "director_id", referencedColumnName = "person_id"))
//    private List<DirectorJPA> directors=new ArrayList<>();
//    
//    @ManyToMany
//    @JoinTable(
//            name = "media_writers",
//            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "person_id"))
//    private List<WriterJPA> writers=new ArrayList<>();
//    
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ActingJPA> cast=new ArrayList<>();
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CritiqueJPA> critiques=new ArrayList<>();

    public MediaJPA() {
    }

    public MediaJPA(String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating) {
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public MediaJPA(String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres) {
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genres = genres;
    }

    public MediaJPA(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genres = genres;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    @Override
    public String toString() {
        return title;
    }

    
  
    
    
    

}
