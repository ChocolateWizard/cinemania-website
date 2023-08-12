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
public class ActingDTO implements DTO {

    private MediaDTO media;
    private ActorDTO actor;
    private boolean starring=false;  
    private List<ActingRoleDTO> roles = new ArrayList<>();

    public ActingDTO() {
    }
    
    

    public MediaDTO getMedia() {
        return media;
    }

    public void setMedia(MediaDTO media) {
        this.media = media;
    }

    public ActorDTO getActor() {
        return actor;
    }

    public void setActor(ActorDTO actor) {
        this.actor = actor;
    }

    public List<ActingRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRoleDTO> roles) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        } else {
            this.roles = roles;
        }
    }

    public boolean isStarring() {
        return starring;
    }

    public void setStarring(boolean starring) {
        this.starring = starring;
    }
    
    

}
