/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class ApiDiscoverMovieResponse {

    private int page;
    private List<ApiDiscoverMovie> results = new ArrayList<>();
    @JsonProperty(value = "total_pages")
    private int totalPages;
    @JsonProperty(value = "total_results")
    private int totalResults;

    public ApiDiscoverMovieResponse() {
    }

    public ApiDiscoverMovieResponse(int page, int totalPages, int totalResults) {
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ApiDiscoverMovie> getResults() {
        return results;
    }

    public void setResults(List<ApiDiscoverMovie> results) {
        if (results == null) {
            this.results = new ArrayList<>();
        } else {
            this.results = results;
        }
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
