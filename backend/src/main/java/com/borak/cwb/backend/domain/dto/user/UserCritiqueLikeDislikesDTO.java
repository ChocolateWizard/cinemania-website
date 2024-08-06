/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.user;

import com.borak.cwb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "critiqueId",
    "like"
})
public class UserCritiqueLikeDislikesDTO implements DTO {

    @JsonProperty(value = "critique_id")
    private Long critiqueId;
    private Boolean like;

    public UserCritiqueLikeDislikesDTO() {
    }

    public UserCritiqueLikeDislikesDTO(Long critiqueId, Boolean like) {
        this.critiqueId = critiqueId;
        this.like = like;
    }

    public Long getCritiqueId() {
        return critiqueId;
    }

    public void setCritiqueId(Long critiqueId) {
        this.critiqueId = critiqueId;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

}
