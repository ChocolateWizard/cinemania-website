/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;



import com.borak.kinweb.backend.domain.enums.Gender;


/**
 *
 * @author Mr Poyo
 */
public class PersonDTO {

    protected Long id;

    protected String firstName;

    protected String lastName;

    protected Gender gender;

    protected String profilePhotoURL;

    protected boolean isStar;

}
