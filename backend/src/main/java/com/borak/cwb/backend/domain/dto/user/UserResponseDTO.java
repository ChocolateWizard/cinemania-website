/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.user;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.domain.dto.media.MediaResponseDTO;
import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "firstName",
    "lastName",
    "gender",
    "profileName",
    "profileImageUrl",
    "role",
    "country",
    "library",
    "critiques",
    "comments",
    "critiquesLikeDislikes",
    "commentsLikeDislikes"
})
public class UserResponseDTO implements DTO {

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    private Gender gender;

    @JsonProperty(value = "profile_name")
    private String profileName;

    @JsonProperty(value = "profile_image_url")
    private String profileImageUrl;

    private UserRole role;

    private UserCountryResponseDTO country;

    private UserLibraryResponseDTO library;

    private List<Long> critiques = new ArrayList<>();
    private List<Long> comments = new ArrayList<>();

    @JsonProperty(value = "critique_like_dislikes")
    private List<UserCritiqueLikeDislikesDTO> critiquesLikeDislikes = new ArrayList<>();

    @JsonProperty(value = "comment_like_dislikes")
    private List<UserCommentLikeDislikesDTO> commentsLikeDislikes = new ArrayList<>();

    public UserResponseDTO() {
    }

    public UserResponseDTO(String firstName, String lastName, Gender gender, String profileName, String profileImageUrl, UserRole role, UserCountryResponseDTO country, UserLibraryResponseDTO library) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.country = country;
        this.library = library;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserCountryResponseDTO getCountry() {
        return country;
    }

    public void setCountry(UserCountryResponseDTO country) {
        this.country = country;
    }

    public UserLibraryResponseDTO getLibrary() {
        return library;
    }

    public void setLibrary(UserLibraryResponseDTO library) {
        this.library = library;
    }

    public List<Long> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<Long> critiques) {
        this.critiques = critiques;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public List<UserCritiqueLikeDislikesDTO> getCritiquesLikeDislikes() {
        return critiquesLikeDislikes;
    }

    public void setCritiquesLikeDislikes(List<UserCritiqueLikeDislikesDTO> critiquesLikeDislikes) {
        this.critiquesLikeDislikes = critiquesLikeDislikes;
    }

    public List<UserCommentLikeDislikesDTO> getCommentsLikeDislikes() {
        return commentsLikeDislikes;
    }

    public void setCommentsLikeDislikes(List<UserCommentLikeDislikesDTO> commentsLikeDislikes) {
        this.commentsLikeDislikes = commentsLikeDislikes;
    }

}
