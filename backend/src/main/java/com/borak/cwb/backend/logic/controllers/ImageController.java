/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.controllers;

import com.borak.cwb.backend.logic.services.image.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "images")
public class ImageController {

    @Autowired
    private IImageService imageService;

    @GetMapping("/media/{filename:.+}")
    public ResponseEntity getMediaImage(@PathVariable String filename) {
        return imageService.getMediaImage(filename);
    }

    @GetMapping("/person/{filename:.+}")
    public ResponseEntity getPersonImage(@PathVariable String filename) {
        return imageService.getPersonImage(filename);
    }

    @GetMapping("/user/{filename:.+}")
    public ResponseEntity getUserImage(@PathVariable String filename) {
        return imageService.getUserImage(filename);
    }

}
