/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers.repositories;

import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional(readOnly = true)
public class MediaTestRepository {

    private final MediaRepositoryJPA repo;

    @Autowired
    public MediaTestRepository(MediaRepositoryJPA repo) {
        this.repo = repo;
    }

    public Long[] findAllMediaIds() {
        List<MediaJPA> medias = repo.findAll();
        int n = medias.size();
        int i = 0;
        Long[] array = new Long[n];
        for (MediaJPA media : medias) {
            array[i++] = media.getId();
        }
        return array;
    }
    

}
