/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.critique;

import com.borak.cwb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.cwb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.security.SecurityUser;
import com.borak.cwb.backend.exceptions.DuplicateResourceException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.CritiqueTransformer;
import com.borak.cwb.backend.repository.api.ICritiqueRepository;
import com.borak.cwb.backend.repository.api.IMediaRepository;
import com.borak.cwb.backend.repository.jpa.CritiqueRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
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
public class CritiqueServiceJPA implements ICritiqueService<CritiqueRequestDTO, Long> {

    @Autowired
    private CritiqueRepositoryJPA critiqueRepo;
    @Autowired
    private MediaRepositoryJPA mediaRepo;

    @Autowired
    private CritiqueTransformer critiqueTransformer;

    @Override
    public ResponseEntity postCritique(CritiqueRequestDTO critiqueRequest) {
        if (!mediaRepo.existsById(critiqueRequest.getMediaId())) {
            throw new ResourceNotFoundException("Media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJPA critique = critiqueTransformer.toCritiqueJPA(critiqueRequest, loggedUser.getId());
        if (critiqueRepo.existsById(critique.getId())) {
            throw new DuplicateResourceException("Duplicate critique for media with id: " + critiqueRequest.getMediaId());
        }
        critiqueRepo.save(critique);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

    @Override
    public ResponseEntity putCritique(CritiqueRequestDTO critiqueRequest) {
        if (!mediaRepo.existsById(critiqueRequest.getMediaId())) {
            throw new ResourceNotFoundException("Media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJPA critique = critiqueTransformer.toCritiqueJPA(critiqueRequest, loggedUser.getId());
        if (!critiqueRepo.existsById(critique.getId())) {
            throw new ResourceNotFoundException("Users critique for media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        critiqueRepo.save(critique);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

    @Override
    public ResponseEntity deleteCritique(Long id) {
        if (!mediaRepo.existsById(id)) {
            throw new ResourceNotFoundException("Media with id: " + id + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJPA critique = critiqueTransformer.toCritiqueJPA(id, loggedUser.getId());
        if (!critiqueRepo.existsById(critique.getId())) {
            throw new ResourceNotFoundException("Users critique for media with id: " + id + " does not exist in database!");
        }
        critiqueRepo.delete(critique);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

}
