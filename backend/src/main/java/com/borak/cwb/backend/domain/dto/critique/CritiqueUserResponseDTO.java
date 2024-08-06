/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.critique;

import com.borak.cwb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "profileName",
    "profileImageUrl"})
public class CritiqueUserResponseDTO implements DTO {

    @JsonProperty(value = "profile_name")
    private String profileName;
    @JsonProperty(value = "profile_image_url")
    private String profileImageUrl;

    public CritiqueUserResponseDTO() {
    }

    public CritiqueUserResponseDTO(String profileName, String profileImageUrl) {
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
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

}
