/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.CommentJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface CommentRepositoryJPA extends JpaRepository<CommentJPA, Long> {

    boolean existsByUserAndCritique(UserJPA user, CritiqueJPA critique);

    boolean existsByIdAndCritique(Long id, CritiqueJPA critique);

    Optional<CommentJPA> findByIdAndUserAndCritique(Long id, UserJPA user, CritiqueJPA critique);

}
