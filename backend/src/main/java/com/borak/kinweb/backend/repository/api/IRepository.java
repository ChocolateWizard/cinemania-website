/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;


import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Top level repository interface for all other custom interfaces of domain
 * model
 *
 * @author Mr. Poyo
 */
public interface IRepository<T, ID> {

    /**
     * Saves a given entity. Use the returned instance for further operations as
     * the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is
     * {@literal null}.
     */
    T save(T entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null} nor must it contain
     * {@literal null}.
     * @return the saved entities; will never be {@literal null}. The returned
     * {@literal Iterable} will have the same size as the {@literal Iterable}
     * passed as an argument.
     * @throws IllegalArgumentException in case the given
     * {@link Iterable entities} or one of its entities is {@literal null}.
     */
    List<T> saveAll(List<T> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if
     * none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    Optional<T> findById(ID id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists,
     * {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    boolean existsById(ID id);

    /**
     * Returns all instances of the type.
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
     */
    void delete(T entity);

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     * <p>
     * Entities that aren't found in the persistence store are silently ignored.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null}
     * elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one
     * of its elements is {@literal null}.
     */
    void deleteAllById(List<ID> ids);

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain
     * {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or
     * one of its entities is {@literal null}.
     */
    void deleteAll(List<T> entities);

    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();
}
