/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder.domain.db;

import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
public class PersonWrapperDB {

    private PersonDB person;
    private DirectorDB director;
    private WriterDB writer;
    private ActorDB actor;

    public PersonWrapperDB() {
    }

    public PersonWrapperDB(PersonDB person, DirectorDB director, WriterDB writer, ActorDB actor) {
        this.person = person;
        this.director = director;
        this.writer = writer;
        this.actor = actor;
    }

    public void setId(Long id) {
        person.setId(id);
        if (director != null) {
            director.setId(id);
        }
        if (writer != null) {
            writer.setId(id);
        }
        if (actor != null) {
            actor.setId(id);
        }
    }

    public PersonDB getPerson() {
        return person;
    }

    public void setPerson(PersonDB person) {
        this.person = person;
    }

    public DirectorDB getDirector() {
        return director;
    }

    public void setDirector(DirectorDB director) {
        this.director = director;
    }

    public WriterDB getWriter() {
        return writer;
    }

    public void setWriter(WriterDB writer) {
        this.writer = writer;
    }

    public ActorDB getActor() {
        return actor;
    }

    public void setActor(ActorDB actor) {
        this.actor = actor;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.person);
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
        if (obj instanceof PersonDB personDB) {
            return personDB.getId().equals(this.getPerson().getId());
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonWrapperDB other = (PersonWrapperDB) obj;
        return Objects.equals(this.person, other.person);
    }

}
