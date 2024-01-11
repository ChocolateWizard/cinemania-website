/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Mr. Poyo
 */
public interface IUserService<RF,LF>{

    ResponseEntity register(RF registerForm);

    ResponseEntity login(LF loginForm);
    
    ResponseEntity logout();
}
