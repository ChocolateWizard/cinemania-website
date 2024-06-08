/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.db;

import com.borak.kinweb.backend.domain.classes.MyImage;
import java.time.LocalDate;

/**
 *
 * @author Mr Poyo
 */
public class MovieDB extends MediaDB {

    private Integer length;

    public MovieDB() {
    }

    public MovieDB(Long id, String title, String coverImageName, String coverImagePath, MyImage coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer length) {
        super(id, title, coverImageName, coverImagePath, coverImage, description, releaseDate, audienceRating);
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
