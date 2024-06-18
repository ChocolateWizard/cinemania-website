/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Entity(name = "Director")
@Table(name = "director")
public class DirectorJPA implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_id")
    private Long personId;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false, unique = true, insertable = false, updatable = false)
    private PersonJPA person;

    @ManyToMany(mappedBy = "directors", fetch = FetchType.LAZY)
    private List<MediaJPA> medias = new ArrayList<>();

    public DirectorJPA() {
    }

    public DirectorJPA(Long personId) {
        this.personId = personId;
    }

    public DirectorJPA(Long personId, PersonJPA person) {
        this.personId = personId;
        this.person = person;
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

    public List<MediaJPA> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaJPA> medias) {
        this.medias = medias;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.personId);
        hash = 97 * hash + Objects.hashCode(this.person);
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
        final DirectorJPA other = (DirectorJPA) obj;
        if (!Objects.equals(this.personId, other.personId)) {
            return false;
        }
        return Objects.equals(this.person, other.person);
    }

    @Override
    public String toString() {
        return "Director: " + person;
    }

}
