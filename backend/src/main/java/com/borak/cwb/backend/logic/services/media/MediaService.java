/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.media;

import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.enums.SortOption;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.logic.transformers.MediaTransformer;
import com.borak.cwb.backend.repository.api.IMediaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
//@Service
//@Transactional
public class MediaService implements IMediaService {

    @Autowired
    private IMediaRepository<MediaJDBC, Long> mediaRepo;

    @Autowired
    private MediaTransformer mediaTransformer;

    @Override
    public ResponseEntity getAllMediasByTitleWithGenresPaginated(int page, int size, String title, List<Long> genreIds, SortOption sortByAudienceRating, SortOption sortByReleaseDate, Integer releaseYear, MediaType mediaType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
