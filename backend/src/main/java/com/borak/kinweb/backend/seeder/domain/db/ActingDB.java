package com.borak.kinweb.backend.seeder.domain.db;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class ActingDB {
   
    private MediaDB media;
    private ActorDB actor;
    private Boolean starring;
    private List<ActingRoleDB> roles = new ArrayList<>();

    public ActingDB() {
    }

    public ActingDB(MediaDB media, Boolean starring) {
        this.media = media;
        this.starring = starring;
    }

    public ActingDB(MediaDB media, ActorDB actor, Boolean starring) {
        this.media = media;
        this.actor = actor;
        this.starring = starring;
    }   

    public MediaDB getMedia() {
        return media;
    }

    public void setMedia(MediaDB media) {
        this.media = media;
    }

    public ActorDB getActor() {
        return actor;
    }

    public void setActor(ActorDB actor) {
        this.actor = actor;
    }

    public Boolean isStarring() {
        return starring;
    }

    public void setStarring(Boolean starring) {
        this.starring = starring;
    }

    public List<ActingRoleDB> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRoleDB> roles) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        } else {
            this.roles = roles;
        }
    }

}
