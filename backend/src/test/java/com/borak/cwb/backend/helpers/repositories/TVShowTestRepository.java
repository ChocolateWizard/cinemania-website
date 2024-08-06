/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers.repositories;

import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.CommentJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import com.borak.cwb.backend.repository.jpa.TVShowRepositoryJPA;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional(readOnly = true)
public class TVShowTestRepository {

    private final TVShowRepositoryJPA repo;

    @Autowired
    public TVShowTestRepository(TVShowRepositoryJPA repo) {
        this.repo = repo;
    }

    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public Optional<TVShowJPA> findById(Long id) {
        Optional<TVShowJPA> show = repo.findById(id);
        //initialize lazy attributes
        if (show.isPresent()) {
            show.get().getGenres().size();
            show.get().getDirectors().size();
            show.get().getWriters().size();
            for (CritiqueJPA critique : show.get().getCritiques()) {
                critique.getLikeDislikes().size();
                for (CommentJPA comment : critique.getComments()) {
                    comment.getLikeDislikes().size();
                }
            }
            show.get().getActings().size();
            for (ActingJPA acting : show.get().getActings()) {
                acting.getRoles().size();
            }
        }
        return show;
    }

    public List<TVShowJPA> findAll() {
        List<TVShowJPA> shows = repo.findAll();
        //initialize lazy attributes
        for (TVShowJPA show : shows) {
            show.getGenres().size();
            show.getDirectors().size();
            show.getWriters().size();
            for (CritiqueJPA critique : show.getCritiques()) {
                critique.getLikeDislikes().size();
                for (CommentJPA comment : critique.getComments()) {
                    comment.getLikeDislikes().size();
                }
            }
            show.getActings().size();
            for (ActingJPA acting : show.getActings()) {
                acting.getRoles().size();
            }
        }
        return shows;
    }

}
