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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Comment")
@Table(name = "comment", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "critique_id"}))
@Access(AccessType.FIELD)
public class CommentJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserJPA user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "critique_id", referencedColumnName = "id", nullable = false)
    private CritiqueJPA critique;

    @NotBlank
    @Size(max = 300)
    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(targetEntity = CommentLikeDislikeJPA.class, mappedBy = "id.comment", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CommentLikeDislikeJPA> likeDislikes = new ArrayList<>();

    public CommentJPA() {
    }

    public CommentJPA(Long id) {
        this.id = id;
    }

    public CommentJPA(UserJPA user, CritiqueJPA critique, String content, LocalDateTime createdAt) {
        this.user = user;
        this.critique = critique;
        this.content = content;
        this.createdAt = createdAt;
    }

    public CommentJPA(Long id, UserJPA user, CritiqueJPA critique, String content, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.critique = critique;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentLikeDislikeJPA> getLikeDislikes() {
        return likeDislikes;
    }

    public void setLikeDislikes(List<CommentLikeDislikeJPA> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.id);
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
        final CommentJPA other = (CommentJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
