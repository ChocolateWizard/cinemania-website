/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


/**
 *
 * @author Mr Poyo
 */
@Entity(name = "Movie")
@Table(name = "movie")
@PrimaryKeyJoinColumn(name = "media_id")
public class MovieJPA extends MediaJPA{

    
    @Column(name = "length",nullable = false)
    private Integer length;



    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    
  

}
