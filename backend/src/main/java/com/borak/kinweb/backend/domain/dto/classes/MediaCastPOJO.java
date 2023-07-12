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
public class MediaCastPOJO {
    
    private List<ActingPOJO> actorsAndRoles = new ArrayList<>();

    public List<ActingPOJO> getActorsAndRoles() {
        return actorsAndRoles;
    }

    public void setActorsAndRoles(List<ActingPOJO> actorsAndRoles) {
        this.actorsAndRoles = actorsAndRoles;
    }
    
    
    
}
