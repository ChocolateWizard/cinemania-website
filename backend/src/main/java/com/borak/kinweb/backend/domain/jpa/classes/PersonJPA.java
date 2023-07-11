/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.classes;



import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jpa.converters.GenderConverter;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author Mr Poyo
 */
@Entity(name = "Person")
@Table(name = "person")
@Access(AccessType.FIELD)
public class PersonJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(name = "first_name",length = 255,nullable = false)
    protected String firstName;
    
    @Column(name = "last_name",length = 255,nullable = false)
    protected String lastName;
    
    @Column(name = "gender")
    @Convert(converter = GenderConverter.class)
    protected Gender gender;
    
    
    @Column(name = "profile_photo_url",length = 255,nullable = true)
    protected String profilePhotoURL;   
    
    @Column(name = "is_star",nullable = false)
    protected boolean isStar;
    
    

}
