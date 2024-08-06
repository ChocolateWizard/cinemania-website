/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface TVShowRepositoryJPA extends JpaRepository<TVShowJPA, Long>, JpaSpecificationExecutor<TVShowJPA> {

    @Query("SELECT t FROM TVShow t WHERE t.audienceRating >= :rating ORDER BY t.audienceRating DESC")
    List<TVShowJPA> findAllByAudienceRatingGreaterThanEqual(int rating, Pageable pageable);

    @Query("SELECT t FROM TVShow t WHERE YEAR(t.releaseDate) >= :year ORDER BY t.releaseDate ASC")
    List<TVShowJPA> findAllByReleaseDateYearGreaterThanEqual(int year, Pageable pageable);

    //This method shouldn't make unnecessary count query to database
    List<TVShowJPA> findAllByOrderByIdAsc(Pageable page);

}
