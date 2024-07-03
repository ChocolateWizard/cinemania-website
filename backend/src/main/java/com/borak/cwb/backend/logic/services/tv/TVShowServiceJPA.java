/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.tv;

import com.borak.cwb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.ActorJPA;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.ActingTransformer;
import com.borak.cwb.backend.logic.transformers.ActorTransformer;
import com.borak.cwb.backend.logic.transformers.DirectorTransformer;
import com.borak.cwb.backend.logic.transformers.TVShowTransformer;
import com.borak.cwb.backend.logic.transformers.WriterTransformer;
import com.borak.cwb.backend.repository.jpa.ActorRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.DirectorRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.GenreRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.TVShowRepositoryJPA;
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
public class TVShowServiceJPA implements ITVShowService<TVShowRequestDTO> {

    private static final int POPULARITY_TRESHOLD = 80;

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private TVShowRepositoryJPA tvShowRepo;
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
    private TVShowTransformer tvShowTransformer;
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
    public ResponseEntity getAllTVShowsWithGenresPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<TVShowJPA> t = tvShowRepo.findAll(p);
        List<TVShowResponseDTO> response = tvShowTransformer.jpaToTVShowResponse(t.getContent(), "genres");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenresPopularPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<TVShowJPA> t = tvShowRepo.findAllByAudienceRatingGreaterThanEqual(POPULARITY_TRESHOLD, p);
        List<TVShowResponseDTO> response = tvShowTransformer.jpaToTVShowResponse(t.getContent(), "genres");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue() - 1;
        Pageable p = PageRequest.of(page - 1, size);
        Page<TVShowJPA> t = tvShowRepo.findAllByReleaseDateYearGreaterThanEqual(year, p);
        List<TVShowResponseDTO> response = tvShowTransformer.jpaToTVShowResponse(t.getContent(), "genres");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithDetailsPaginated(int page, int size) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<TVShowJPA> t = tvShowRepo.findAll(p);
        List<TVShowResponseDTO> response = tvShowTransformer.jpaToTVShowResponse(t.getContent(), "details");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getTVShowWithGenres(Long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isPresent()) {
            return new ResponseEntity<>(tvShowTransformer.toResponseFromJPA(tvShow.get(), "genres"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowWithDetails(Long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isPresent()) {
            return new ResponseEntity<>(tvShowTransformer.toResponseFromJPA(tvShow.get(), "details"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowDirectors(Long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isPresent()) {
            List<DirectorJPA> directors = tvShow.get().getDirectors();
            return new ResponseEntity<>(directorTransformer.jpaToDirectorResponse(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowWriters(Long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isPresent()) {
            List<WriterJPA> writers = tvShow.get().getWriters();
            return new ResponseEntity<>(writerTransformer.jpaToWriterResponse(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowActors(Long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isPresent()) {
            List<ActorJPA> actors = tvShow.get().getActings().stream().map(e -> e.getActor()).toList();
            return new ResponseEntity<>(actorTransformer.jpaToActorResponse(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowActorsWithRoles(Long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isPresent()) {
            List<ActingJPA> actings = tvShow.get().getActings();
            return new ResponseEntity<>(actingTransformer.jpaToActorResponse(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity deleteTVShowById(long id) {
        Optional<TVShowJPA> tvShow = tvShowRepo.findById(id);
        if (tvShow.isEmpty()) {
            throw new ResourceNotFoundException("No tv show found with id: " + id);
        }
        tvShowRepo.deleteById(id);
        if (tvShow.get().getCoverImage() != null && !tvShow.get().getCoverImage().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(tvShow.get().getCoverImage());
        }
        return new ResponseEntity(tvShowTransformer.toResponseFromJPA(tvShow.get(), "details"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity postTVShow(TVShowRequestDTO tvShowClient) {
        for (Long genre : tvShowClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : tvShowClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : tvShowClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (TVShowRequestDTO.Actor actor : tvShowClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        TVShowJPA tvShowToSave = tvShowTransformer.toTVShowJPA(tvShowClient);
        tvShowToSave.setCoverImage(null);
        TVShowJPA tvShow;
        if (tvShowClient.getCoverImage() != null) {
            tvShow = tvShowRepo.save(tvShowToSave);
            tvShowClient.getCoverImage().setName("" + tvShow.getId());
            tvShow.setCoverImage(tvShowClient.getCoverImage().getFullName());
            tvShow = tvShowRepo.saveAndFlush(tvShow);
            manager.refresh(tvShow);
            fileRepo.saveMediaCoverImage(tvShowClient.getCoverImage());
        } else {
            tvShow = tvShowRepo.saveAndFlush(tvShowToSave);
            manager.refresh(tvShow);
        }
        return new ResponseEntity<>(tvShowTransformer.toResponseFromJPA(tvShow, "details"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putTVShow(TVShowRequestDTO request) {
        Optional<TVShowJPA> tvShowDB = tvShowRepo.findById(request.getId());
        if (!tvShowDB.isPresent()) {
            throw new ResourceNotFoundException("TV show with id: " + request.getId() + " does not exist in database!");
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
        for (TVShowRequestDTO.Actor actor : request.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        TVShowJPA tvShow;
        String coverImageDB = tvShowDB.get().getCoverImage();
        if (request.getCoverImage() != null) {
            //MyImage != null
            //client provided a cover image in PUT request so he wished to replace the current one if it's present
            request.getCoverImage().setName("" + request.getId());
            TVShowJPA tvShowToSave = tvShowTransformer.toTVShowJPA(request);
            for (ActingJPA acting : tvShowToSave.getActings()) {
                for (ActingJPA actingDB : tvShowDB.get().getActings()) {
                    if (Objects.equals(actingDB.getActor().getPersonId(), acting.getActor().getPersonId())) {
                        acting.setId(actingDB.getId());
                    }
                }
            }
            tvShow = tvShowRepo.saveAndFlush(tvShowToSave);
            manager.refresh(tvShow);
            if (coverImageDB != null) {
                fileRepo.deleteIfExistsMediaCoverImage(coverImageDB);
            }
            fileRepo.saveMediaCoverImage(request.getCoverImage());
        } else {
            //MyImage == null
            //client provided no cover image in PUT request so he wished to delete the exisitng one if present         
            TVShowJPA tvShowToSave = tvShowTransformer.toTVShowJPA(request);
            for (ActingJPA acting : tvShowToSave.getActings()) {
                for (ActingJPA actingDB : tvShowDB.get().getActings()) {
                    if (Objects.equals(actingDB.getActor().getPersonId(), acting.getActor().getPersonId())) {
                        acting.setId(actingDB.getId());
                    }
                }
            }
            tvShow = tvShowRepo.saveAndFlush(tvShowToSave);
            manager.refresh(tvShow);
            if (coverImageDB != null) {
                fileRepo.deleteIfExistsMediaCoverImage(coverImageDB);
            }
        }
        return new ResponseEntity<>(tvShowTransformer.toResponseFromJPA(tvShow, "details"), HttpStatus.OK);
    }

}
