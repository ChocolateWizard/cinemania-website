/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.api;

import com.borak.cwb.backend.exceptions.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 * Interface of User entity repositories.
 *
 * @author Mr. Poyo
 * @param <U> User entity class
 * @param <UID> Identifier of User entity
 * @param <M> Media entity class
 * @param <MID> Identifier of Media entity
 */
public interface IUserRepository<U, UID, M, MID> extends IRepository<U, UID> {

    boolean existsEmail(String email) throws DatabaseException, IllegalArgumentException;

    boolean existsUsername(String username) throws DatabaseException, IllegalArgumentException;

    boolean existsProfileName(String profileName) throws DatabaseException, IllegalArgumentException;

    Optional<U> findByUsername(String username) throws DatabaseException, IllegalArgumentException;

    Optional<U> findByIdWithRelations(UID id) throws DatabaseException, IllegalArgumentException;

    boolean existsMediaInLibrary(UID userId, MID mediaId) throws DatabaseException, IllegalArgumentException;

    void addMediaToLibrary(UID userId, MID mediaId) throws DatabaseException, IllegalArgumentException;

    void removeMediaFromLibrary(UID userId, MID mediaId) throws DatabaseException, IllegalArgumentException;

    List<M> findAllLibraryMediaByUserId(UID userId) throws DatabaseException, IllegalArgumentException;

}
