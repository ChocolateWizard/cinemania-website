/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class MediaCrewPOJO {
    
    private List<DirectorPOJO> directors = new ArrayList<>();

    private List<WriterPOJO> writers = new ArrayList<>();

    public MediaCrewPOJO() {
    }

    public List<DirectorPOJO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorPOJO> directors) {
        this.directors = directors;
    }

    public List<WriterPOJO> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterPOJO> writers) {
        this.writers = writers;
    }
    
    
}
