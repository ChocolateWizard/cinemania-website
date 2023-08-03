/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public class GenreDTO implements DTO {

    private Long id;

    private String name;

    private List<MediaDTO> medias=new ArrayList<>();

    public GenreDTO() {
    }
    
    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreDTO(Long id, String name, List<MediaDTO> medias) {
        this.id = id;
        this.name = name;
        this.medias = medias;
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

    public List<MediaDTO> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaDTO> medias) {
        this.medias = medias;
    }
    
    

}
