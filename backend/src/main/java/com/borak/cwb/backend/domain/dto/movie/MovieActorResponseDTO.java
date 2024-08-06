/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.movie;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({"id", "firstName", "lastName", "profilePhotoUrl", "gender", "star", "starring", "roles"})
public class MovieActorResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "profile_photo_url")
    private String profilePhotoUrl;

    @JsonView(JsonVisibilityViews.Lite.class)
    private Gender gender;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "is_star")
    private Boolean star;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "is_starring")
    private Boolean starring;

    @JsonView(JsonVisibilityViews.Lite.class)
    private List<MovieActorRoleResponseDTO> roles = new ArrayList<>();

    public MovieActorResponseDTO() {
    }

    public MovieActorResponseDTO(Long id, String firstName, String lastName, String profilePhotoUrl, Gender gender, Boolean star, Boolean starring) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.gender = gender;
        this.star = star;
        this.starring = starring;
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

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    public Boolean getStarring() {
        return starring;
    }

    public void setStarring(Boolean starring) {
        this.starring = starring;
    }

    public List<MovieActorRoleResponseDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<MovieActorRoleResponseDTO> roles) {
        this.roles = roles;
    }

}
