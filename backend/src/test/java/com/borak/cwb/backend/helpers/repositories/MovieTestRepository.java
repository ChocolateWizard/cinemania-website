/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers.repositories;

import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.CommentJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.repository.jpa.MovieRepositoryJPA;
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
public class MovieTestRepository {

    private final MovieRepositoryJPA repo;

    @Autowired
    public MovieTestRepository(MovieRepositoryJPA repo) {
        this.repo = repo;
    }

    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public Optional<MovieJPA> findById(Long id) {
        Optional<MovieJPA> movie = repo.findById(id);
        //initialize lazy attributes
        if (movie.isPresent()) {
            movie.get().getGenres().size();
            movie.get().getDirectors().size();
            movie.get().getWriters().size();
            for (CritiqueJPA critique : movie.get().getCritiques()) {
                critique.getLikeDislikes().size();
                for (CommentJPA comment : critique.getComments()) {
                    comment.getLikeDislikes().size();
                }
            }
            movie.get().getActings().size();
            for (ActingJPA acting : movie.get().getActings()) {
                acting.getRoles().size();
            }
        }
        return movie;
    }

    public List<MovieJPA> findAll() {
        List<MovieJPA> movies = repo.findAll();
        //initialize lazy attributes
        for (MovieJPA movie : movies) {
            movie.getGenres().size();
            movie.getDirectors().size();
            movie.getWriters().size();
            for (CritiqueJPA critique : movie.getCritiques()) {
                critique.getLikeDislikes().size();
                for (CommentJPA comment : critique.getComments()) {
                    comment.getLikeDislikes().size();
                }
            }

            movie.getActings().size();
            for (ActingJPA acting : movie.getActings()) {
                acting.getRoles().size();
            }
        }
        return movies;
    }

}
