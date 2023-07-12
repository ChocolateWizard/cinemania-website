/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "TVShow")
@Table(name = "tv_show")
@PrimaryKeyJoinColumn(name = "media_id")
public class TVShowJPA extends MediaJPA {

    @Column(name = "number_of_seasons")
    private Integer numberOfSeasons;

    public TVShowJPA() {

    }

    public TVShowJPA(Integer numberOfSeasons, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating) {
        super(title, coverImageUrl, description, releaseDate, rating);
        this.numberOfSeasons = numberOfSeasons;
    }

    public TVShowJPA(Integer numberOfSeasons, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres) {
        super(title, coverImageUrl, description, releaseDate, rating, genres);
        this.numberOfSeasons = numberOfSeasons;
    }

    public TVShowJPA(Integer numberOfSeasons, Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres) {
        super(id, title, coverImageUrl, description, releaseDate, rating, genres);
        this.numberOfSeasons = numberOfSeasons;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

}
