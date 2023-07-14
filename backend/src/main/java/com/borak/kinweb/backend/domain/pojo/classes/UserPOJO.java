/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.pojo.classes;

import com.borak.kinweb.backend.domain.enums.Gender;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class UserPOJO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String profileImageUrl;

    private String username;

    private String email;

    private String password;

    private CountryPOJO country;

    private List<MediaPOJO> library=new ArrayList<>();

    

}
