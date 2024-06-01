/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.user;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.DTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.domain.jdbc.classes.CountryJDBC;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 *
 * @author Mr. Poyo
 */
public class UserRegisterDTO implements DTO {

    @NotBlank(message = "First name must not be null or empty!")
    @Size(max = 100, message = "First name must have less than 100 characters!")
    @JsonProperty(value = "first_name")
    private String firstName;

    @NotBlank(message = "Last name must not be null or empty!")
    @Size(max = 100, message = "Last name must have less than 100 characters!")
    @JsonProperty(value = "last_name")
    private String lastName;

    @NotNull
    private Gender gender;

    @NotBlank(message = "Profile name must not be null or empty!")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Profile name must only contain letters, numbers and _")
    @Size(max = 100, message = "Profile name must have less than 100 characters!")
    @JsonProperty(value = "profile_name")
    private String profileName;

    @JsonIgnore
    private MyImage profileImage;

    @NotBlank(message = "Username is mandatory!")
    @Size(max = 300, message = "Invalid username!")
    private String username;

    @NotBlank(message = "Email is mandatory!")
    @Size(max = 300, message = "Email must have less than 300 characters!")
    @Email(message = "Invalid email structure!")
    private String email;

    @NotBlank(message = "Password is mandatory!")
    @Size(max = 300, message = "Invalid password!")
    private String password;

    @NotNull
    private UserRole role = UserRole.REGULAR;

    @NotNull(message = "Country id must not be null!")
    @Min(value = 1, message = "Country id must be greater than or equal to 1!")
    @JsonProperty(value = "country_id")
    private Long countryId;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String firstName, String lastName, Gender gender, String profileName, MyImage profileImage, String username, String email, String password, Long countryId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.username = username;
        this.email = email;
        this.password = password;
        this.countryId = countryId;
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

    public MyImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MyImage profileImage) {
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

}
