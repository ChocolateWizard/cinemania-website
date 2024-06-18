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
@Entity(name = "Movie")
@Table(name = "movie")
@PrimaryKeyJoinColumn(name = "media_id")
public class MovieJPA extends MediaJPA {

    @NotNull
    @Min(value = 0)
    @Column(name = "length", nullable = false)
    private Integer length;

    public MovieJPA() {
    }

    public MovieJPA(String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer length) {
        super(title, coverImage, description, releaseDate, audienceRating, criticRating);
        this.length = length;
    }

    public MovieJPA(Long id, String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer length) {
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
