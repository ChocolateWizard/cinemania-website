/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "ActingRole")
@Table(name = "acting_role")
@Access(AccessType.FIELD)
public class ActingRoleJPA implements Serializable {

    @EmbeddedId
    private ActingRoleJPA.ID id;

    @NotBlank
    @Size(max = 300)
    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Embeddable
    public static class ID implements Serializable {

        @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
        @JoinColumn(name = "acting_id", referencedColumnName = "id", nullable = false)
        private ActingJPA acting;

        @NotNull
        @Min(value = 1)
        @Column(name = "id", nullable = false)
        private Long id;

        public ID() {
        }

        public ID(ActingJPA acting, Long id) {
            this.acting = acting;
            this.id = id;
        }

        public ActingJPA getActing() {
            return acting;
        }

        public void setActing(ActingJPA acting) {
            this.acting = acting;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.acting);
            hash = 17 * hash + Objects.hashCode(this.id);
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
            final ID other = (ID) obj;
            if (!Objects.equals(this.acting, other.acting)) {
                return false;
            }
            return Objects.equals(this.id, other.id);
        }

    }

    public ActingRoleJPA() {
    }

    public ActingRoleJPA(ID id, String name) {
        this.id = id;
        this.name = name;
    }

    public ActingRoleJPA.ID getId() {
        return id;
    }

    public void setId(ActingRoleJPA.ID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final ActingRoleJPA other = (ActingRoleJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
