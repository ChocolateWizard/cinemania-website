/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.country;

import com.borak.cwb.backend.domain.jpa.CountryJPA;
import com.borak.cwb.backend.logic.transformers.CountryTransformer;
import com.borak.cwb.backend.repository.jpa.CountryRepositoryJPA;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class CountryServiceJPA implements ICountryService {

    private final CountryTransformer countryTransformer;
    private final CountryRepositoryJPA countryRepo;

    @Autowired
    public CountryServiceJPA(CountryTransformer countryTransformer, CountryRepositoryJPA countryRepo) {
        this.countryTransformer = countryTransformer;
        this.countryRepo = countryRepo;
    }

//=================================================================================================================================    
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public ResponseEntity getAll() {
        List<CountryJPA> countries = countryRepo.findAll();
        return new ResponseEntity(countryTransformer.jpaToCountryResponse(countries), HttpStatus.OK);
    }

}
