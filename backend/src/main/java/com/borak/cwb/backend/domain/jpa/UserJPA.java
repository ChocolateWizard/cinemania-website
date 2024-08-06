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

    @NotBlank(message = "First name must not be empty")
    @Size(max = 100, message = "First name must be less than or equal to 100 characters")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Size(max = 100, message = "Last name must be less than or equal to 100 characters")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull(message = "Gender must not be null")
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotBlank(message = "Profile name must not be empty")
    @Size(max = 100, message = "Profile name must be less than or equal to 100 characters")
    @Column(name = "profile_name", nullable = false, unique = true, length = 100)
    private String profileName;

    @Size(max = 110, message = "Profile image must be less than or equal to 110 characters")
    @Column(name = "profile_image", length = 110)
    private String profileImage;

    @NotBlank(message = "Username must not be empty")
    @Size(max = 300, message = "Username must be less than or equal to 300 characters")
    @Column(name = "username", nullable = false, unique = true, length = 300)
    private String username;

    @NotBlank(message = "Email must not be empty")
    @Size(max = 300, message = "Email must be less than or equal to 300 characters")
    @Email(message = "Email must be of valid email structure")
    @Column(name = "email", nullable = false, unique = true, length = 300)
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(max = 300, message = "Password must be less than or equal to 300 characters")
    @Column(name = "password", nullable = false, length = 300)
    private String password;

    @NotNull(message = "Role must not be null")
    @Column(name = "role", nullable = false, length = 30)
    private UserRole role;

    @NotNull(message = "Created at must not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "country_id", nullable = false, referencedColumnName = "id")
    private CountryJPA country;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "user_media",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id", nullable = false))
    private List<MediaJPA> medias = new ArrayList<>();

    @OneToMany(targetEntity = CritiqueJPA.class, mappedBy = "user", fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CritiqueJPA> critiques = new ArrayList<>();

    @OneToMany(targetEntity = CommentJPA.class, mappedBy = "user", fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CommentJPA> comments = new ArrayList<>();

    @OneToMany(targetEntity = CritiqueLikeDislikeJPA.class, mappedBy = "id.user",
            fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CritiqueLikeDislikeJPA> critiqueLikeDislikes = new ArrayList<>();

    @OneToMany(targetEntity = CommentLikeDislikeJPA.class, mappedBy = "id.user",
            fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CommentLikeDislikeJPA> commentsLikeDislikes = new ArrayList<>();

    public UserJPA() {
    }

    public UserJPA(Long id) {
        this.id = id;
    }

    public UserJPA(Long id, String profileName, String profileImage) {
        this.id = id;
        this.profileName = profileName;
        this.profileImage = profileImage;
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

    public List<CommentJPA> getComments() {
        return comments;
    }

    public void setComments(List<CommentJPA> comments) {
        this.comments = comments;
    }

    public List<CritiqueLikeDislikeJPA> getCritiqueLikeDislikes() {
        return critiqueLikeDislikes;
    }

    public void setCritiqueLikeDislikes(List<CritiqueLikeDislikeJPA> critiqueLikeDislikes) {
        this.critiqueLikeDislikes = critiqueLikeDislikes;
    }

    public List<CommentLikeDislikeJPA> getCommentsLikeDislikes() {
        return commentsLikeDislikes;
    }

    public void setCommentsLikeDislikes(List<CommentLikeDislikeJPA> commentsLikeDislikes) {
        this.commentsLikeDislikes = commentsLikeDislikes;
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
