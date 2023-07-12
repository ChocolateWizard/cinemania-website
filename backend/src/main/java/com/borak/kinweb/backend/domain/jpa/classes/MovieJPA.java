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
@Entity(name = "Movie")
@Table(name = "movie")
@PrimaryKeyJoinColumn(name = "media_id")
public class MovieJPA extends MediaJPA {

    private Integer length;

    public MovieJPA() {
    }

    public MovieJPA(Integer length, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating) {
        super(title, coverImageUrl, description, releaseDate, rating);
        this.length = length;
    }

    public MovieJPA(Integer length, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres) {
        super(title, coverImageUrl, description, releaseDate, rating, genres);
        this.length = length;
    }

    public MovieJPA(Integer length, Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer rating, List<GenreJPA> genres) {
        super(id, title, coverImageUrl, description, releaseDate, rating, genres);
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
