/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import com.borak.cwb.backend.domain.enums.Gender;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Person")
@Table(name = "person")
@Access(AccessType.FIELD)
public class PersonJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name must not be empty")
    @Size(max = 100, message = "First name must be less than or equal to 100 characters")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Size(max = 100, message = "Last name must be less than or equal to 100 characters")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull(message = "Gender must not be null")
    @Column(name = "gender", nullable = false, length = 1)
    private Gender gender;

    @Size(max = 30, message = "Profile photo must be less than or equal to 30 characters")
    @Column(name = "profile_photo", length = 30)
    private String profilePhoto;

    @NotNull(message = "Created at must not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private DirectorJPA director;
    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private WriterJPA writer;
    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private ActorJPA actor;

    public PersonJPA() {
    }

    public PersonJPA(Long id) {
        this.id = id;
    }

    public PersonJPA(String firstName, String lastName, Gender gender, String profilePhoto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
    }

    public PersonJPA(Long id, String firstName, String lastName, Gender gender, String profilePhoto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
    }

    public PersonJPA(Long id, String firstName, String lastName, Gender gender, String profilePhoto, DirectorJPA director, WriterJPA writer, ActorJPA actor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.director = director;
        this.writer = writer;
        this.actor = actor;
    }

    public PersonJPA(Long id, String firstName, String lastName, Gender gender, String profilePhoto, LocalDateTime createdAt, LocalDateTime updatedAt, DirectorJPA director, WriterJPA writer, ActorJPA actor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.director = director;
        this.writer = writer;
        this.actor = actor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public DirectorJPA getDirector() {
        return director;
    }

    public void setDirector(DirectorJPA director) {
        this.director = director;
    }

    public WriterJPA getWriter() {
        return writer;
    }

    public void setWriter(WriterJPA writer) {
        this.writer = writer;
    }

    public ActorJPA getActor() {
        return actor;
    }

    public void setActor(ActorJPA actor) {
        this.actor = actor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final PersonJPA other = (PersonJPA) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
