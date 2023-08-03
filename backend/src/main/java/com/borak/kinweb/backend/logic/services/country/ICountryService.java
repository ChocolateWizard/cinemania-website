/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.country;

import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public interface ICountryService {
    
    public List<CountryDTO> getAllCountries();
    
}
