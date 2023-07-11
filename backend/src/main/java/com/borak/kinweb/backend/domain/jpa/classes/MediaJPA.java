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
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "Media")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "media")
@Access(AccessType.FIELD)
public abstract class MediaJPA implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;  

    @Column(name = "title", length = 300, nullable = false)
    private String title;

    @Column(name = "cover_image_url", length = 500, nullable = false)
    private String coverImageUrl;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @ManyToMany
    @JoinTable(
            name = "media_genres",
            joinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private List<GenreJPA> genres;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CritiqueJPA> critiques;

    public MediaJPA() {
    }

    public MediaJPA(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres, List<CritiqueJPA> critiques) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genres = genres;
        this.critiques = critiques;
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
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final MediaJPA other = (MediaJPA) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return title + " (" + releaseDate + ')';
    }
    
    
  
    
    
    

}
