/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.seeder.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class ApiCredits {

    @JsonProperty("cast")
    private List<ApiCreditsCast> cast = new ArrayList<>();

    @JsonProperty("crew")
    private List<ApiCreditsCrew> crew = new ArrayList<>();

    public ApiCredits() {
    }

    public List<ApiCreditsCast> getCast() {
        return cast;
    }

    public void setCast(List<ApiCreditsCast> cast) {
        if (cast == null) {
            this.cast = new ArrayList<>();
        } else {
            this.cast = cast;
        }
    }

    public List<ApiCreditsCrew> getCrew() {
        return crew;
    }

    public void setCrew(List<ApiCreditsCrew> crew) {
        if (crew == null) {
            this.crew = new ArrayList<>();
        } else {
            this.crew = crew;
        }
    }

}
