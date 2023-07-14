/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.country;

import com.borak.kinweb.backend.exceptions.ResponseException;
import com.borak.kinweb.backend.exceptions.ExceptionHandler;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public abstract class TemplateCountryService<T, PK> implements ICountryService<PK> {

    @Override
    public ResponseEntity getAllCountries() {
        try {
            List<T> countries = getAllCountriesImpl();
            ResponseEntity response = new ResponseEntity(countries, HttpStatus.OK);
            return response;
        } catch (ResponseException ex) {
            return ExceptionHandler.toResponseEntity(ex);
        }
    }

    public abstract List<T> getAllCountriesImpl() throws ResponseException;

}
