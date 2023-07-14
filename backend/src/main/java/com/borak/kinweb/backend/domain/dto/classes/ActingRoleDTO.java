/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.pojo.classes.*;
import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public class ActingRoleDTO implements DTO{

    private ActingDTO acting;
    private List<String> roles;

    public ActingRoleDTO() {
    }

    public ActingRoleDTO(ActingDTO acting, List<String> roles) {
        this.acting = acting;
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public ActingDTO getActing() {
        return acting;
    }

    public void setActing(ActingDTO acting) {
        this.acting = acting;
    }
    
    

}
