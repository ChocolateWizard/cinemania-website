/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.exceptions;

import java.util.Arrays;

/**
 *
 * @author Mr. Poyo
 */
public class ValidationException extends RuntimeException {

    private String[] messages;

    public ValidationException() {
    }

    public ValidationException(String[] messages) {
        super(Arrays.toString(messages));
        this.messages = messages;
    }

    public ValidationException(String message) {
        super(message);
        this.messages = new String[]{message};
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.messages = new String[]{message};
    }

    public String[] getMessages() {
        return messages;
    }

}
