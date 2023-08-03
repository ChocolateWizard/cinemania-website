/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.intf;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Mr. Poyo
 */
//Basic CRUD operations
public interface IRepository<T,ID> {
    T save(T entity);

    List<T> saveAll(List<T> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    List<T> findAllById(List<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(List<ID> ids);

    void deleteAll(List<T> entities);

    void deleteAll();
}
