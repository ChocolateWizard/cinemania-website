/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;

/**
 * Movie repository meant to store Movie objects into database
 *
 * @author Mr. Poyo
 */
public interface IMovieRepository<T, ID> extends IRepository<T, ID> {

    /**
     * Insert or update a given movie entity with its relationships. Critiques
     * and ActingRoles will be ignored. Use a separate repository for these
     * entities. Also criticRating attribute will be ignored, since its value is
     * managed by database trigger.
     * <p>
     * Relationship entities are assumed to be present in database, and their
     * IDs present in their entities. Their attributes will not be updated, only
     * relationship to them. If Movie ID is present then this operation will
     * perform update, otherwise it will insert.
     * <p>
     * If movie ID is not present in database, update will be ignored. Use the
     * returned instance for further operations as the save operation might have
     * changed the entity instance completely.
     *
     * @param movie entity must not be {@literal null} and its fields and
     * relationships must be valid.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is
     * {@literal null} or not valid.
     * @throws DataAccessException if there is any problem
     */
    T save(T movie) throws DataAccessException, IllegalArgumentException;

    /**
     * Insert or update all given movie entities with their relationships.
     * Critiques and ActingRoles will be ignored. Use a separate repository for
     * these entities. Also criticRating attribute will be ignored, since its
     * value is managed by database trigger.
     * <p>
     * Relationship entities are assumed to be present in database, and their
     * IDs present in their entities. Their attributes will not be updated, only
     * relationship to them. If Movie ID is present then this operation will
     * perform update, otherwise it will insert.
     * <p>
     * If movie ID is not present in database, update will be ignored. Use the
     * returned instance for further operations as the save operation might have
     * changed the entity instance completely.
     *
     * @param movies must not be {@literal null} nor must it contain
     * {@literal null}. Each movie entity in list must have be valid.
     * @return the saved entities; will never be {@literal null}. The returned
     * {@literal List} will have the same size as the {@literal List} passed as
     * an argument.
     * @throws IllegalArgumentException in case the given {@link List entities}
     * or one of its entities is {@literal null} or not valid.
     * @throws DataAccessException if there is any problem.
     */
    List<T> saveAll(List<T> movies) throws DataAccessException, IllegalArgumentException;

    /**
     * Retrieves a movie entity by its id, with all of its relationships. Critic
     * entities in Critiques will only have username set.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if
     * none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     * @throws DataAccessException if there is any problem.
     */
    Optional<T> findById(ID id) throws DataAccessException, IllegalArgumentException;

    /**
     * Returns whether a movie entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists,
     * {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    boolean existsById(ID id);

    /**
     * Returns all instances of the movie type with all of its relationships.
     *
     * @return all entities
     */
    List<T> findAll();

    /**
     * Returns all instances of the type {@code T} with the given IDs.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null}
     * values.
     * @return guaranteed to be not {@literal null}. The size can be equal or
     * less than the number of given {@literal ids}.
     * @throws IllegalArgumentException in case the given {@link Iterable ids}
     * or one of its items is {@literal null}.
     */
    List<T> findAllById(List<ID> ids);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    long count();

    /**
     * Deletes the entity with the given id.
     * <p>
     * If the entity is not found in the persistence store it is silently
     * ignored.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is
     * {@literal null}
     */
    void deleteById(ID id);

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is
     * {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     * locking and has a version attribute with a different value from that
     * found in the persistence store. Also thrown if the entity is assumed to
     * be present but does not exist in the database.
     */
    void delete(T movie);

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     * <p>
     * Entities that aren't found in the persistence store are silently ignored.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null}
     * elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one
     * of its elements is {@literal null}.
     * @since 2.5
     */
    void deleteAllById(List<ID> ids);

    /**
     * Deletes the given entities.
     *
     * @param movies must not be {@literal null}. Must not contain
     * {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or
     * one of its entities is {@literal null}.
     * @throws OptimisticLockingFailureException when at least one entity uses
     * optimistic locking and has a version attribute with a different value
     * from that found in the persistence store. Also thrown if at least one
     * entity is assumed to be present but does not exist in the database.
     */
    void deleteAll(List<T> movies);

    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();

}
