/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.pojo.classes;

import java.io.Serializable;

/**
 *
 * @author Mr Poyo
 */
public class CountryPOJO implements POJO{

    
    private Long id;

   
    private String name;

   
    private String officialStateName;

    
    private String code;

    public CountryPOJO() {
    }

    public CountryPOJO(String name, String officialStateName, String code) {
        this.name = name;
        this.officialStateName = officialStateName;
        this.code = code;
    }   
    

    public CountryPOJO(Long id, String name, String officialStateName, String code) {
        this.id = id;
        this.name = name;
        this.officialStateName = officialStateName;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialStateName() {
        return officialStateName;
    }

    public void setOfficialStateName(String officialStateName) {
        this.officialStateName = officialStateName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name + " (" + code + ')';
    }

    
    

}
