/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.db;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.enums.Gender;

/**
 *
 * @author Mr. Poyo
 */
public class ActorDB extends PersonDB {

    private boolean star;

    public ActorDB() {
    }

    public ActorDB(Long id, String firstName, String lastName, Gender gender, String profilePhotoName, String profilePhotoPath, MyImage profilePhoto, boolean star) {
        super(id, firstName, lastName, gender, profilePhotoName, profilePhotoPath, profilePhoto);
        this.star = star;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

}
