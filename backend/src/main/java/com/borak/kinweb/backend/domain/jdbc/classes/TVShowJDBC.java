/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;

import java.time.LocalDate;



/**
 *
 * @author Mr Poyo
 */

public class TVShowJDBC extends MediaJDBC {
  

    private Integer numberOfSeasons;

    public TVShowJDBC() {
        
    }

    public TVShowJDBC(Long id, String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating,Integer numberOfSeasons) {
        super(id, title, coverImage, description, releaseDate, audienceRating, criticRating);
        this.numberOfSeasons = numberOfSeasons;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    
    
}
