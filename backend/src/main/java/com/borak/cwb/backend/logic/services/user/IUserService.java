/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.user;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface IUserService {

    ResponseEntity postMediaIntoLibrary(long mediaId);

    ResponseEntity deleteMediaFromLibrary(long mediaId);

}
