/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jdbc.classes;

import java.util.Objects;

/**
 *
 * @author Mr. Poyo
 */
public class PersonWrapperJDBC implements JDBC {

    private PersonJDBC person;
    private DirectorJDBC director;
    private WriterJDBC writer;
    private ActorJDBC actor;

    public PersonWrapperJDBC() {
    }

    public PersonWrapperJDBC(PersonJDBC person, DirectorJDBC director, WriterJDBC writer, ActorJDBC actor) {
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

    public PersonJDBC getPerson() {
        return person;
    }

    public void setPerson(PersonJDBC person) {
        this.person = person;
    }

    public DirectorJDBC getDirector() {
        return director;
    }

    public void setDirector(DirectorJDBC director) {
        this.director = director;
    }

    public WriterJDBC getWriter() {
        return writer;
    }

    public void setWriter(WriterJDBC writer) {
        this.writer = writer;
    }

    public ActorJDBC getActor() {
        return actor;
    }

    public void setActor(ActorJDBC actor) {
        this.actor = actor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.person);
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
        if (obj instanceof PersonJDBC personJDBC) {
            return personJDBC.getId().equals(this.getPerson().getId());
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonWrapperJDBC other = (PersonWrapperJDBC) obj;
        return Objects.equals(this.person, other.person);
    }

}
