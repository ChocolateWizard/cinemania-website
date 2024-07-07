/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.media;

import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.enums.SortOption;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import com.borak.cwb.backend.domain.jpa.specifications.MediaJPASpecs;
import com.borak.cwb.backend.domain.jpa.specifications.MovieJPASpecs;
import com.borak.cwb.backend.domain.jpa.specifications.TVShowJPASpecs;
import com.borak.cwb.backend.exceptions.UnexpectedException;
import com.borak.cwb.backend.logic.transformers.MediaTransformer;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.MovieRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.TVShowRepositoryJPA;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
public class MediaServiceJPA implements IMediaService {

    @Autowired
    private MediaRepositoryJPA mediaRepo;
    @Autowired
    private MovieRepositoryJPA movieRepo;
    @Autowired
    private TVShowRepositoryJPA tvShowRepo;

    @Autowired
    private MediaTransformer mediaTransformer;

    @Override
    public ResponseEntity getAllMediasByTitleWithGenresPaginated(int page, int size, String title, List<Long> genreIds, SortOption sortByAudienceRating, SortOption sortByReleaseDate, Integer releaseYear, MediaType mediaType) {
        Sort sort = Sort.unsorted();
        if (sortByAudienceRating != null) {
            switch (sortByAudienceRating) {
                case ASC -> {
                    sort = sort.and(Sort.by("audienceRating").ascending());
                }
                case DESC -> {
                    sort = sort.and(Sort.by("audienceRating").descending());
                }
                default ->
                    throw new UnexpectedException("Unknown sorting option!");
            }
        }
        if (sortByReleaseDate != null) {
            switch (sortByReleaseDate) {
                case ASC -> {
                    sort = sort.and(Sort.by("releaseDate").ascending());
                }
                case DESC -> {
                    sort = sort.and(Sort.by("releaseDate").descending());
                }
                default ->
                    throw new UnexpectedException("Unknown sorting option!");
            }
        }
        Pageable p = PageRequest.of(page - 1, size, sort);
        if (mediaType == null) {
            //search for all media
            Specification<MediaJPA> spec = Specification.where(null);
            if (title != null) {
                spec = spec.and(MediaJPASpecs.byTitleLike("%" + title + "%"));
            }
            if (genreIds != null && !genreIds.isEmpty()) {
                spec = spec.and(MediaJPASpecs.hasGenres(genreIds));
            }
            if (releaseYear != null) {
                spec = spec.and(MediaJPASpecs.byReleaseDateYear(releaseYear));
            }
            Page<MediaJPA> medias = mediaRepo.findAll(spec, p);
            return new ResponseEntity(mediaTransformer.jpaToMediaResponse(medias.getContent()), HttpStatus.OK);
        } else {
            switch (mediaType) {
                case MOVIE -> {
                    //search for movies
                    Specification<MovieJPA> spec = Specification.where(null);
                    if (title != null) {
                        spec = spec.and(MovieJPASpecs.byTitleLike("%" + title + "%"));
                    }
                    if (genreIds != null && !genreIds.isEmpty()) {
                        spec = spec.and(MovieJPASpecs.hasGenres(genreIds));
                    }
                    if (releaseYear != null) {
                        spec = spec.and(MovieJPASpecs.byReleaseDateYear(releaseYear));
                    }
                    Page<MovieJPA> movies = movieRepo.findAll(spec, p);
                    return new ResponseEntity(mediaTransformer.jpaToMediaResponse(movies.getContent()), HttpStatus.OK);
                }
                case TV_SHOW -> {
                    //search for tv shows
                    Specification<TVShowJPA> spec = Specification.where(null);
                    if (title != null) {
                        spec = spec.and(TVShowJPASpecs.byTitleLike("%" + title + "%"));
                    }
                    if (genreIds != null && !genreIds.isEmpty()) {
                        spec = spec.and(TVShowJPASpecs.hasGenres(genreIds));
                    }
                    if (releaseYear != null) {
                        spec = spec.and(TVShowJPASpecs.byReleaseDateYear(releaseYear));
                    }
                    Page<TVShowJPA> tvShows = tvShowRepo.findAll(spec, p);
                    return new ResponseEntity(mediaTransformer.jpaToMediaResponse(tvShows.getContent()), HttpStatus.OK);
                }
                default ->
                    throw new UnexpectedException("Unknown media type!");
            }
        }

    }

}
