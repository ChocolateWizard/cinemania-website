/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jdbc;

/**
 *
 * @author Mr. Poyo
 */
public class CritiqueJDBC implements JDBC {

    private UserJDBC critic;

    private MediaJDBC media;

    private String description;

    private Integer rating;

    public CritiqueJDBC() {
    }

    public CritiqueJDBC(UserJDBC critic, MediaJDBC media, String description, Integer rating) {
        this.critic = critic;
        this.media = media;
        this.description = description;
        this.rating = rating;
    }

    public UserJDBC getCritic() {
        return critic;
    }

    public void setCritic(UserJDBC critic) {
        this.critic = critic;
    }

    public MediaJDBC getMedia() {
        return media;
    }

    public void setMedia(MediaJDBC media) {
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
