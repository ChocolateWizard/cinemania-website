/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import com.borak.kinweb.backend.exceptions.ResponseException;
import com.borak.kinweb.backend.logic.services.country.CountryService;
import com.borak.kinweb.backend.logic.services.country.ICountryService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */

//front controller should be slim and stupid. Not much going on here, just get request, call service and return
//response of service

//@CrossOrigin(origins = "http://localhost:8081")
//@Configuration
@RestController
@RequestMapping("api")
public class FrontController {
    
    @Autowired
    private ICountryService countryService;
    
    @GetMapping(path = "countries")
    public ResponseEntity getAllCountries() {     
            return countryService.getAllCountries();
    }
    
    
    
}
