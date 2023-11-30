/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;

import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Mr. Poyo
 */
public interface IMovieRepository<M, ID> extends IRepository<M, ID> {

    public List<M> findAllWithGenres() throws DatabaseException;

    public List<M> findAllWithGenresPaginated(int page, int size) throws DatabaseException;
    
    public List<M> findAllWithRelations() throws DatabaseException;

    public List<M> findAllWithRelationsPaginated(int page, int size) throws DatabaseException;

    public List<M> findAllByAudienceRatingWithGenresPaginated(int page, int size, int ratingThresh) throws DatabaseException;

    public List<M> findAllByReleaseYearWithGenresPaginated(int page, int size, int year) throws DatabaseException;

    public Optional<String> findByIdCoverImage(ID id) throws DatabaseException;

    public Optional<M> findByIdWithRelations(ID id) throws DatabaseException;
    
    public Optional<M> findByIdWithGenres(ID id) throws DatabaseException;
    
    public void updateCoverImage(ID id, String coverImage) throws DatabaseException;

}
