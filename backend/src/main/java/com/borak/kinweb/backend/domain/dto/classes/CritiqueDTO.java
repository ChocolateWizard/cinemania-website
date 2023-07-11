/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;



import com.borak.kinweb.backend.domain.jpa.classes.MediaJPA;
import com.borak.kinweb.backend.domain.jpa.classes.UserCriticJPA;
import java.io.Serializable;

/**
 *
 * @author Mr. Poyo
 */
public class CritiqueDTO implements Serializable {

    private UserCriticJPA critic;

    private MediaJPA media;

    private String description;

    private Integer rating;

    public CritiqueDTO() {
    }

    public CritiqueDTO(UserCriticJPA critic, MediaJPA media, String description, Integer rating) {
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
