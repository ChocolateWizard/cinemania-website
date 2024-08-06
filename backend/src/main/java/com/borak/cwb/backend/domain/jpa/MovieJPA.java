/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Movie")
@Table(name = "movie")
@PrimaryKeyJoinColumn(name = "media_id")
public class MovieJPA extends MediaJPA {

    @NotNull(message = "Length must not be null")
    @Min(value = 0, message = "Length must be greater than or equal to 0")
    @Column(name = "length", nullable = false)
    private Integer length;

    public MovieJPA() {
    }

    public MovieJPA(Long id) {
        super(id);
    }

    public MovieJPA(String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticsRating, Integer length) {
        super(title, coverImage, description, releaseDate, audienceRating, criticsRating);
        this.length = length;
    }

    public MovieJPA(Long id, String title, String coverImage, String description, LocalDate releaseDate, Integer audienceRating, Integer criticsRating, Integer length) {
        super(id, title, coverImage, description, releaseDate, audienceRating, criticsRating);
        this.length = length;
    }

    public MovieJPA(Long id, String title, String coverImage, String description, LocalDate releaseDate, LocalDateTime createdAt, LocalDateTime updatedAt, Integer audienceRating, Integer criticsRating, Integer length) {
        super(id, title, coverImage, description, releaseDate, createdAt, updatedAt, audienceRating, criticsRating);
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
