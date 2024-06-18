/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.user;

import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.domain.security.SecurityUser;
import com.borak.cwb.backend.exceptions.DuplicateResourceException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.UserRepositoryJPA;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class UserServiceJPA implements IUserService<Long> {

    @Autowired
    private UserRepositoryJPA userRepo;
    @Autowired
    private MediaRepositoryJPA mediaRepo;

    @Override
    public ResponseEntity postMediaIntoLibrary(Long mediaId) {
        if (!mediaRepo.existsById(mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserJPA> userDB = userRepo.findByUsername(loggedUser.getUsername());
        MediaJPA media = new MediaJPA(mediaId);
        if (userDB.get().getMedias().contains(media)) {
            throw new DuplicateResourceException("Duplicate user library entry! Media with id: " + mediaId + " already present!");
        }
        userDB.get().getMedias().add(media);
        userRepo.save(userDB.get());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity deleteMediaFromLibrary(Long mediaId) {
        if (!mediaRepo.existsById(mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserJPA> userDB = userRepo.findByUsername(loggedUser.getUsername());
        MediaJPA media = new MediaJPA(mediaId);
        if (!userDB.get().getMedias().contains(media)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " not present in users library!");
        }
        userDB.get().getMedias().remove(media);
        userRepo.save(userDB.get());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
