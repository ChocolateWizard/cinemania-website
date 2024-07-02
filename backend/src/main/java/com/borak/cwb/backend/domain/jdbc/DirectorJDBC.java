/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jdbc;

import com.borak.cwb.backend.domain.enums.Gender;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class DirectorJDBC extends PersonJDBC {

    private List<MediaJDBC> medias = new ArrayList<>();

    public DirectorJDBC() {
    }

    public DirectorJDBC(Long id) {
        super(id);
    }

    public DirectorJDBC(Long id, String firstName, String lastName, Gender gender, String profilePhotoURL) {
        super(id, firstName, lastName, gender, profilePhotoURL);
    }

    public List<MediaJDBC> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaJDBC> medias) {
        if (medias == null) {
            this.medias = new ArrayList<>();
        } else {
            this.medias = medias;
        }
    }

}
