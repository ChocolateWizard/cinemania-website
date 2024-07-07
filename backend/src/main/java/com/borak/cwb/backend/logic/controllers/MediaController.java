/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.controllers;

import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.enums.SortOption;
import com.borak.cwb.backend.logic.services.media.IMediaService;
import com.borak.cwb.backend.logic.services.validation.DomainValidationService;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "api/medias")
@Validated
public class MediaController {

    @Autowired
    private IMediaService mediaService;
    @Autowired
    private DomainValidationService domainValidator;

    //=========================GET MAPPINGS==================================  
    @GetMapping(path = "/search")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getMediasByTitle(
            @RequestParam(name = "page", defaultValue = "1", required = false)
            @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            @Min(value = 1, message = "Size number has to be greater than or equal to 1")
            @Max(value = 100, message = "Size number has to be less than or equal to 100") int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "genreIds", required = false)
            @Size(max = 5, message = "Number of genre ids must be less than or equal to 5") List<
                    @NotNull(message = "Genre id must not be null")
            @Min(value = 1, message = "Genre id has to be greater than or equal to 1") Long> genreIds,
            @RequestParam(name = "sortByAudienceRating", required = false) SortOption sortByAudienceRating,
            @RequestParam(name = "sortByReleaseDate", required = false) SortOption sortByReleaseDate,
            @RequestParam(name = "releaseYear", required = false)
            @Min(value = 1965, message = "Release date year has to be greater than or equal to 1965") Integer releaseYear,
            @RequestParam(name = "mediaType", required = false) MediaType mediaType
    ) {
        domainValidator.validate(title, genreIds);
        return mediaService.getAllMediasByTitleWithGenresPaginated(page, size, title, genreIds, sortByAudienceRating, sortByReleaseDate, releaseYear, mediaType);
    }

}
