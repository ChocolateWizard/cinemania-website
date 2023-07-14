/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.country;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface ICountryService<PK> {
    
    
    public ResponseEntity getAllCountries();
//    public ResponseEntity getCountryById(PK id);
//    public ResponseEntity storeCountry(RequestEntity request);
//    public ResponseEntity updateCountry(RequestEntity request);
//    public ResponseEntity deleteCountry(RequestEntity request);
    
}
