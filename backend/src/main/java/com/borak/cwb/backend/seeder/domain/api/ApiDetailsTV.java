/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.seeder.domain.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class ApiDetailsTV {

    private int id;
    private String name;

    @JsonProperty(value = "poster_path")
    private String posterPath;

    @JsonProperty(value = "overview")
    private String overview;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty(value = "first_air_date")
    private LocalDate firstAirDate;

    @JsonProperty(value = "number_of_seasons")
    private int numberOfSeasons;

    @JsonProperty(value = "vote_average")
    private double voteAverage;

    private List<ApiGenre> genres = new ArrayList<>();

    @JsonProperty(value = "created_by")
    private List<ApiTVCreator> createdBy = new ArrayList<>();

    @JsonProperty(value = "credits")
    private ApiCredits credits;

    public ApiDetailsTV() {
    }

    public ApiDetailsTV(int id, String name, String posterPath, String overview, LocalDate firstAirDate, int numberOfSeasons, double voteAverage, ApiCredits credits) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.overview = overview;
        this.firstAirDate = firstAirDate;
        this.numberOfSeasons = numberOfSeasons;
        this.voteAverage = voteAverage;
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public LocalDate getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(LocalDate firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public List<ApiGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<ApiGenre> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

    public List<ApiTVCreator> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<ApiTVCreator> createdBy) {
        if (createdBy == null) {
            this.createdBy = new ArrayList<>();
        } else {
            this.createdBy = createdBy;
        }
    }

    public ApiCredits getCredits() {
        return credits;
    }

    public void setCredits(ApiCredits credits) {
        this.credits = credits;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ApiDetailsTV other = (ApiDetailsTV) obj;
        return this.id == other.id;
    }

}
