/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.dto.classes.TVShowDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "tv")
public class TvShowController {

    //=========================GET MAPPINGS==================================
    //Returns: all tv shows
    //Included: Genres
    //Excluded: Directors, Writers, Actors, Acting roles
    @GetMapping
    public List<TVShowDTO> getAllTVShows() {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: all tv shows
    //Included: Genres,Directors, Writers, Actors, Acting roles
    //Excluded:
    @GetMapping(path = "/details")
    public List<TVShowDTO> getAllTVShowsWithDetails() {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: specific tv show with given id
    //Included: Genres
    //Excluded: Directors, Writers, Actors, Acting roles
    @GetMapping(path = "/{id}")
    public List<TVShowDTO> getTVShow(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: specific tv show with given id
    //Included: Genres,Directors, Writers, Actors, Acting roles
    //Excluded:
    @GetMapping(path = "/{id}/details")
    public List<TVShowDTO> getTVShowWithDetails(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: directors of specific tv show with given id
    //Included:
    //Excluded:
    @GetMapping(path = "/{id}/directors")
    public List<TVShowDTO> getTVShowDirectors(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: writers of specific tv show with given id
    //Included:
    //Excluded:
    @GetMapping(path = "/{id}/writers")
    public List<TVShowDTO> getTVShowWriters(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: actors of specific tv show with given id
    //Included:
    //Excluded:  Acting roles
    @GetMapping(path = "/{id}/actors")
    public List<TVShowDTO> getTVShowActors(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //Returns: actors of specific tv show with given id
    //Included: Acting roles
    //Excluded:
    @GetMapping(path = "/{id}/actors/roles")
    public List<TVShowDTO> getTVShowActorsWithRoles(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    //=========================POST MAPPINGS==================================
    //=========================PUT MAPPINGS===================================
    //=========================DELETE MAPPINGS================================
}
