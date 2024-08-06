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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
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
@Entity(name = "Writer")
@Table(name = "writer")
public class WriterJPA implements Serializable {

    @Id
    @NotNull(message = "Person id must not be null")
    @Column(name = "person_id")
    private Long personId;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId
    private PersonJPA person;

    @ManyToMany(targetEntity = MediaJPA.class, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "media_writers",
            joinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "person_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false))
    private List<MediaJPA> medias = new ArrayList<>();

    public WriterJPA() {
    }

    public WriterJPA(Long personId) {
        this.personId = personId;
    }

    public WriterJPA(Long personId, PersonJPA person) {
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
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.personId);
        hash = 79 * hash + Objects.hashCode(this.person);
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
        final WriterJPA other = (WriterJPA) obj;
        if (!Objects.equals(this.personId, other.personId)) {
            return false;
        }
        return Objects.equals(this.person, other.person);
    }

    @Override
    public String toString() {
        return "Writer: " + person;
    }

}
