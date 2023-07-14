/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.pojo.classes;


import java.io.Serializable;

/**
 *
 * @author Mr. Poyo
 */
public class CritiquePOJO implements POJO {

    private UserCriticPOJO critic;

    private MediaPOJO media;

    private String description;

    private Integer rating;

    public CritiquePOJO() {
    }

    public CritiquePOJO(UserCriticPOJO critic, MediaPOJO media, String description, Integer rating) {
        this.critic = critic;
        this.media = media;
        this.description = description;
        this.rating = rating;
    }

    public UserCriticPOJO getCritic() {
        return critic;
    }

    public void setCritic(UserCriticPOJO critic) {
        this.critic = critic;
    }

    public MediaPOJO getMedia() {
        return media;
    }

    public void setMedia(MediaPOJO media) {
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
