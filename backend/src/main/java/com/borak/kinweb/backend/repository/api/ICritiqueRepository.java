/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;

import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.util.Optional;

/**
 *
 * @author Mr. Poyo
 * @param <C> Critique entity class
 * @param <ID> ID of Critique entity class
 */
public interface ICritiqueRepository<C, ID> extends IRepository<C, ID> {

    boolean exists(C entity) throws DatabaseException, IllegalArgumentException;

    void delete(C entity) throws DatabaseException, IllegalArgumentException;

    Optional<C> find(C entity) throws DatabaseException, IllegalArgumentException;
}
