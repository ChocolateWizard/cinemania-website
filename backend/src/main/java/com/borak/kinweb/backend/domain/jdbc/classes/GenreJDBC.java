/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;

import java.sql.Types;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public class GenreJDBC implements JDBC {

    private Long id;

    private String name;

    private List<MediaJDBC> medias = new ArrayList<>();

    public GenreJDBC() {
    }

    public GenreJDBC(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
