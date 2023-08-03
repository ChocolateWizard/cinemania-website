/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;

import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public class ActingRoleJDBC implements JDBC{

    private ActingJDBC acting;
    private Long id;
    private String name;

    public ActingRoleJDBC() {
    }

    public ActingRoleJDBC(ActingJDBC acting, String name) {
        this.acting = acting;
        this.name = name;
    }

    public ActingRoleJDBC(ActingJDBC acting, Long id, String name) {
        this.acting = acting;
        this.id = id;
        this.name = name;
    }

    public ActingJDBC getActing() {
        return acting;
    }

    public void setActing(ActingJDBC acting) {
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
