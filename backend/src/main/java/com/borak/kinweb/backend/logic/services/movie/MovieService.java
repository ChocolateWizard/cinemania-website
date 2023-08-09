/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActorDTO;
import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.logic.transformers.ActorTransformer;
import com.borak.kinweb.backend.logic.transformers.DirectorTransformer;
import com.borak.kinweb.backend.logic.transformers.MovieTransformer;
import com.borak.kinweb.backend.logic.transformers.WriterTransformer;
import com.borak.kinweb.backend.repository.api.IMovieRepository;
import java.util.ArrayList;

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
@Transactional
public class MovieService implements IMovieService {

    @Autowired
    private IMovieRepository<MovieJDBC, Long> movieRepo;

    @Autowired
    private MovieTransformer mvTransformer;
    @Autowired
    private DirectorTransformer drTransformer;
    @Autowired
    private WriterTransformer wrTransformer;
    @Autowired
    private ActorTransformer acTranformer;
//----------------------------------------------------------------------------------------------------

    @Override
    public List<MovieDTO> getAllMoviesWithGenres() {
        return mvTransformer.jdbcToDto(movieRepo.findAllRelationshipGenres());
    }

    @Override
    public List<MovieDTO> getAllMoviesWithDetails() {
        return mvTransformer.jdbcToDto(movieRepo.findAll());
    }

    @Override
    public MovieDTO getMovieWithGenres(Long id) {
        Optional<MovieJDBC> movieDB = movieRepo.findByIdNoRelationships(id);
        if (movieDB.isPresent()) {
            MovieJDBC m = movieRepo.findByIdGenres(id);
            movieDB.get().setGenres(m.getGenres());
            return mvTransformer.jdbcToDto(movieDB.get());
        }
        return null;
    }

    @Override
    public MovieDTO getMovieWithDetails(Long id) {
        Optional<MovieJDBC> movieDB = movieRepo.findById(id);
        if (movieDB.isPresent()) {
            return mvTransformer.jdbcToDto(movieDB.get());
        }
        return null;
    }

    @Override
    public List<DirectorDTO> getMovieDirectors(Long id) {
        MovieJDBC movie = movieRepo.findByIdDirectors(id);
        return drTransformer.jdbcToDto(movie.getDirectors());
    }

    @Override
    public List<WriterDTO> getMovieWriters(Long id) {
        MovieJDBC movie = movieRepo.findByIdWriters(id);
        return wrTransformer.jdbcToDto(movie.getWriters());
    }

    @Override
    public List<ActorDTO> getMovieActors(Long id) {
        MovieJDBC movie = movieRepo.findByIdActors(id);
        List<ActorDTO> actors = new ArrayList(movie.getActings().size());
        for (ActingJDBC acting : movie.getActings()) {
            actors.add(acTranformer.jdbcToDto(acting.getActor()));
        }
        return actors;

    }

    @Override
    public List<ActingDTO> getMovieActorsWithRoles(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

}
