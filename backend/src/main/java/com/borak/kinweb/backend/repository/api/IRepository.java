/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;

import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 * Top level repository interface for all other custom interfaces of domain
 * model
 *
 * @author Mr. Poyo
 * @param <T> Object representing database entity
 * @param <ID> ID of an object that represents database entity
 */
public interface IRepository<T, ID> {

    T insert(T entity) throws DatabaseException, IllegalArgumentException;

    void update(T entity) throws DatabaseException, IllegalArgumentException;

    Optional<T> findById(ID id) throws DatabaseException, IllegalArgumentException;

    boolean existsById(ID id) throws DatabaseException, IllegalArgumentException;

    List<T> findAll() throws DatabaseException;

    List<T> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException;

    long count() throws DatabaseException;

    void deleteById(ID id) throws DatabaseException, IllegalArgumentException;

}
