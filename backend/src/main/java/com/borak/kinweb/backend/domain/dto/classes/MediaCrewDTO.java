/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.pojo.classes.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class MediaCrewDTO{
    
    private List<DirectorDTO> directors = new ArrayList<>();

    private List<WriterDTO> writers = new ArrayList<>();

    public MediaCrewDTO() {
    }

    public List<DirectorDTO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorDTO> directors) {
        this.directors = directors;
    }

    public List<WriterDTO> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterDTO> writers) {
        this.writers = writers;
    }
    
    
}
