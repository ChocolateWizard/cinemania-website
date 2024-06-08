/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.db;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.enums.Gender;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class PersonDB {

    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String profilePhotoName;

    private String profilePhotoPath;

    private MyImage profilePhoto;

    public PersonDB() {
    }

    public PersonDB(Long id) {
        this.id = id;
    }

    public PersonDB(Long id, String firstName, String lastName, Gender gender, String profilePhotoName, String profilePhotoPath, MyImage profilePhoto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhotoName = profilePhotoName;
        this.profilePhotoPath = profilePhotoPath;
        this.profilePhoto = profilePhoto;
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

    public String getProfilePhotoName() {
        return profilePhotoName;
    }

    public void setProfilePhotoName(String profilePhotoName) {
        this.profilePhotoName = profilePhotoName;
    }

    public MyImage getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(MyImage profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final PersonDB other = (PersonDB) obj;
        return Objects.equals(this.id, other.id);
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

}
