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

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull
    @Column(name = "gender", nullable = false, length = 1)
    private Gender gender;

    @Size(max = 30)
    @Column(name = "profile_photo", length = 30)
    private String profilePhoto;

    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private DirectorJPA directorInfo;
    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private WriterJPA writerInfo;
    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private ActorJPA actorInfo;

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

    public PersonJPA(Long id, String firstName, String lastName, Gender gender, String profilePhoto, DirectorJPA directorInfo, WriterJPA writerInfo, ActorJPA actorInfo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.directorInfo = directorInfo;
        this.writerInfo = writerInfo;
        this.actorInfo = actorInfo;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public DirectorJPA getDirectorInfo() {
        return directorInfo;
    }

    public void setDirectorInfo(DirectorJPA directorInfo) {
        this.directorInfo = directorInfo;
    }

    public WriterJPA getWriterInfo() {
        return writerInfo;
    }

    public void setWriterInfo(WriterJPA writerInfo) {
        this.writerInfo = writerInfo;
    }

    public ActorJPA getActorInfo() {
        return actorInfo;
    }

    public void setActorInfo(ActorJPA actorInfo) {
        this.actorInfo = actorInfo;
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
