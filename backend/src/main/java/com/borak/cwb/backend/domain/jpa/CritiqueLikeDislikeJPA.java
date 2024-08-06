/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "CritiqueLikeDislike")
@Table(name = "critique_like_dislike")
@Access(AccessType.FIELD)
public class CritiqueLikeDislikeJPA implements Serializable {

    @EmbeddedId
    private CritiqueLikeDislikeJPA.ID id;

    @NotNull
    @Column(name = "is_like", nullable = false)
    private Boolean isLike;

    public static class ID implements Serializable {

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
        private UserJPA user;

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "critique_id", referencedColumnName = "id", nullable = false)
        private CritiqueJPA critique;

        public ID() {
        }

        public ID(UserJPA user, CritiqueJPA critique) {
            this.user = user;
            this.critique = critique;
        }

        public UserJPA getUser() {
            return user;
        }

        public void setUser(UserJPA user) {
            this.user = user;
        }

        public CritiqueJPA getCritique() {
            return critique;
        }

        public void setCritique(CritiqueJPA critique) {
            this.critique = critique;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.user);
            hash = 79 * hash + Objects.hashCode(this.critique);
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
            final ID other = (ID) obj;
            if (!Objects.equals(this.user, other.user)) {
                return false;
            }
            return Objects.equals(this.critique, other.critique);
        }

    }

    public CritiqueLikeDislikeJPA() {
    }

    public CritiqueLikeDislikeJPA(ID id, Boolean isLike) {
        this.id = id;
        this.isLike = isLike;
    }

    public CritiqueLikeDislikeJPA.ID getId() {
        return id;
    }

    public void setId(CritiqueLikeDislikeJPA.ID id) {
        this.id = id;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final CritiqueLikeDislikeJPA other = (CritiqueLikeDislikeJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
