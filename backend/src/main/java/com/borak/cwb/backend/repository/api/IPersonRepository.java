/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.api;

import com.borak.cwb.backend.exceptions.DatabaseException;
import java.util.Optional;

/**
 *
 * @author Mr. Poyo
 */
public interface IPersonRepository<P, ID> extends IRepository<P, ID> {

    public void updateProfilePhoto(ID id, String profilePhoto) throws DatabaseException, IllegalArgumentException;

    public Optional<String> findByIdProfilePhoto(ID id) throws DatabaseException, IllegalArgumentException;
}
