/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
//controller should be slim and stupid. Not much going on here, just get request, call service and return
//response of service
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping(path = "countries")
public class CountryController {

    //=========================GET MAPPINGS==================================
    //Returns: all countries
    //Included: 
    //Excluded:
    @GetMapping
    public List<CountryDTO> getAllCountries() {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: specific country with given id
    //Included:
    //Excluded:
    @GetMapping(path = "/{id}")
    public List<CountryDTO> getCountry(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //=========================POST MAPPINGS==================================
    //=========================PUT MAPPINGS===================================
    //=========================DELETE MAPPINGS================================
}
