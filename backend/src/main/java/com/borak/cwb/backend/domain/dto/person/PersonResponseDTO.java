/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.person;

import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "id",
    "firstName",
    "lastName",
    "gender",
    "profilePhotoUrl",
    "professions"
})
public class PersonResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonView(JsonVisibilityViews.Lite.class)
    private Gender gender;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "profile_photo_url")
    private String profilePhotoUrl;

    @JsonView(JsonVisibilityViews.Medium.class)
    private List<Profession> professions = new ArrayList<>();

    public PersonResponseDTO() {
    }

    public PersonResponseDTO(Long id, String firstName, String lastName, Gender gender, String profilePhotoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "name")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Director.class, name = "director"),
        @JsonSubTypes.Type(value = Writer.class, name = "writer"),
        @JsonSubTypes.Type(value = Actor.class, name = "actor")
    })
    public static abstract class Profession {
    }

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonPropertyOrder({
        "name",
        "workedOn"
    })
    public static class Director extends Profession {

        @JsonView(JsonVisibilityViews.Heavy.class)
        @JsonProperty(value = "worked_on")
        private List<Long> workedOn = new ArrayList<>();

        public List<Long> getWorkedOn() {
            return workedOn;
        }

        public void setWorkedOn(List<Long> workedOn) {
            if (workedOn == null) {
                this.workedOn = new ArrayList<>();
            } else {
                this.workedOn = workedOn;
            }
        }
    }

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonPropertyOrder({
        "name",
        "workedOn"
    })
    public static class Writer extends Profession {

        @JsonView(JsonVisibilityViews.Heavy.class)
        @JsonProperty(value = "worked_on")
        private List<Long> workedOn = new ArrayList<>();

        public List<Long> getWorkedOn() {
            return workedOn;
        }

        public void setWorkedOn(List<Long> workedOn) {
            if (workedOn == null) {
                this.workedOn = new ArrayList<>();
            } else {
                this.workedOn = workedOn;
            }
        }

    }

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonPropertyOrder({
        "name",
        "star",
        "workedOn"
    })
    public static class Actor extends Profession {

        @JsonProperty(value = "is_star")
        private Boolean star;

        @JsonView(JsonVisibilityViews.Heavy.class)
        @JsonProperty(value = "worked_on")
        private List<Acting> workedOn = new ArrayList<>();

        public Actor() {
        }

        public Actor(Boolean star) {
            this.star = star;
        }

        @JsonView(JsonVisibilityViews.Heavy.class)
        @JsonPropertyOrder({
            "mediaId",
            "starring",
            "roles"
        })
        public static class Acting {

            @JsonProperty(value = "media_id")
            private Long mediaId;

            @JsonProperty(value = "is_starring")
            private Boolean starring;

            private List<Role> roles = new ArrayList<>();

            public Acting() {
            }

            public Acting(Long mediaId, Boolean starring) {
                this.mediaId = mediaId;
                this.starring = starring;
            }

            @JsonView(JsonVisibilityViews.Heavy.class)
            @JsonPropertyOrder({
                "id",
                "name",})
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

            public Long getMediaId() {
                return mediaId;
            }

            public void setMediaId(Long mediaId) {
                this.mediaId = mediaId;
            }

            public Boolean isStarring() {
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

        public Boolean isStar() {
            return star;
        }

        public void setStar(Boolean star) {
            this.star = star;
        }

        public List<Acting> getWorkedOn() {
            return workedOn;
        }

        public void setWorkedOn(List<Acting> workedOn) {
            if (workedOn == null) {
                this.workedOn = new ArrayList<>();
            } else {
                this.workedOn = workedOn;
            }
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public List<Profession> getProfessions() {
        return professions;
    }

    public void setProfessions(List<Profession> professions) {
        this.professions = professions;
    }

}
