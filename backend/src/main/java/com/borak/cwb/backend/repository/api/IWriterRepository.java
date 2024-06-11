/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.api;

import com.borak.cwb.backend.exceptions.DatabaseException;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public interface IWriterRepository<W, ID> extends IRepository<W, ID> {

    public List<W> findAllByMediaId(ID id) throws DatabaseException, IllegalArgumentException;

}
