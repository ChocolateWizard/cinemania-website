/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;


import com.borak.kinweb.backend.domain.enums.Gender;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */

public class UserCriticJDBC extends UserJDBC{
    
  
    private List<CritiqueJDBC> critiques=new ArrayList<>();

    public UserCriticJDBC() {
       
    }

    public UserCriticJDBC(Long id, String firstName, String lastName, Gender gender, String profileImageUrl, String username, String email, String password, CountryJDBC country) {
        super(id, firstName, lastName, gender, profileImageUrl, username, email, password, country);
    }

    public List<CritiqueJDBC> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueJDBC> critiques) {
        this.critiques = critiques;
    }
    
    

   
    
    
    
}
