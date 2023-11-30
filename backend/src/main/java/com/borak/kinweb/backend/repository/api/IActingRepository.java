/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.repository.api;

import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public interface IActingRepository<AT, ID> extends IRepository<AT, ID> {

    public List<AT> findAllByMediaId(ID id) throws DatabaseException;

    public List<AT> insertAll(List<AT> entities) throws DatabaseException;

}
