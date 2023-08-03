

package com.borak.kinweb.backend.domain.jdbc.classes;

import java.util.List;



/**
 *
 * @author Mr. Poyo
 */
public class ActingJDBC implements JDBC{
    
    private MediaJDBC media;
    private ActorJDBC actor;
    private List<ActingRoleJDBC> roles;

    public MediaJDBC getMedia() {
        return media;
    }

    public void setMedia(MediaJDBC media) {
        this.media = media;
    }

    public ActorJDBC getActor() {
        return actor;
    }

    public void setActor(ActorJDBC actor) {
        this.actor = actor;
    }

    public List<ActingRoleJDBC> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRoleJDBC> roles) {
        this.roles = roles;
    }
    
    
    
    
    
}
