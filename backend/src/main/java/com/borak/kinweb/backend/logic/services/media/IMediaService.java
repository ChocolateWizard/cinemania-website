/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.media;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface IMediaService {

    public ResponseEntity getAllMediasByTitleWithGenresPaginated(int page, int size, String title);

}
