/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.UserJPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface UserRepositoryJPA extends JpaRepository<UserJPA, Long> {

    Optional<UserJPA> findByUsername(String username);

    boolean existsByProfileName(String profileName);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
