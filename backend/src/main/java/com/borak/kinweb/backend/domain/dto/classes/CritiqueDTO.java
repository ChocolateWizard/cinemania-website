/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;



import com.borak.kinweb.backend.domain.pojo.classes.*;
import com.borak.kinweb.backend.domain.jpa.classes.MediaJPA;
import com.borak.kinweb.backend.domain.jpa.classes.UserCriticJPA;
import java.io.Serializable;

/**
 *
 * @author Mr. Poyo
 */
public class CritiqueDTO implements DTO {

    private UserCriticDTO critic;

    private MediaDTO media;

    private String description;

    private Integer rating;

    public CritiqueDTO() {
    }

    public CritiqueDTO(UserCriticDTO critic, MediaDTO media, String description, Integer rating) {
        this.critic = critic;
        this.media = media;
        this.description = description;
        this.rating = rating;
    }

    public UserCriticDTO getCritic() {
        return critic;
    }

    public void setCritic(UserCriticDTO critic) {
        this.critic = critic;
    }

    public MediaDTO getMedia() {
        return media;
    }

    public void setMedia(MediaDTO media) {
        this.media = media;
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

    
    
    
    
    

}
