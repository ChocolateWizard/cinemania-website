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

    private boolean isStar;

    public ActorJDBC() {
        this.isStar = false;
    }

    public ActorJDBC(Long id, String firstName, String lastName, Gender gender, String profilePhotoURL, boolean isStar) {
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
