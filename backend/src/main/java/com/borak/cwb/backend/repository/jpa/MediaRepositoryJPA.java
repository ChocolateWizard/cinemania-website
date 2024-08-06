/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.repository.jpa;

import com.borak.cwb.backend.domain.jpa.MediaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public interface MediaRepositoryJPA extends JpaRepository<MediaJPA, Long>, ListPagingAndSortingRepository<MediaJPA, Long>, JpaSpecificationExecutor<MediaJPA> {

    Page<MediaJPA> findByTitleContaining(String title, Pageable pageable);

}
