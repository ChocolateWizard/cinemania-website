/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Country")
@Table(name = "country")
@Access(AccessType.FIELD)
public class CountryJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be empty")
    @Size(max = 300, message = "Name must be less than or equal to 300 characters")
    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @NotBlank(message = "Official state name must not be empty")
    @Size(max = 300, message = "Official state name must be less than or equal to 300 characters")
    @Column(name = "official_state_name", nullable = false, length = 300)
    private String officialStateName;

    @NotBlank(message = "Code must not be empty")
    @Size(min = 2, max = 2, message = "Code must be 2 characters long")
    @Column(name = "code", nullable = false, length = 2, unique = true)
    private String code;

    public CountryJPA() {
    }

    public CountryJPA(Long id) {
        this.id = id;
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
        hash = 79 * hash + Objects.hashCode(this.id);
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
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return name;
    }

}
