/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.intf;

import com.borak.kinweb.backend.exceptions.ResponseException;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface ICountryRepository<E,PK>{

  
    public List<E> getAll() throws ResponseException;


}
