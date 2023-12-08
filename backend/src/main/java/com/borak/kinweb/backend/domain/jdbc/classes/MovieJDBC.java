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
public class MovieJDBC extends MediaJDBC{

    private Integer length;

    public MovieJDBC() {

    }

    public MovieJDBC(Long id, String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer length) {
        super(id, title, coverImage, description, releaseDate, audienceRating, criticRating);
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
