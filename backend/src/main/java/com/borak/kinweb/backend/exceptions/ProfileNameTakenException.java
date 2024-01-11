/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.exceptions;

/**
 *
 * @author Mr. Poyo
 */
public class ProfileNameTakenException extends RuntimeException{

    public ProfileNameTakenException() {
    }

    public ProfileNameTakenException(String message) {
        super(message);
    }

    public ProfileNameTakenException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
