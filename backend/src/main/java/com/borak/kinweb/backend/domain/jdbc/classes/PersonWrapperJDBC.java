/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jdbc.classes;

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

}
