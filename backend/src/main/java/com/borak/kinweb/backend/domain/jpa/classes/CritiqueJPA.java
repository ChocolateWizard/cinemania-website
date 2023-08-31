/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Critique")
@Table(name = "critique")
public class CritiqueJPA implements JPA {

    @Id   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_critic_id")
    private UserCriticJPA critic;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private MediaJPA media;
 
    private String description;

    private Integer rating;

    public CritiqueJPA() {
    }

    public CritiqueJPA(UserCriticJPA critic, MediaJPA media, String description, Integer rating) {
        this.critic = critic;
        this.media = media;
        this.description = description;
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public UserCriticJPA getCritic() {
        return critic;
    }

    public void setCritic(UserCriticJPA critic) {
        this.critic = critic;
    }

    public MediaJPA getMedia() {
        return media;
    }

    public void setMedia(MediaJPA media) {
        this.media = media;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.critic);
        hash = 67 * hash + Objects.hashCode(this.media);
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
        if (!Objects.equals(this.critic, other.critic)) {
            return false;
        }
        return Objects.equals(this.media, other.media);
    }

}
