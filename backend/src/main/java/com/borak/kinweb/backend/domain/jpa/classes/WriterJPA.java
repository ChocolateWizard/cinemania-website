/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 *
 * @author Mr. Poyo
 */
@Entity(name = "Writer")
@Table(name = "writer")
@PrimaryKeyJoinColumn(name = "person_id")
public class WriterJPA extends PersonJPA{
    
}
