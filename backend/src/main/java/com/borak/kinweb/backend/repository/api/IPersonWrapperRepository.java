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
public interface IPersonWrapperRepository<PW, ID> extends IRepository<PW, ID> {

    public List<PW> findAllWithRelationsPaginated(int page, int size) throws DatabaseException, IllegalArgumentException;

    public Optional<PW> findByIdWithRelations(ID id) throws DatabaseException, IllegalArgumentException;
}
