/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Critique")
@Table(name = "critique")
@Access(AccessType.FIELD)
public class CritiqueJPA implements Serializable {

    @EmbeddedId
    private CritiqueJPA.ID id;

    @NotBlank
    @Size(max = 500)
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Embeddable
    public static class ID implements Serializable {

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "user_critic_id", referencedColumnName = "id")
        private UserJPA critic;

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "media_id", referencedColumnName = "id")
        private MediaJPA media;

        public ID() {
        }

        public ID(UserJPA critic, MediaJPA media) {
            this.critic = critic;
            this.media = media;
        }

        public UserJPA getCritic() {
            return critic;
        }

        public void setCritic(UserJPA critic) {
            this.critic = critic;
        }

        public MediaJPA getMedia() {
            return media;
        }

        public void setMedia(MediaJPA media) {
            this.media = media;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.critic);
            hash = 37 * hash + Objects.hashCode(this.media);
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
            final ID other = (ID) obj;
            if (!Objects.equals(this.critic, other.critic)) {
                return false;
            }
            return Objects.equals(this.media, other.media);
        }

    }

    public CritiqueJPA() {
    }

    public CritiqueJPA(ID id, String description, Integer rating) {
        this.id = id;
        this.description = description;
        this.rating = rating;
    }

    public CritiqueJPA.ID getId() {
        return id;
    }

    public void setId(CritiqueJPA.ID id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
