/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.exceptions.InvalidInputException;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.repository.util.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
    private FileRepository fileRepo;

    @GetMapping("/media/{filename:.+}")
    public ResponseEntity getMediaImage(@PathVariable String filename) {
        String[] pom;
        try {
            pom = MyImage.extractNameAndExtension(filename);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid image name!");
        }
        Resource file = fileRepo.getMediaCoverImage(filename);
        if (file.exists() || file.isReadable()) {
            return ResponseEntity.ok().contentType(MyImage.parseContentType(pom[1])).body(file);
        } else {
            throw new ResourceNotFoundException("No such image found!");
        }
    }

    @GetMapping("/person/{filename:.+}")
    public ResponseEntity getPersonImage(@PathVariable String filename) {
        String[] pom;
        try {
            pom = MyImage.extractNameAndExtension(filename);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid image name!");
        }
        Resource file = fileRepo.getPersonProfilePhoto(filename);
        if (file.exists() || file.isReadable()) {
            return ResponseEntity.ok().contentType(MyImage.parseContentType(pom[1])).body(file);
        } else {
            throw new ResourceNotFoundException("No such image found!");
        }
    }

    @GetMapping("/user/{filename:.+}")
    public ResponseEntity getUserImage(@PathVariable String filename) {
        String[] pom;
        try {
            pom = MyImage.extractNameAndExtension(filename);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid image name!");
        }
        Resource file = fileRepo.getUserProfileImage(filename);
        if (file.exists() || file.isReadable()) {
            return ResponseEntity.ok().contentType(MyImage.parseContentType(pom[1])).body(file);
        } else {
            throw new ResourceNotFoundException("No such image found!");
        }
    }

}
