/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.logic.transformers.serializers.acting.ActingJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.media.MediaDirectorsJsonSerializer;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonSerialize(using = ActingJsonSerializer.class)
public class ActingDTO implements DTO {

    private MediaDTO media;
    private ActorDTO actor;
    private Boolean starring;
    private List<ActingRoleDTO> roles = new ArrayList<>();

    public ActingDTO() {
    }

    public MediaDTO getMedia() {
        return media;
    }

    public void setMedia(MediaDTO media) {
        this.media = media;
    }

    public ActorDTO getActor() {
        return actor;
    }

    public void setActor(ActorDTO actor) {
        this.actor = actor;
    }

    public List<ActingRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRoleDTO> roles) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        } else {
            this.roles = roles;
        }
    }

    public Boolean isStarring() {
        return starring;
    }

    public void setStarring(Boolean starring) {
        this.starring = starring;
    }

}
