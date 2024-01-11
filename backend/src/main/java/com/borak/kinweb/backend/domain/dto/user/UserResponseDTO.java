/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.user;

import com.borak.kinweb.backend.domain.dto.DTO;
import com.borak.kinweb.backend.domain.dto.media.MediaResponseDTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class UserResponseDTO implements DTO {

    private String firstName;

    private String lastName;

    private Gender gender;

    private String profileName;

    private String profileImageUrl;

    private Country country;

    private List<MediaResponseDTO> medias = new ArrayList<>();

    private List<Critique> critiques = new ArrayList<>();

    public UserResponseDTO() {
    }

    public UserResponseDTO(String firstName, String lastName, Gender gender, String profileName, String profileImageUrl, Country country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
        this.country = country;
    }

    public static class Country {

        private Long id;

        private String name;

        private String officialStateName;

        private String code;

        public Country() {
        }

        public Country(Long id, String name, String officialStateName, String code) {
            this.id = id;
            this.name = name;
            this.officialStateName = officialStateName;
            this.code = code;
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

        public String getOfficialStateName() {
            return officialStateName;
        }

        public void setOfficialStateName(String officialStateName) {
            this.officialStateName = officialStateName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    public static class Critique {

        private MediaResponseDTO media;

        private String description;

        private Integer rating;

        public Critique() {
        }

        public Critique(MediaResponseDTO media, String description, Integer rating) {
            this.media = media;
            this.description = description;
            this.rating = rating;
        }

        public MediaResponseDTO getMedia() {
            return media;
        }

        public void setMedia(MediaResponseDTO media) {
            this.media = media;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<MediaResponseDTO> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaResponseDTO> medias) {
        if (medias == null) {
            this.medias = new ArrayList<>();
        } else {
            this.medias = medias;
        }
    }

    public List<Critique> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<Critique> critiques) {
        if (critiques == null) {
            this.critiques = new ArrayList<>();
        } else {
            this.critiques = critiques;
        }
    }

}
