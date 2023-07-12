/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.enums.Gender;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class UserPOJO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String profileImageUrl;

    private String username;

    private String email;

    private String password;

    private CountryPOJO country;

    private List<MediaPOJO> library=new ArrayList<>();

    public UserPOJO() {
    }

    public UserPOJO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserPOJO(Long id, String firstName, String lastName, Gender gender, String profileImageUrl, String username, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserPOJO(Long id, String firstName, String lastName, Gender gender, String profileImageUrl, String username, String email, String password, CountryPOJO country, List<MediaPOJO> library) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.email = email;
        this.password = password;
        this.country = country;
        this.library = library;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CountryPOJO getCountry() {
        return country;
    }

    public void setCountry(CountryPOJO country) {
        this.country = country;
    }

    public List<MediaPOJO> getLibrary() {
        return library;
    }

    public void setLibrary(List<MediaPOJO> library) {
        this.library = library;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.email);
        hash = 53 * hash + Objects.hashCode(this.password);
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
        final UserPOJO other = (UserPOJO) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return Objects.equals(this.password, other.password);
    }

    

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
