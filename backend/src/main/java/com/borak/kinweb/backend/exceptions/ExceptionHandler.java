/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public final class ExceptionHandler {

    public static ResponseEntity toResponseEntity(ResponseException ex) {
        switch (ex.getExceptionType()) {
            case GET_ALL_COUNTRIES_ERROR:
                return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
