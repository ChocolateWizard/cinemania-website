/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;


import com.borak.kinweb.backend.domain.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "UserCritic")
@Table(name = "user_critic")
@PrimaryKeyJoinColumn(name = "user_id")
public class UserCriticJPA extends UserJPA{
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CritiqueJPA> critiques=new ArrayList<>();

    public UserCriticJPA() {
       
    }

    public UserCriticJPA(List<CritiqueJPA> critiques, String username, String password) {
        super(username, password);
        this.critiques = critiques;
    }

    public UserCriticJPA(List<CritiqueJPA> critiques, Long id, String firstName, String lastName, Gender gender, String profileImageUrl, String username, String email, String password, CountryJPA country, List<MediaJPA> library) {
        super(id, firstName, lastName, gender, profileImageUrl, username, email, password, country, library);
        this.critiques = critiques;
    } 
      

    public List<CritiqueJPA> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueJPA> critiques) {
        this.critiques = critiques;
    }
    
    
    
}
