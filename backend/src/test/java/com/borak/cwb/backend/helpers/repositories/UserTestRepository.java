/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers.repositories;

import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.repository.jpa.UserRepositoryJPA;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional(readOnly = true)
public class UserTestRepository {

    private final UserRepositoryJPA repo;

    @Autowired
    public UserTestRepository(UserRepositoryJPA userRepo) {
        this.repo = userRepo;
    }

    public List<UserJPA> findAll() {
        List<UserJPA> users = repo.findAll();
        //initialize lazy attributes
        for (UserJPA u : users) {
            u.getCountry();
            u.getMedias().size();
            u.getCritiques().size();
        }
        return users;
    }

    public String[] findAllUsernames() {
        List<UserJPA> users = repo.findAll();
        int n = users.size();
        String[] usernames = new String[n];
        for (int i = 0; i < n; i++) {
            usernames[i] = users.get(i).getUsername();
        }
        return usernames;
    }

    public Optional<UserJPA> findByUsername(String username) {
        Optional<UserJPA> user = repo.findByUsername(username);
        //initialize lazy attributes
        if (user.isPresent()) {
            user.get().getCountry();
            user.get().getMedias().size();
            user.get().getCritiques().size();
        }
        return user;
    }

    public Optional<UserJPA> findById(Long id) {
        Optional<UserJPA> user = repo.findById(id);
        //initialize lazy attributes
        if (user.isPresent()) {
            user.get().getCountry();
            user.get().getMedias().size();
            user.get().getCritiques().size();
        }
        return user;
    }

    public boolean existsMediaInLibrary(Long userId, Long mediaId) {
        UserJPA user = repo.findById(userId).get();
        for (MediaJPA media : user.getMedias()) {
            if (media.getId().equals(mediaId)) {
                return true;
            }
        }
        return false;
    }
    
    

}
