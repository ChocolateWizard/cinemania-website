/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.enums.Gender;

/**
 *
 * @author Mr. Poyo
 */
public class ActorDTO extends PersonDTO {

    private boolean isStar=false;

    public ActorDTO() {
    }

    public ActorDTO(Long id, String firstName, String lastName, Gender gender, String profilePhotoURL, boolean isStar) {
        super(id, firstName, lastName, gender, profilePhotoURL);
        this.isStar = isStar;
    }

    public boolean isIsStar() {
        return isStar;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    

}
