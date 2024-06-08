/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Mr. Poyo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDiscoverMovie {

    private int id;

    public ApiDiscoverMovie() {
    }

    public ApiDiscoverMovie(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
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
        final ApiDiscoverMovie other = (ApiDiscoverMovie) obj;
        return this.id == other.id;
    }

}
