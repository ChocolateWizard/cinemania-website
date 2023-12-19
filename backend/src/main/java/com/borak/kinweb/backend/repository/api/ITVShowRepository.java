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
public interface ITVShowRepository<TV, ID> extends IRepository<TV, ID> {

    public List<TV> findAllWithGenres() throws DatabaseException;

    public List<TV> findAllWithRelations() throws DatabaseException;

    public List<TV> findAllWithGenresPaginated(int page, int size) throws DatabaseException, IllegalArgumentException;

    public List<TV> findAllWithRelationsPaginated(int page, int size) throws DatabaseException, IllegalArgumentException;

    public List<TV> findAllByAudienceRatingWithGenresPaginated(int page, int size, int ratingThresh) throws DatabaseException, IllegalArgumentException;

    public List<TV> findAllByReleaseYearWithGenresPaginated(int page, int size, int year) throws DatabaseException, IllegalArgumentException;

    public Optional<String> findByIdCoverImage(ID id) throws DatabaseException, IllegalArgumentException;

    public Optional<TV> findByIdWithGenres(ID id) throws DatabaseException, IllegalArgumentException;

    public Optional<TV> findByIdWithRelations(ID id) throws DatabaseException, IllegalArgumentException;

    public void updateCoverImage(ID id, String coverImage) throws DatabaseException, IllegalArgumentException;
}
