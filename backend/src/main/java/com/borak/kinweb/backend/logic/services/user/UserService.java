/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.user;

import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.domain.security.SecurityUser;
import com.borak.kinweb.backend.exceptions.DuplicateResourceException;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.logic.transformers.UserTransformer;
import com.borak.kinweb.backend.repository.api.IMediaRepository;
import com.borak.kinweb.backend.repository.api.IUserRepository;
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
public class UserService implements IUserService<Long> {
    
    @Autowired
    private IUserRepository<UserJDBC, Long> userRepo;
    @Autowired
    private IMediaRepository<MediaJDBC, Long> mediaRepo;
    @Autowired
    private UserTransformer userTransformer;
    
    @Override
    public ResponseEntity postMediaIntoLibrary(Long mediaId) {
        if (!mediaRepo.existsById(mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserJDBC user = userTransformer.toUserJDBC(loggedUser, mediaId);
        if (userRepo.existsMediaInLibrary(user)) {
            throw new DuplicateResourceException("Duplicate user library entry! Media with id: " + mediaId + " already present!");
        }
        userRepo.addMediaToLibrary(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @Override
    public ResponseEntity deleteMediaFromLibrary(Long mediaId) {
        if (!mediaRepo.existsById(mediaId)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserJDBC user = userTransformer.toUserJDBC(loggedUser, mediaId);
        if (!userRepo.existsMediaInLibrary(user)) {
            throw new ResourceNotFoundException("Media with id: " + mediaId + " not present in users library!");
        }
        userRepo.removeMediaFromLibrary(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}
