/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.critique;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 * @param <C> critique request
 * @param <CID> critique id
 * @param <CM> comment request
 * @param <CMID> comment id
 */
public interface ICritiqueService<C, CID, CM, CMID> {

    //=================================================================================================================================
    //POST
    ResponseEntity postCritique(C critiqueRequest);

    ResponseEntity postCritiqueLike(CID critiqueID);

    ResponseEntity postCritiqueDislike(CID critiqueID);

    ResponseEntity postCritiqueComment(CM commentRequest);

    ResponseEntity postCritiqueCommentLike(CID critiqueID, CMID commentID);

    ResponseEntity postCritiqueCommentDislike(CID critiqueID, CMID commentID);

    //=================================================================================================================================
    //PUT
    ResponseEntity putCritiqueLike(CID critiqueID);

    ResponseEntity putCritiqueDislike(CID critiqueID);

    ResponseEntity putCritiqueCommentLike(CID critiqueID, CMID commentID);

    ResponseEntity putCritiqueCommentDislike(CID critiqueID, CMID commentID);

    //=================================================================================================================================
    //DELETE
    ResponseEntity deleteCritique(CID critiqueID);

    ResponseEntity deleteCritiqueLike(CID critiqueID);

    ResponseEntity deleteCritiqueDislike(CID critiqueID);

    ResponseEntity deleteCritiqueComment(CID critiqueID, CMID commentID);

    ResponseEntity deleteCritiqueCommentLike(CID critiqueID, CMID commentID);

    ResponseEntity deleteCritiqueCommentDislike(CID critiqueID, CMID commentID);

}
