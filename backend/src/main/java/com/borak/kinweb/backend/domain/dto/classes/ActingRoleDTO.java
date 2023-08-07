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
public class ActingRoleDTO implements DTO{

    private ActingDTO acting;
    private Long id;
    private String name;

    public ActingRoleDTO() {
    }

    public ActingRoleDTO(ActingDTO acting, String name) {
        this.acting = acting;
        this.name = name;
    }

    public ActingRoleDTO(ActingDTO acting, Long id, String name) {
        this.acting = acting;
        this.id = id;
        this.name = name;
    }

    public ActingDTO getActing() {
        return acting;
    }

    public void setActing(ActingDTO acting) {
        this.acting = acting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
  
    
    

}
