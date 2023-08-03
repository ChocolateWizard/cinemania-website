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
public class DirectorDTO extends PersonDTO{

    public DirectorDTO() {
    }

    public DirectorDTO(Long id, String firstName, String lastName, Gender gender, String profilePhotoURL) {
        super(id, firstName, lastName, gender, profilePhotoURL);
    }
    
    
    
}
