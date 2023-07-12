/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;


import com.borak.kinweb.backend.domain.enums.Gender;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */

public class UserCriticPOJO extends UserPOJO{
    
  
    private List<CritiquePOJO> critiques=new ArrayList<>();

    public UserCriticPOJO() {
       
    }

    public UserCriticPOJO(List<CritiquePOJO> critiques, Long id, String firstName, String lastName, Gender gender, String profileImageUrl, String username, String email, String password, CountryPOJO country, List<MediaPOJO> library) {
        super(id, firstName, lastName, gender, profileImageUrl, username, email, password, country, library);
        this.critiques = critiques;
    }

    public List<CritiquePOJO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiquePOJO> critiques) {
        this.critiques = critiques;
    }
    
    
    
}
