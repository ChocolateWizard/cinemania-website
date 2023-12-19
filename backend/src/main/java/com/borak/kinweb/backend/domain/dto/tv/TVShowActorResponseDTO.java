/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.tv;

import com.borak.kinweb.backend.domain.dto.DTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
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
public class TVShowActorResponseDTO implements DTO {

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

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonProperty(value = "is_starring")
    private Boolean starring;

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<Role> roles = new ArrayList<>();

    public TVShowActorResponseDTO() {
    }

    public TVShowActorResponseDTO(Long id, String firstName, String lastName, String profilePhotoUrl, Gender gender, Boolean star, Boolean starring) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.gender = gender;
        this.star = star;
        this.starring = starring;
    }

    @JsonPropertyOrder({"id", "name"})
    @JsonView(JsonVisibilityViews.Heavy.class)
    public static class Role {

        private Long id;

        private String name;

        public Role() {
        }

        public Role(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        } else {
            this.roles = roles;
        }
    }

}
