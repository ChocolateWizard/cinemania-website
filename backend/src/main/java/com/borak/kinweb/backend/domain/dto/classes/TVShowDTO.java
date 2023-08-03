/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import java.time.LocalDate;

/**
 *
 * @author Mr Poyo
 */
public class TVShowDTO extends MediaDTO {

    private Integer numberOfSeasons;

    public TVShowDTO() {
    }

    public TVShowDTO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer audienceRating, Integer criticRating, Integer numberOfSeasons) {
        super(id, title, coverImageUrl, description, releaseDate, audienceRating, criticRating);
        this.numberOfSeasons = numberOfSeasons;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

}
