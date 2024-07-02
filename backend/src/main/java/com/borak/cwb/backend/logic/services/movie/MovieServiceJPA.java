/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.movie;

import com.borak.cwb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.ActorJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.ActingTransformer;
import com.borak.cwb.backend.logic.transformers.ActorTransformer;
import com.borak.cwb.backend.logic.transformers.DirectorTransformer;
import com.borak.cwb.backend.logic.transformers.MovieTransformer;
import com.borak.cwb.backend.logic.transformers.WriterTransformer;
import com.borak.cwb.backend.repository.jpa.ActorRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.DirectorRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.GenreRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.MovieRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.WriterRepositoryJPA;
import com.borak.cwb.backend.repository.file.FileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class MovieServiceJPA implements IMovieService<MovieRequestDTO> {

    private static final int POPULARITY_TRESHOLD = 80;

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private MovieRepositoryJPA movieRepo;
    @Autowired
    private GenreRepositoryJPA genreRepo;
    @Autowired
    private DirectorRepositoryJPA directorRepo;
    @Autowired
    private WriterRepositoryJPA writerRepo;
    @Autowired
    private ActorRepositoryJPA actorRepo;
    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private MovieTransformer movieTransformer;
    @Autowired
    private DirectorTransformer directorTransformer;
    @Autowired
    private WriterTransformer writerTransformer;
    @Autowired
    private ActorTransformer actorTransformer;
    @Autowired
    private ActingTransformer actingTransformer;
//----------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity getAllMoviesWithGenresPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<MovieJPA> m = movieRepo.findAll(p);
        List<MovieResponseDTO> response = movieTransformer.toResponseFromJPA(m.getContent(), new Class[]{GenreJPA.class});
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<MovieJPA> m = movieRepo.findAllByAudienceRatingGreaterThanEqual(POPULARITY_TRESHOLD, p);
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJPA(m.getContent(), new Class[]{GenreJPA.class});
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue() - 1;
        Pageable p = PageRequest.of(page - 1, size);
        Page<MovieJPA> m = movieRepo.findAllByReleaseDateYearGreaterThanEqual(year, p);
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJPA(m.getContent(), new Class[]{GenreJPA.class});
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<MovieJPA> m = movieRepo.findAll(p);
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJPA(m.getContent(),
                new Class[]{GenreJPA.class, DirectorJPA.class, WriterJPA.class, ActingJPA.class, CritiqueJPA.class});
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getMovieWithGenres(Long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.toResponseFromJPA(movie.get(), new Class[]{GenreJPA.class}), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWithDetails(Long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.toResponseFromJPA(movie.get(),
                    new Class[]{GenreJPA.class, DirectorJPA.class, WriterJPA.class, ActingJPA.class, CritiqueJPA.class}), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieDirectors(Long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            List<DirectorJPA> directors = movie.get().getDirectors();
            return new ResponseEntity<>(directorTransformer.toMovieDirectorResponseFromJPA(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWriters(Long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            List<WriterJPA> writers = movie.get().getWriters();
            return new ResponseEntity<>(writerTransformer.toMovieWriterResponseFromJPA(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActors(Long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            List<ActorJPA> actors = movie.get().getActings().stream().map(e -> e.getActor()).toList();
            return new ResponseEntity<>(actorTransformer.toMovieActorResponseFromJPA(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActorsWithRoles(Long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            List<ActingJPA> actings = movie.get().getActings();
            return new ResponseEntity<>(actingTransformer.toMovieActorResponseFromJPA(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity deleteMovieById(long id) {
        Optional<MovieJPA> movie = movieRepo.findById(id);
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("No movie found with id: " + id);
        }
        movieRepo.deleteById(id);
        if (movie.get().getCoverImage() != null && !movie.get().getCoverImage().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(movie.get().getCoverImage());
        }
        return new ResponseEntity(movieTransformer.toResponseFromJPA(movie.get(),
                new Class[]{GenreJPA.class, DirectorJPA.class, WriterJPA.class, ActingJPA.class, CritiqueJPA.class}), HttpStatus.OK);
    }

    @Override
    public ResponseEntity postMovie(MovieRequestDTO movieClient) {
        for (Long genre : movieClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : movieClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : movieClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (MovieRequestDTO.Actor actor : movieClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        MovieJPA movieToSave = movieTransformer.toMovieJPA(movieClient);
        movieToSave.setCoverImage(null);
        MovieJPA movie;
        if (movieClient.getCoverImage() != null) {
            movie = movieRepo.save(movieToSave);
            movieClient.getCoverImage().setName("" + movie.getId());
            movie.setCoverImage(movieClient.getCoverImage().getFullName());
            movie = movieRepo.saveAndFlush(movie);
            manager.refresh(movie);
            fileRepo.saveMediaCoverImage(movieClient.getCoverImage());
        } else {
            movie = movieRepo.saveAndFlush(movieToSave);
            manager.refresh(movie);
        }
        return new ResponseEntity<>(movieTransformer.toResponseFromJPA(movie,
                new Class[]{GenreJPA.class, DirectorJPA.class, WriterJPA.class, ActingJPA.class, CritiqueJPA.class}), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putMovie(MovieRequestDTO request) {
        Optional<MovieJPA> movieDB = movieRepo.findById(request.getId());
        if (!movieDB.isPresent()) {
            throw new ResourceNotFoundException("Movie with id: " + request.getId() + " does not exist in database!");
        }
        for (Long genre : request.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : request.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : request.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (MovieRequestDTO.Actor actor : request.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        MovieJPA movie;
        String coverImageDB = movieDB.get().getCoverImage();
        if (request.getCoverImage() != null) {
            //MyImage != null
            //client provided a cover image in PUT request so he wished to replace the current one if it's present
            request.getCoverImage().setName("" + request.getId());
            MovieJPA movieToSave = movieTransformer.toMovieJPA(request);
            for (ActingJPA acting : movieToSave.getActings()) {
                for (ActingJPA actingDB : movieDB.get().getActings()) {
                    if (Objects.equals(actingDB.getActor().getPersonId(), acting.getActor().getPersonId())) {
                        acting.setId(actingDB.getId());
                    }
                }
            }
            movie = movieRepo.saveAndFlush(movieToSave);
            manager.refresh(movie);
            if (coverImageDB != null) {
                fileRepo.deleteIfExistsMediaCoverImage(coverImageDB);
            }
            fileRepo.saveMediaCoverImage(request.getCoverImage());
        } else {
            //MyImage == null
            //client provided no cover image in PUT request so he wished to delete the exisitng one if present         
            MovieJPA movieToSave = movieTransformer.toMovieJPA(request);
            for (ActingJPA acting : movieToSave.getActings()) {
                for (ActingJPA actingDB : movieDB.get().getActings()) {
                    if (Objects.equals(actingDB.getActor().getPersonId(), acting.getActor().getPersonId())) {
                        acting.setId(actingDB.getId());
                    }
                }
            }
            movie = movieRepo.saveAndFlush(movieToSave);
            manager.refresh(movie);
            if (coverImageDB != null) {
                fileRepo.deleteIfExistsMediaCoverImage(coverImageDB);
            }
        }
        return new ResponseEntity<>(movieTransformer.toResponseFromJPA(movie,
                new Class[]{GenreJPA.class, DirectorJPA.class, WriterJPA.class, ActingJPA.class, CritiqueJPA.class}), HttpStatus.OK);
    }

}
