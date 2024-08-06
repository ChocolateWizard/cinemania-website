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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Critique")
@Table(name = "critique", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "media_id"}))
@Access(AccessType.FIELD)
public class CritiqueJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserJPA user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false)
    private MediaJPA media;

    @NotBlank(message = "Description must not be empty")
    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @NotNull(message = "Rating must not be null")
    @Min(value = 0, message = "Rating must be greater than or equal to 0")
    @Max(value = 100, message = "Rating must be less than or equal to 100")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull(message = "Created at must not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(targetEntity = CritiqueLikeDislikeJPA.class, mappedBy = "id.critique", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CritiqueLikeDislikeJPA> likeDislikes = new ArrayList<>();

    @OneToMany(targetEntity = CommentJPA.class, mappedBy = "critique", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CommentJPA> comments = new ArrayList<>();

    public CritiqueJPA() {
    }

    public CritiqueJPA(Long id) {
        this.id = id;
    }

    public CritiqueJPA(UserJPA user, MediaJPA media) {
        this.user = user;
        this.media = media;
    }

    public CritiqueJPA(UserJPA user, MediaJPA media, String description, Integer rating) {
        this.user = user;
        this.media = media;
        this.description = description;
        this.rating = rating;
    }

    public CritiqueJPA(Long id, UserJPA user, MediaJPA media, String description, Integer rating) {
        this.id = id;
        this.user = user;
        this.media = media;
        this.description = description;
        this.rating = rating;
    }

    public CritiqueJPA(Long id, UserJPA user, MediaJPA media, String description, Integer rating, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.media = media;
        this.description = description;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserJPA getUser() {
        return user;
    }

    public void setUser(UserJPA user) {
        this.user = user;
    }

    public MediaJPA getMedia() {
        return media;
    }

    public void setMedia(MediaJPA media) {
        this.media = media;
    }

    public List<CritiqueLikeDislikeJPA> getLikeDislikes() {
        return likeDislikes;
    }

    public void setLikeDislikes(List<CritiqueLikeDislikeJPA> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }

    public List<CommentJPA> getComments() {
        return comments;
    }

    public void setComments(List<CommentJPA> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final CritiqueJPA other = (CritiqueJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
