/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Mr. Poyo
 */
public class ActorDTO extends PersonDTO {

    @JsonProperty(value = "is_star")
    private boolean star = false;

    public ActorDTO() {
    }

    public ActorDTO(Long id, String firstName, String lastName, Gender gender, String profilePhotoURL, boolean isStar) {
        super(id, firstName, lastName, gender, profilePhotoURL);
        this.star = isStar;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

}
