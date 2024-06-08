package com.borak.kinweb.backend.domain.jdbc.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
public class ActingJDBC implements JDBC {

    private MediaJDBC media;
    private ActorJDBC actor;
    private Boolean starring;
    private List<ActingRoleJDBC> roles = new ArrayList<>();

    public ActingJDBC() {
    }

    public ActingJDBC(MediaJDBC media, Boolean starring) {
        this.media = media;
        this.starring = starring;
    }

    public ActingJDBC(MediaJDBC media, ActorJDBC actor, Boolean starring) {
        this.media = media;
        this.actor = actor;
        this.starring = starring;
    }

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

    public Boolean isStarring() {
        return starring;
    }

    public void setStarring(Boolean starring) {
        this.starring = starring;
    }

    public List<ActingRoleJDBC> getRoles() {
        return roles;
    }

    public void setRoles(List<ActingRoleJDBC> roles) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        } else {
            this.roles = roles;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.media);
        hash = 37 * hash + Objects.hashCode(this.actor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActingJDBC other = (ActingJDBC) obj;
        if (!Objects.equals(this.media, other.media)) {
            return false;
        }
        return Objects.equals(this.actor, other.actor);
    }

}
