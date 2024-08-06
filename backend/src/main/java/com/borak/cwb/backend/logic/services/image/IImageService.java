/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.image;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author User
 */
public interface IImageService {

    ResponseEntity getMediaImage(String filename);

    ResponseEntity getPersonImage(String filename);

    ResponseEntity getUserImage(String filename);

}
