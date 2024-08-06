/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Acting")
@Table(name = "acting", uniqueConstraints = @UniqueConstraint(columnNames = {"media_id", "actor_id"}))
@Access(AccessType.FIELD)
public class ActingJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false)
    private MediaJPA media;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "actor_id", referencedColumnName = "person_id", nullable = false)
    private ActorJPA actor;

    @NotNull(message = "Starring status must not be null")
    @Column(name = "is_starring", nullable = false)
    private Boolean starring;

    @OneToMany(mappedBy = "id.acting", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActingRoleJPA> roles = new ArrayList<>();

    public ActingJPA() {
    }

    public ActingJPA(MediaJPA media, ActorJPA actor, Boolean starring) {
        this.media = media;
        this.actor = actor;
        this.starring = starring;
    }

    public ActingJPA(Long id, MediaJPA media, ActorJPA actor, Boolean starring) {
        this.id = id;
        this.media = media;
        this.actor = actor;
        this.starring = starring;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaJPA getMedia() {
        return media;
    }

    public void setMedia(MediaJPA media) {
        this.media = media;
    }

    public ActorJPA getActor() {
        return actor;
    }

    public void setActor(ActorJPA actor) {
        this.actor = actor;
    }

    public Boolean getStarring() {
        return starring;
    }

    public void setStarring(Boolean starring) {
        this.starring = starring;
    }

    public List<ActingRoleJPA> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRoleJPA> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final ActingJPA other = (ActingJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
