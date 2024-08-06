/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Actor")
@Table(name = "actor")
public class ActorJPA implements Serializable {

    @Id
    @NotNull(message = "Person id must not be null")
    @Column(name = "person_id")
    private Long personId;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId
    private PersonJPA person;

    @NotNull(message = "Star status must not be null")
    @Column(name = "is_star", nullable = false)
    private Boolean star;

    @OneToMany(mappedBy = "actor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActingJPA> actings = new ArrayList<>();

    public ActorJPA() {
    }

    public ActorJPA(Long personId) {
        this.personId = personId;
    }

    public ActorJPA(Long personId, PersonJPA person, Boolean star) {
        this.personId = personId;
        this.person = person;
        this.star = star;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public PersonJPA getPerson() {
        return person;
    }

    public void setPerson(PersonJPA person) {
        this.person = person;
    }

    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    public List<ActingJPA> getActings() {
        return actings;
    }

    public void setActings(List<ActingJPA> actings) {
        this.actings = actings;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.personId);
        hash = 13 * hash + Objects.hashCode(this.person);
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
        final ActorJPA other = (ActorJPA) obj;
        if (!Objects.equals(this.personId, other.personId)) {
            return false;
        }
        return Objects.equals(this.person, other.person);
    }

    @Override
    public String toString() {
        return "Actor: " + person;
    }

}
