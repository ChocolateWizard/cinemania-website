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

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Critique")
@Table(name = "critique")
public class CritiqueJPA implements Serializable {

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

}
