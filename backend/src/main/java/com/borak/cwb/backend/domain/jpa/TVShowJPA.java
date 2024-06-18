/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "TVShow")
@Table(name = "tv_show")
@PrimaryKeyJoinColumn(name = "media_id")
public class TVShowJPA extends MediaJPA {

    @NotNull
    @Min(value = 0)
    @Column(name = "number_of_seasons", nullable = false)
    private Integer numberOfSeasons;

    public TVShowJPA() {

    }

    public TVShowJPA(String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer numberOfSeasons) {
        super(title, coverImage, description, releaseDate, audienceRating, criticRating);
        this.numberOfSeasons = numberOfSeasons;
    }

    public TVShowJPA(Long id, String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer numberOfSeasons) {
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
