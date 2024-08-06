/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.media;

import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.enums.SortOption;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface IMediaService {

    ResponseEntity getAllMediasByTitleWithGenresPaginated(int page, int size, String title,
            List<Long> genreIds,
            SortOption sortByAudienceRating, SortOption sortByReleaseDate, Integer releaseYear, MediaType mediaType);

}
