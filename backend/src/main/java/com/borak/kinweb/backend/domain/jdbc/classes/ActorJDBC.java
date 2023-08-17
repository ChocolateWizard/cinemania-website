/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;

import com.borak.kinweb.backend.domain.enums.Gender;

/**
 *
 * @author Mr. Poyo
 */
public class ActorJDBC extends PersonJDBC {

    private boolean star=false;

    public ActorJDBC() {
    }

    public ActorJDBC(Long id, String firstName, String lastName, Gender gender, String profilePhotoURL, boolean isStar) {
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
