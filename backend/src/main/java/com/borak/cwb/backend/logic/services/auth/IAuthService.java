/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.auth;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 * @param <RF> register form object
 * @param <LF> login form object
 */
public interface IAuthService<RF, LF> {

    ResponseEntity register(RF registerForm);

    ResponseEntity login(LF loginForm);

    ResponseEntity logout();
}
