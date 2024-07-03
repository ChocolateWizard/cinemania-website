/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.enums.UserRole;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Entity(name = "User")
@Table(name = "user")
@Access(AccessType.FIELD)
public class UserJPA implements Serializable {

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
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotBlank
    @Size(max = 100)
    @Column(name = "profile_name", nullable = false, unique = true, length = 100)
    private String profileName;

    @Size(max = 110)
    @Column(name = "profile_image", length = 110)
    private String profileImage;

    @NotBlank
    @Size(max = 300)
    @Column(name = "username", nullable = false, unique = true, length = 300)
    private String username;

    @NotBlank
    @Size(max = 300)
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 300)
    private String email;

    @NotBlank
    @Size(max = 300)
    @Column(name = "password", nullable = false, length = 300)
    private String password;

    @NotNull
    @Column(name = "role", nullable = false, length = 100)
    private UserRole role;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "country_id", nullable = false, referencedColumnName = "id")
    private CountryJPA country;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "user_media",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false))
    private List<MediaJPA> medias = new ArrayList<>();

    @OneToMany(targetEntity = CritiqueJPA.class, mappedBy = "id.critic", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<CritiqueJPA> critiques = new ArrayList<>();

    public UserJPA() {
    }

    public UserJPA(String firstName, String lastName, Gender gender, String profileName, String profileImage, String username, String email, String password, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt, CountryJPA country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.country = country;
    }

    public UserJPA(Long id, String firstName, String lastName, Gender gender, String profileName, String profileImage, String username, String email, String password, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt, CountryJPA country) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.country = country;
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

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
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

    public CountryJPA getCountry() {
        return country;
    }

    public void setCountry(CountryJPA country) {
        this.country = country;
    }

    public List<MediaJPA> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaJPA> medias) {
        this.medias = medias;
    }

    public List<CritiqueJPA> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<CritiqueJPA> critiques) {
        this.critiques = critiques;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final UserJPA other = (UserJPA) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + profileName + ")";
    }

}
