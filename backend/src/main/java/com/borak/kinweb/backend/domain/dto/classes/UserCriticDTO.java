/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;


import java.util.List;

/**
 *
 * @author Mr. Poyo
 */

public class UserCriticDTO extends UserDTO{
    
  
    private List<CritiqueDTO> critiques;
   

    public List<CritiqueDTO> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueDTO> critiques) {
        this.critiques = critiques;
    }
    
    
    
}
