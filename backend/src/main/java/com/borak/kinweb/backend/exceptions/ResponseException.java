/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.exceptions;

import com.borak.kinweb.backend.domain.enums.ExceptionType;

/**
 *
 * @author Mr. Poyo
 */
public class ResponseException extends Exception {

    private ExceptionType exceptionType;
    private String message;
    private Throwable cause;

    public ResponseException(ExceptionType exceptionType, String message) {
        this.exceptionType = exceptionType;
        this.message = message;
    }

    public ResponseException(ExceptionType exceptionType, String message, Throwable cause) {
        this.exceptionType = exceptionType;
        this.message = message;
        this.cause = cause;
    }

    @Override
    public String toString() {
        return exceptionType + ": " + message;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
    

}
