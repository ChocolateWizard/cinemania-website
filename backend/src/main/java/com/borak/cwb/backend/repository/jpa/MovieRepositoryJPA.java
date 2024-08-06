/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.MovieJPA;
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
public interface MovieRepositoryJPA extends JpaRepository<MovieJPA, Long>, JpaSpecificationExecutor<MovieJPA> {

    /*
    Slice can be set here instead of Page as return type due to performance reasons 
    as Page makes another call to see total number of records and pages, but i can set List
    for both performance and conveniece reasons as it neggates any count queries being executed 
    unintentionally
     */
    @Query("SELECT m FROM Movie m WHERE m.audienceRating >= :rating ORDER BY m.audienceRating DESC")
    List<MovieJPA> findAllByAudienceRatingGreaterThanEqual(int rating, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE YEAR(m.releaseDate) >= :year ORDER BY m.releaseDate ASC")
    List<MovieJPA> findAllByReleaseDateYearGreaterThanEqual(int year, Pageable pageable);

    //This method shouldn't make unnecessary count query to database
    List<MovieJPA> findAllByOrderByIdAsc(Pageable page);
    
}
