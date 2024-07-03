/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.controllers;

import com.borak.cwb.backend.logic.services.genre.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
@RequestMapping(path = "api/genres")
public class GenreController {
    
    @Autowired
    private IGenreService genreService;

    //=========================GET MAPPINGS==================================  
    @GetMapping
    public ResponseEntity getGenres() {
        return genreService.getAll();
    }
    
}
