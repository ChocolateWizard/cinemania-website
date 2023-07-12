/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public class ActingRolePOJO {

    private ActingPOJO acting;
    private List<String> roles;

    public ActingRolePOJO() {
    }

    public ActingRolePOJO(ActingPOJO acting, List<String> roles) {
        this.acting = acting;
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public ActingPOJO getActing() {
        return acting;
    }

    public void setActing(ActingPOJO acting) {
        this.acting = acting;
    }
    
    

}
