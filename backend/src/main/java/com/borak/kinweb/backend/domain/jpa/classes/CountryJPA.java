/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "Country")
@Table(name = "country")
public class CountryJPA implements Serializable,JPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "official_state_name")
    private String officialStateName;

    private String code;

    public CountryJPA() {
    }

    public CountryJPA(String name, String officialStateName, String code) {
        this.name = name;
        this.officialStateName = officialStateName;
        this.code = code;
    }

    public CountryJPA(Long id, String name, String officialStateName, String code) {
        this.id = id;
        this.name = name;
        this.officialStateName = officialStateName;
        this.code = code;
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

    public String getOfficialStateName() {
        return officialStateName;
    }

    public void setOfficialStateName(String officialStateName) {
        this.officialStateName = officialStateName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.code);
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
        final CountryJPA other = (CountryJPA) obj;
        return Objects.equals(this.code, other.code);
    }
    
    @Override
    public String toString() {
        return name + " (" + code + ')';
    }

}
