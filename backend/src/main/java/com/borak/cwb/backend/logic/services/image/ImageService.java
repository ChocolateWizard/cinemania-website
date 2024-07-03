/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.image;

import com.borak.cwb.backend.domain.MyImage;
import com.borak.cwb.backend.exceptions.InvalidInputException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.repository.file.FileRepository;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class ImageService implements IImageService {

    @Autowired
    private FileRepository fileRepo;

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    @Override
    public ResponseEntity getMediaImage(String filename) {
        String[] pom;
        try {
            pom = MyImage.extractNameAndExtension(filename);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid image name!");
        }

        Resource file = fileRepo.getMediaCoverImage(filename);
        if (file.exists() && file.isReadable()) {
            try {
                return ResponseEntity.ok().contentType(MyImage.parseContentType(pom[1])).body(file.getContentAsByteArray());
            } catch (IOException ex) {
                log.error("Unable to read media image: " + filename);
            }
        }
        throw new ResourceNotFoundException("No such image found!");
    }

    @Override
    public ResponseEntity getPersonImage(String filename) {
        String[] pom;
        try {
            pom = MyImage.extractNameAndExtension(filename);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid image name!");
        }
        Resource file = fileRepo.getPersonProfilePhoto(filename);
        if (file.exists() && file.isReadable()) {
            try {
                return ResponseEntity.ok().contentType(MyImage.parseContentType(pom[1])).body(file.getContentAsByteArray());
            } catch (IOException ex) {
                log.error("Unable to read person image: " + filename);
            }
        }
        throw new ResourceNotFoundException("No such image found!");
    }

    @Override
    public ResponseEntity getUserImage(String filename) {
        String[] pom;
        try {
            pom = MyImage.extractNameAndExtension(filename);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid image name!");
        }
        Resource file = fileRepo.getUserProfileImage(filename);
        if (file.exists() && file.isReadable()) {
            try {
                return ResponseEntity.ok().contentType(MyImage.parseContentType(pom[1])).body(file.getContentAsByteArray());
            } catch (IOException ex) {
                log.error("Unable to read user image: " + filename);
            }
        }
        throw new ResourceNotFoundException("No such image found!");

    }

}
