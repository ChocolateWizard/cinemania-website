/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.user;

import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.exceptions.DuplicateResourceException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.repository.api.IMediaRepository;
import com.borak.cwb.backend.repository.api.IUserRepository;
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
//@Service
//@Transactional
public class UserServiceJDBC implements IUserService<Long> {

    @Autowired
    private IUserRepository<UserJDBC, Long, MediaJDBC, Long> userRepo;
    @Autowired
    private IMediaRepository<MediaJDBC, Long> mediaRepo;

    @Override
    public ResponseEntity postMediaIntoLibrary(Long mediaId) {
        if (!mediaRepo.existsById(mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRepo.existsMediaInLibrary(loggedUser.getId(), mediaId)) {
            throw new DuplicateResourceException("Duplicate user library entry! Media with id: " + mediaId + " already present!");
        }
        userRepo.addMediaToLibrary(loggedUser.getId(), mediaId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity deleteMediaFromLibrary(Long mediaId) {
        if (!mediaRepo.existsById(mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userRepo.existsMediaInLibrary(loggedUser.getId(), mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " not present in users library!");
        }
        userRepo.removeMediaFromLibrary(loggedUser.getId(), mediaId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
