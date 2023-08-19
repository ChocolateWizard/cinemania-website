/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;

import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 * Movie repository meant to store Movie objects into database
 *
 * @author Mr. Poyo
 * @param <M> must represent movie in database
 * @param <G> must represent genre in database
 * @param <D> must represent director in database
 * @param <W> must represent writer in database
 * @param <A> must represent actor in database
 * @param <AC> must represent acting in database
 * @param <ID> must represent movie ID in database
 */
public interface IMovieRepository<M, G, D, W, A, AC, ID> extends IRepository<M, ID> {

    public List<M> findAllNoRelationships() throws DatabaseException;

    public List<M> findAllRelationshipGenres() throws DatabaseException;
    
    public List<M> findAllNoRelationshipsPaginated(int page,int size) throws DatabaseException;

    public List<M> findAllRelationshipGenresPaginated(int page,int size) throws DatabaseException;
    
    public List<M> findAllByAudienceRatingRelationshipGenresPaginated(int page,int size,int ratingThresh) throws DatabaseException;
    
    public List<M> findAllByReleaseYearRelationshipGenresPaginated(int page,int size,int year) throws DatabaseException;

    public Optional<M> findByIdNoRelationships(ID id) throws DatabaseException;

    public List<G> findByIdGenres(ID id) throws DatabaseException;

    public List<D> findByIdDirectors(ID id) throws DatabaseException;

    public List<W> findByIdWriters(ID id) throws DatabaseException;

    public List<A> findByIdActors(ID id) throws DatabaseException;

    public List<AC> findByIdActorsWithRoles(ID id) throws DatabaseException;

}
