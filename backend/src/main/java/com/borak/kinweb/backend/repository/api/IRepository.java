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
 * @param <T> entity
 * @param <ID> ID of entity
 */
public interface IRepository<T, ID> {

    T save(T entity) throws DatabaseException;

    List<T> saveAll(List<T> entities)throws DatabaseException;

    T insert(T entity)throws DatabaseException;

    List<T> insertAll(List<T> entities)throws DatabaseException;

    T update(T entity)throws DatabaseException;

    List<T> updateAll(List<T> entities)throws DatabaseException;

    Optional<T> findById(ID id)throws DatabaseException;

    boolean existsById(ID id)throws DatabaseException;

    List<T> findAll()throws DatabaseException;

    List<T> findAllById(List<ID> ids)throws DatabaseException;
    
    List<T> findAllPaginated(int page,int size)throws DatabaseException;

    long count()throws DatabaseException;

    void deleteById(ID id)throws DatabaseException;

    void delete(T entity)throws DatabaseException;

    void deleteAllById(List<ID> ids)throws DatabaseException;

    void deleteAll(List<T> entities)throws DatabaseException;

    void deleteAll()throws DatabaseException;
}
