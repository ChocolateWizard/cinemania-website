

package com.borak.kinweb.backend.domain.pojo.classes;

import java.util.List;



/**
 *
 * @author Mr. Poyo
 */
public class ActingPOJO implements POJO{
    
    private MediaPOJO media;
    private ActorPOJO actor;
    private List<ActingRolePOJO> roles;

    public MediaPOJO getMedia() {
        return media;
    }

    public void setMedia(MediaPOJO media) {
        this.media = media;
    }

    public ActorPOJO getActor() {
        return actor;
    }

    public void setActor(ActorPOJO actor) {
        this.actor = actor;
    }

    public List<ActingRolePOJO> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRolePOJO> roles) {
        this.roles = roles;
    }
    
    
    
    
    
}
