/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.country;

import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import com.borak.kinweb.backend.logic.transformers.CountryTransformer;
import com.borak.kinweb.backend.repository.intf.ICountryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mr. Poyo
 */
@Service
public class CountryService implements ICountryService{
    
    @Override
    public List<CountryDTO> getAllCountries() {
throw new UnsupportedOperationException("Not supported");
    }
    
    
}
