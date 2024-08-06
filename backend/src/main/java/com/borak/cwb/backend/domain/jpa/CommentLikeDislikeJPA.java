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
@Entity(name = "CommentLikeDislike")
@Table(name = "comment_like_dislike")
@Access(AccessType.FIELD)
public class CommentLikeDislikeJPA implements Serializable {

    @EmbeddedId
    private CommentLikeDislikeJPA.ID id;

    @NotNull
    @Column(name = "is_like", nullable = false)
    private Boolean isLike;

    public CommentLikeDislikeJPA() {
    }

    public CommentLikeDislikeJPA(ID id, Boolean isLike) {
        this.id = id;
        this.isLike = isLike;
    }

    public static class ID implements Serializable {

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
        private UserJPA user;

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
        private CommentJPA comment;

        public ID() {
        }

        public ID(UserJPA user, CommentJPA comment) {
            this.user = user;
            this.comment = comment;
        }

        public UserJPA getUser() {
            return user;
        }

        public void setUser(UserJPA user) {
            this.user = user;
        }

        public CommentJPA getComment() {
            return comment;
        }

        public void setComment(CommentJPA comment) {
            this.comment = comment;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Objects.hashCode(this.user);
            hash = 23 * hash + Objects.hashCode(this.comment);
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
            return Objects.equals(this.comment, other.comment);
        }

    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
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
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final CommentLikeDislikeJPA other = (CommentLikeDislikeJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
