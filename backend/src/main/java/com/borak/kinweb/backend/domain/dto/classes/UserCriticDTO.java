/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;


import com.borak.kinweb.backend.domain.pojo.classes.*;
import com.borak.kinweb.backend.domain.enums.Gender;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */

public class UserCriticDTO extends UserDTO{
    
  
    private List<CritiqueDTO> critiques=new ArrayList<>();

    public UserCriticDTO() {
       
    }

    public UserCriticDTO(List<CritiqueDTO> critiques, Long id, String firstName, String lastName, Gender gender, String profileImageUrl, String username, String email, String password, CountryDTO country, List<MediaDTO> library) {
        super(id, firstName, lastName, gender, profileImageUrl, username, email, password, country, library);
        this.critiques = critiques;
    }

    public List<CritiqueDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueDTO> critiques) {
        this.critiques = critiques;
    }
    
    
    
}
