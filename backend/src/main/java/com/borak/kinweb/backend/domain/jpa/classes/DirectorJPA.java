/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Director")
@Table(name = "director")
@PrimaryKeyJoinColumn(name = "person_id", referencedColumnName = "id")
public class DirectorJPA extends PersonJPA {

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
        final DirectorJPA other = (DirectorJPA) obj;
        return Objects.equals(this.id, other.id);
    }

}
