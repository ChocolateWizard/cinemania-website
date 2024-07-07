/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.MovieJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface MovieRepositoryJPA extends JpaRepository<MovieJPA, Long>, PagingAndSortingRepository<MovieJPA, Long>, JpaSpecificationExecutor<MovieJPA> {

    @Query("SELECT m FROM Movie m WHERE m.audienceRating >= :rating ORDER BY m.audienceRating DESC")
    Page<MovieJPA> findAllByAudienceRatingGreaterThanEqual(int rating, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE YEAR(m.releaseDate) >= :year")
    Page<MovieJPA> findAllByReleaseDateYearGreaterThanEqual(int year, Pageable pageable);

}
