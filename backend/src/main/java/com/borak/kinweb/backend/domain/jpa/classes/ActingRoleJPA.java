/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "ActingRole")
@Table(name = "acting_role")
public class ActingRoleJPA implements JPA{

    @Id
    private Long id;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns( {
    @JoinColumn(name="acting_media_id", referencedColumnName="media_id"),
    @JoinColumn(name="acting_actor_id", referencedColumnName="actor_id")
} )
    private ActingJPA acting;
    
    @Column(name = "name")
    private String name;

    public ActingRoleJPA() {
    }

    public ActingRoleJPA(Long id, ActingJPA acting, String name) {
        this.id = id;
        this.acting = acting;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActingJPA getActing() {
        return acting;
    }

    public void setActing(ActingJPA acting) {
        this.acting = acting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.acting);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActingRoleJPA other = (ActingRoleJPA) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.acting, other.acting);
    }

    

    
    
    
    

}
