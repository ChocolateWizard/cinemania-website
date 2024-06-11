/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.seeder.domain.db;

/**
 *
 * @author Mr Poyo
 */
public class ActingRoleDB {

    private ActingDB acting;
    private Long id;
    private String name;

    public ActingRoleDB() {
    }

    public ActingRoleDB(ActingDB acting, String name) {
        this.acting = acting;
        this.name = name;
    }

    public ActingRoleDB(ActingDB acting, Long id, String name) {
        this.acting = acting;
        this.id = id;
        this.name = name;
    }

    public ActingDB getActing() {
        return acting;
    }

    public void setActing(ActingDB acting) {
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
