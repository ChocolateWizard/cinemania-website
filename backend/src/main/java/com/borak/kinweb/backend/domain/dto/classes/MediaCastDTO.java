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
public class MediaCastDTO{
    
    private List<ActingDTO> actorsAndRoles = new ArrayList<>();

    public List<ActingDTO> getActorsAndRoles() {
        return actorsAndRoles;
    }

    public void setActorsAndRoles(List<ActingDTO> actorsAndRoles) {
        this.actorsAndRoles = actorsAndRoles;
    }
    
    
    
}
