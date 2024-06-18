/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.GenreJPA;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Mr. Poyo
 */
public interface GenreRepositoryJPA extends JpaRepository<GenreJPA, Long>{
    
}
