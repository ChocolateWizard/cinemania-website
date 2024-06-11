/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.seeder.domain.db;

import com.borak.cwb.backend.domain.classes.MyImage;
import java.time.LocalDate;

/**
 *
 * @author Mr Poyo
 */
public class TVShowDB extends MediaDB {

    private Integer numberOfSeasons;

    public TVShowDB() {
    }

    public TVShowDB(Long id, String title, String coverImageName, String coverImagePath, MyImage coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer numberOfSeasons) {
        super(id, title, coverImageName, coverImagePath, coverImage, description, releaseDate, audienceRating);
        this.numberOfSeasons = numberOfSeasons;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

}
