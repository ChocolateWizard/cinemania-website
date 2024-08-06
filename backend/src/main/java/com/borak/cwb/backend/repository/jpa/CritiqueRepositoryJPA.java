/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface CritiqueRepositoryJPA extends JpaRepository<CritiqueJPA, Long> {

    public boolean existsByUserAndMedia(UserJPA user, MediaJPA media);
    
    @Modifying
    @Query("DELETE FROM Critique c WHERE c.user = :user AND c.media = :media")
    public void deleteByUserAndMedia(UserJPA user, MediaJPA media);
    
    @Query("SELECT c FROM Critique c WHERE c.user = :user AND c.media = :media")
    public Optional<CritiqueJPA> findByUserAndMedia(UserJPA user, MediaJPA media);

}
