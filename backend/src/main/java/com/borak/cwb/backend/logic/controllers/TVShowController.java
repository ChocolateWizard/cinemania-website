/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.controllers;

import com.borak.cwb.backend.domain.MyImage;
import com.borak.cwb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.cwb.backend.logic.services.tv.ITVShowService;
import com.borak.cwb.backend.logic.services.validation.DomainValidationService;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "api/tv")
@Validated
public class TVShowController {

    private final ITVShowService tvShowService;
    private final DomainValidationService domainValidator;

    @Autowired
    public TVShowController(ITVShowService tvShowService, DomainValidationService domainValidator) {
        this.tvShowService = tvShowService;
        this.domainValidator = domainValidator;
    }

//=================================================================================================================================
//GET
    @GetMapping
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getTVShows(
            @RequestParam(name = "page", defaultValue = "1", required = false)
            @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            @Min(value = 1, message = "Size number has to be greater than or equal to 1")
            @Max(value = 100, message = "Size number has to be less than or equal to 100") int size) {
        return tvShowService.getAllTVShowsWithGenresPaginated(page, size);
    }

    @GetMapping(path = "/popular")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getTVShowsPopular(
            @RequestParam(name = "page", defaultValue = "1", required = false)
            @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            @Min(value = 1, message = "Size number has to be greater than or equal to 1")
            @Max(value = 100, message = "Size number has to be less than or equal to 100") int size) {
        return tvShowService.getAllTVShowsWithGenresPopularPaginated(page, size);
    }

    @GetMapping(path = "/current")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getTVShowsCurrent(
            @RequestParam(name = "page", defaultValue = "1", required = false)
            @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            @Min(value = 1, message = "Size number has to be greater than or equal to 1")
            @Max(value = 100, message = "Size number has to be less than or equal to 100") int size) {
        return tvShowService.getAllTVShowsWithGenresCurrentPaginated(page, size);
    }

    @GetMapping(path = "/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getTVShowsDetails(
            @RequestParam(name = "page", defaultValue = "1", required = false)
            @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            @Min(value = 1, message = "Size number has to be greater than or equal to 1")
            @Max(value = 100, message = "Size number has to be less than or equal to 100") int size) {
        return tvShowService.getAllTVShowsWithDetailsPaginated(page, size);
    }

    @GetMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Medium.class)
    public ResponseEntity getTVShowById(@PathVariable @Min(value = 1, message = "TV show id must be greater than or equal to 1") long id) {
        return tvShowService.getTVShowWithGenres(id);
    }

    @GetMapping(path = "/{id}/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getTVShowByIdDetails(@PathVariable @Min(value = 1, message = "TV show id must be greater than or equal to 1") long id) {
        return tvShowService.getTVShowWithDetails(id);
    }

//=================================================================================================================================
//POST
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity postTVShow(@RequestPart("tv_show") TVShowRequestDTO tvShow, @RequestPart(name = "cover_image", required = false) MultipartFile coverImage) {
        domainValidator.validate(tvShow, coverImage, RequestMethod.POST);
        if (coverImage != null) {
            tvShow.setCoverImage(new MyImage(coverImage));
        }
        return tvShowService.postTVShow(tvShow);
    }

//=================================================================================================================================
//PUT
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity putTVShow(@PathVariable @Min(value = 1, message = "TV show id must be greater than or equal to 1") long id, @RequestPart("tv_show") TVShowRequestDTO tvShow, @RequestPart(name = "cover_image", required = false) MultipartFile coverImage) {
        tvShow.setId(id);
        domainValidator.validate(tvShow, coverImage, RequestMethod.PUT);
        if (coverImage != null) {
            tvShow.setCoverImage(new MyImage(coverImage));
        }
        return tvShowService.putTVShow(tvShow);
    }

//=================================================================================================================================
//DELETE
    @DeleteMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity deleteTVShowById(@PathVariable @Min(value = 1, message = "TV show id must be greater than or equal to 1") long id) {
        return tvShowService.deleteTVShowById(id);
    }

}
