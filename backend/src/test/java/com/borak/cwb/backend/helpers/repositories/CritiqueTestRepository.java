/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers.repositories;

import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.repository.jpa.CritiqueRepositoryJPA;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class CritiqueTestRepository {

    private final CritiqueRepositoryJPA repo;

    @Autowired
    public CritiqueTestRepository(CritiqueRepositoryJPA repo) {
        this.repo = repo;
    }

    public Optional<CritiqueJPA> find(CritiqueJPA critique) {
        return repo.findById(critique.getId());
    }

    public boolean exists(CritiqueJPA critique) {
        return repo.existsById(critique.getId());
    }

}
