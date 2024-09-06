/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.security;

import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.repository.api.IUserRepository;
import com.borak.cwb.backend.repository.jpa.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

//    @Autowired
//    private IUserRepository<UserJDBC, Long, MediaJDBC, Long> userRepo;
    @Autowired
    private UserRepositoryJPA userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserJDBC userDB = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        UserJPA userDB = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new SecurityUser(userDB);
    }

}
