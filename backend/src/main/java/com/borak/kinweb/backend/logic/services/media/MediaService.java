/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.media;

import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.logic.transformers.MediaTransformer;
import com.borak.kinweb.backend.repository.api.IMediaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MediaService implements IMediaService {

    @Autowired
    private IMediaRepository<MediaJDBC, Long> mediaRepo;

    @Autowired
    private MediaTransformer mediaTransformer;

    @Override
    public ResponseEntity getAllMediasByTitleWithGenresPaginated(int page, int size, String title) {
//        title = title.replace("%", "\\%").replace("_", "\\_");
        List<MediaJDBC> medias = mediaRepo.findAllByTitleWithGenresPaginated(page, size, title);
        return new ResponseEntity(mediaTransformer.toMediaResponseDTO(medias), HttpStatus.OK);
    }

}
