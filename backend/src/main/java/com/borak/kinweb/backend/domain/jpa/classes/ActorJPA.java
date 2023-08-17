/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Actor")
@Table(name = "actor")
@PrimaryKeyJoinColumn(name = "person_id")
public class ActorJPA extends PersonJPA{
    
   
    
    @Column(name = "is_star")
    private boolean star=false;

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }
 

    
    
    
    
}
