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

    public GenreDTO() {
    }
    
    public GenreDTO(Long id, String name) {
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
    
    

}
