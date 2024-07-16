/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.tv;

import com.borak.cwb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.ActingTransformer;
import com.borak.cwb.backend.logic.transformers.ActorTransformer;
import com.borak.cwb.backend.logic.transformers.DirectorTransformer;
import com.borak.cwb.backend.logic.transformers.TVShowTransformer;
import com.borak.cwb.backend.logic.transformers.WriterTransformer;
import com.borak.cwb.backend.repository.api.IActingRepository;
import com.borak.cwb.backend.repository.api.IActorRepository;
import com.borak.cwb.backend.repository.api.IDirectorRepository;
import com.borak.cwb.backend.repository.api.IGenreRepository;
import com.borak.cwb.backend.repository.api.ITVShowRepository;
import com.borak.cwb.backend.repository.api.IWriterRepository;
import com.borak.cwb.backend.repository.file.FileRepository;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
//@Service
//@Transactional
public class TVShowServiceJDBC implements ITVShowService<TVShowRequestDTO> {

    private static final int POPULARITY_TRESHOLD = 80;

    @Autowired
    private ITVShowRepository<TVShowJDBC, Long> tvShowRepo;
    @Autowired
    private IGenreRepository<GenreJDBC, Long> genreRepo;
    @Autowired
    private IDirectorRepository<DirectorJDBC, Long> directorRepo;
    @Autowired
    private IWriterRepository<WriterJDBC, Long> writerRepo;
    @Autowired
    private IActorRepository<ActorJDBC, Long> actorRepo;
    @Autowired
    private IActingRepository<ActingJDBC, Long> actingRepo;
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
        List<TVShowResponseDTO> tvShows = tvShowTransformer.jdbcToTVShowResponse(tvShowRepo.findAllWithGenresPaginated(page, size));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenresPopularPaginated(int page, int size) {
        List<TVShowResponseDTO> tvShows = tvShowTransformer.jdbcToTVShowResponse(tvShowRepo.findAllByAudienceRatingWithGenresPaginated(page, size, POPULARITY_TRESHOLD));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue() - 1;
        List<TVShowResponseDTO> tvShows = tvShowTransformer.jdbcToTVShowResponse(tvShowRepo.findAllByReleaseYearWithGenresPaginated(page, size, year));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithDetailsPaginated(int page, int size) {
        List<TVShowResponseDTO> tvShows = tvShowTransformer.jdbcToTVShowResponse(tvShowRepo.findAllWithRelationsPaginated(page, size));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getTVShowWithGenres(Long id) {
        Optional<TVShowJDBC> tvShow = tvShowRepo.findByIdWithGenres(id);
        if (tvShow.isPresent()) {
            return new ResponseEntity<>(tvShowTransformer.toResponseFromJDBC(tvShow.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowWithDetails(Long id) {
        Optional<TVShowJDBC> tvShow = tvShowRepo.findByIdWithRelations(id);
        if (tvShow.isPresent()) {
            return new ResponseEntity<>(tvShowTransformer.toResponseFromJDBC(tvShow.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowDirectors(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<DirectorJDBC> directors = directorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(directorTransformer.jdbcToDirectorResponse(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowWriters(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<WriterJDBC> writers = writerRepo.findAllByMediaId(id);
            return new ResponseEntity<>(writerTransformer.jdbcToWriterResponse(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowActors(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<ActorJDBC> actors = actorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actorTransformer.jdbcToActorResponse(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowActorsWithRoles(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<ActingJDBC> actings = actingRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actingTransformer.jdbcToActorResponse(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity deleteTVShowById(long id) {
        Optional<TVShowJDBC> tvShow = tvShowRepo.findByIdWithRelations(id);
        if (tvShow.isEmpty()) {
            throw new ResourceNotFoundException("No tv show found with id: " + id);
        }
        tvShowRepo.deleteById(id);
        if (tvShow.get().getCoverImage() != null && !tvShow.get().getCoverImage().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(tvShow.get().getCoverImage());
        }
        return new ResponseEntity(tvShowTransformer.toResponseFromJDBC(tvShow.get()), HttpStatus.OK);
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
        TVShowJDBC tvShowJDBC = tvShowTransformer.toTVShowJDBC(tvShowClient);
        tvShowJDBC.setCoverImage(null);
        TVShowJDBC tvShowDB = tvShowRepo.insert(tvShowJDBC);
        Optional<TVShowJDBC> tvShow;
        if (tvShowClient.getCoverImage() != null) {
            tvShowClient.getCoverImage().setName("" + tvShowDB.getId());
            tvShowRepo.updateCoverImage(tvShowDB.getId(), tvShowClient.getCoverImage().getFullName());
            tvShow = tvShowRepo.findByIdWithRelations(tvShowDB.getId());
            fileRepo.saveMediaCoverImage(tvShowClient.getCoverImage());
        } else {
            tvShow = tvShowRepo.findByIdWithRelations(tvShowDB.getId());
        }
        return new ResponseEntity<>(tvShowTransformer.toResponseFromJDBC(tvShow.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putTVShow(TVShowRequestDTO request) {
        if (!tvShowRepo.existsById(request.getId())) {
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
        Optional<TVShowJDBC> response;
        Optional<String> beforeUpdateCoverImage = tvShowRepo.findByIdCoverImage(request.getId());
        if (request.getCoverImage() != null) {
            //MyImage != null
            //replace cover image
            request.getCoverImage().setName("" + request.getId());
            TVShowJDBC tvShowToStore = tvShowTransformer.toTVShowJDBC(request);
            tvShowRepo.update(tvShowToStore);
            response = tvShowRepo.findByIdWithRelations(request.getId());
            if (beforeUpdateCoverImage.isPresent()) {
                fileRepo.deleteIfExistsMediaCoverImage(beforeUpdateCoverImage.get());
            }
            fileRepo.saveMediaCoverImage(request.getCoverImage());
        } else {
            //MyImage == null
            //delete cover image         
            TVShowJDBC tvShowToStore = tvShowTransformer.toTVShowJDBC(request);
            tvShowRepo.update(tvShowToStore);
            response = tvShowRepo.findByIdWithRelations(request.getId());
            if (beforeUpdateCoverImage.isPresent()) {
                fileRepo.deleteIfExistsMediaCoverImage(beforeUpdateCoverImage.get());
            }
        }
        return new ResponseEntity<>(tvShowTransformer.toResponseFromJDBC(response.get()), HttpStatus.OK);
    }

}
