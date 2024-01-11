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
 */
public interface IUserRepository<U, ID> extends IRepository<U, ID> {

    Optional<U> findByUsername(String username) throws DatabaseException, IllegalArgumentException;

    boolean existsUsername(String username) throws DatabaseException, IllegalArgumentException;

    boolean existsEmail(String email) throws DatabaseException, IllegalArgumentException;

    boolean existsProfileName(String profileName) throws DatabaseException, IllegalArgumentException;

    Optional<U> findByIdWithRelations(ID id) throws DatabaseException, IllegalArgumentException;

    void addMediaToLibrary(U user) throws DatabaseException, IllegalArgumentException;

    void removeMediaFromLibrary(U user) throws DatabaseException, IllegalArgumentException;

    boolean existsMediaInLibrary(U user) throws DatabaseException, IllegalArgumentException;

}
