/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.media;

import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.logic.transformers.MediaTransformer;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class MediaServiceJPA implements IMediaService {

    @Autowired
    private MediaRepositoryJPA mediaRepo;

    @Autowired
    private MediaTransformer mediaTransformer;

    @Override
    public ResponseEntity getAllMediasByTitleWithGenresPaginated(int page, int size, String title) {
        Pageable p = PageRequest.of(page - 1, size);
        Page<MediaJPA> medias = mediaRepo.findByTitleContaining(title, p);
        return new ResponseEntity(mediaTransformer.jpaToMediaResponse(medias.getContent()), HttpStatus.OK);
    }

}
