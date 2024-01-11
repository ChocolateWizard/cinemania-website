/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.critique;

import com.borak.kinweb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.security.SecurityUser;
import com.borak.kinweb.backend.exceptions.DuplicateResourceException;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.logic.transformers.CritiqueTransformer;
import com.borak.kinweb.backend.repository.api.ICritiqueRepository;
import com.borak.kinweb.backend.repository.api.IMediaRepository;
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
public class CritiqueService implements ICritiqueService<CritiqueRequestDTO, Long> {

    @Autowired
    private ICritiqueRepository<CritiqueJDBC, Long> critiqueRepo;
    @Autowired
    private IMediaRepository<MediaJDBC, Long> mediaRepo;

    @Autowired
    private CritiqueTransformer critiqueTransformer;

    @Override
    public ResponseEntity postCritique(CritiqueRequestDTO critiqueRequest) {
        if (!mediaRepo.existsById(critiqueRequest.getMediaId())) {
            throw new ResourceNotFoundException("Media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJDBC critique = critiqueTransformer.toCritiqueJDBC(critiqueRequest, loggedUser.getId());
        if (critiqueRepo.exists(critique)) {
            throw new DuplicateResourceException("Duplicate critique for media with id: " + critiqueRequest.getMediaId());
        }
        critiqueRepo.insert(critique);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

    @Override
    public ResponseEntity putCritique(CritiqueRequestDTO critiqueRequest) {
        if (!mediaRepo.existsById(critiqueRequest.getMediaId())) {
            throw new ResourceNotFoundException("Media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJDBC critique = critiqueTransformer.toCritiqueJDBC(critiqueRequest, loggedUser.getId());
        if (!critiqueRepo.exists(critique)) {
            throw new ResourceNotFoundException("Users critique for media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        critiqueRepo.update(critique);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

    @Override
    public ResponseEntity deleteCritique(Long id) {
        if (!mediaRepo.existsById(id)) {
            throw new ResourceNotFoundException("Media with id: " + id + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJDBC critique = critiqueTransformer.toCritiqueJDBC(id, loggedUser.getId());
        if (!critiqueRepo.exists(critique)) {
            throw new ResourceNotFoundException("Users critique for media with id: " + id + " does not exist in database!");
        }
        critiqueRepo.delete(critique);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

}
