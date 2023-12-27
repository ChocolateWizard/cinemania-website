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
public interface IMediaRepository<M, ID> extends IRepository<M, ID> {

    public List<M> findAllByTitleWithGenresPaginated(int page, int size, String title) throws DatabaseException, IllegalArgumentException;

}
