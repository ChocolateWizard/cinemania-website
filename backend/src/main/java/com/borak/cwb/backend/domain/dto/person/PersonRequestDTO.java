/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.person;

import com.borak.cwb.backend.domain.classes.MyImage;
import com.borak.cwb.backend.domain.dto.DTO;
import com.borak.cwb.backend.domain.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class PersonRequestDTO implements DTO {

    @JsonIgnore
    private Long id;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    private Gender gender;

    @JsonIgnore
    private MyImage profilePhoto;

    private List<Profession> professions = new ArrayList<>();

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "name")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Director.class, name = "director"),
        @JsonSubTypes.Type(value = Writer.class, name = "writer"),
        @JsonSubTypes.Type(value = Actor.class, name = "actor")
    })
    public static abstract class Profession {

        private String name;

        public Profession(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Director extends Profession {

        @JsonProperty(value = "worked_on")
        private List<Long> workedOn = new ArrayList<>();

        public Director() {
            super("director");
        }

        public Director(List<Long> workedOn) {
            super("director");
            this.workedOn = workedOn;
        }

        public Director(String name, List<Long> workedOn) {
            super(name);
            this.workedOn = workedOn;
        }

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

    public static class Writer extends Profession {

        @JsonProperty(value = "worked_on")
        private List<Long> workedOn = new ArrayList<>();

        public Writer() {
            super("writer");
        }

        public Writer(List<Long> workedOn) {
            super("writer");
            this.workedOn = workedOn;
        }

        public Writer(String name, List<Long> workedOn) {
            super(name);
            this.workedOn = workedOn;
        }

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

    public static class Actor extends Profession {

        @JsonProperty(value = "is_star")
        private Boolean star;

        @JsonProperty(value = "worked_on")
        private List<Acting> workedOn = new ArrayList<>();

        public Actor() {
            super("actor");
        }

        public Actor(Boolean star) {
            super("actor");
            this.star = star;
        }

        public Actor(Boolean star, List<Acting> workedOn) {
            super("actor");
            this.star = star;
            this.workedOn = workedOn;
        }

        public Actor(String name, Boolean star, List<Acting> workedOn) {
            super(name);
            this.star = star;
            this.workedOn = workedOn;
        }

        public static class Acting {

            @JsonProperty(value = "media_id")
            private Long mediaId;
            @JsonProperty(value = "is_starring")
            private Boolean starring;
            private List<String> roles = new ArrayList<>();

            public Acting() {
            }

            public Acting(Long mediaId, Boolean starring, List<String> roles) {
                this.mediaId = mediaId;
                this.starring = starring;
                this.roles = roles;
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

            public List<String> getRoles() {
                return roles;
            }

            public void setRoles(List<String> roles) {
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

    public MyImage getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(MyImage profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<Profession> getProfessions() {
        return professions;
    }

    public void setProfessions(List<Profession> professions) {
        if (professions == null) {
            this.professions = new ArrayList<>();
        } else {
            this.professions = professions;
        }

    }

}
