/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "UserCritic")
@Table(name = "user_critic")
@PrimaryKeyJoinColumn(name = "user_id")
public class UserCriticJPA extends UserJPA{
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CritiqueJPA> critiques;

      

    public List<CritiqueJPA> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueJPA> critiques) {
        this.critiques = critiques;
    }
    
    
    
}
