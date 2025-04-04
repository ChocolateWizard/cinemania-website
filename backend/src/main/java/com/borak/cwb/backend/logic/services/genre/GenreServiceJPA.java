/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.genre;

import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.logic.transformers.GenreTransformer;
import com.borak.cwb.backend.repository.jpa.GenreRepositoryJPA;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
@Service
@Transactional
public class GenreServiceJPA implements IGenreService {

    private final GenreRepositoryJPA genreRepo;
    private final GenreTransformer genreTransformer;

    @Autowired
    public GenreServiceJPA(GenreRepositoryJPA genreRepo, GenreTransformer genreTransformer) {
        this.genreRepo = genreRepo;
        this.genreTransformer = genreTransformer;
    }

//=================================================================================================================================  
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public ResponseEntity getAll() {
        List<GenreJPA> genres = genreRepo.findAll();
        return new ResponseEntity(genreTransformer.jpaToGenreResponse(genres), HttpStatus.OK);
    }

}
