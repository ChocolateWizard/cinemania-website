/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.controllers;

import com.borak.cwb.backend.domain.dto.critique.CritiqueCommentRequestDTO;
import com.borak.cwb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.cwb.backend.logic.services.critique.ICritiqueService;
import com.borak.cwb.backend.logic.services.validation.DomainValidationService;
import com.borak.cwb.backend.logic.transformers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "api/critiques")
@Validated
public class CritiqueController {

    private final ICritiqueService critiqueService;
    private final DomainValidationService domainValidator;

    @Autowired
    public CritiqueController(ICritiqueService critiqueService, DomainValidationService domainValidator) {
        this.critiqueService = critiqueService;
        this.domainValidator = domainValidator;
    }

//=================================================================================================================================
//GET    
//=================================================================================================================================
//POST
    @PostMapping
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity postCritique(@RequestBody CritiqueRequestDTO critiqueRequest) {
        domainValidator.validate(critiqueRequest, RequestMethod.POST);
        return critiqueService.postCritique(critiqueRequest);
    }

    @PostMapping(path = "/{critiqueID}/likes")
    public ResponseEntity postCritiqueLike(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueID) {
        return critiqueService.postCritiqueLike(critiqueID);
    }

    @PostMapping(path = "/{id}/dislikes")
    public ResponseEntity postCritiqueDislike(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long id) {
        return critiqueService.postCritiqueDislike(id);
    }

    @PostMapping(path = "/{id}/comments")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity postCritiqueComment(@RequestBody CritiqueCommentRequestDTO commentRequest) {
        domainValidator.validate(commentRequest);
        return critiqueService.postCritiqueComment(commentRequest);
    }

    @PostMapping(path = "/{critiqueId}/comments/{commentId}/likes")
    public ResponseEntity postCritiqueCommentLike(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.postCritiqueCommentLike(critiqueId, commentId);
    }

    @PostMapping(path = "/{critiqueId}/comments/{commentId}/dislikes")
    public ResponseEntity postCritiqueCommentDislike(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.postCritiqueCommentDislike(critiqueId, commentId);
    }

//=================================================================================================================================
//PUT
    @PutMapping(path = "/{id}/likes")
    public ResponseEntity putCritiqueLike(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long id) {
        return critiqueService.putCritiqueLike(id);
    }

    @PutMapping(path = "/{id}/dislikes")
    public ResponseEntity putCritiqueDislike(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long id) {
        return critiqueService.putCritiqueDislike(id);
    }

    @PutMapping(path = "/{critiqueId}/comments/{commentId}/likes")
    public ResponseEntity putCritiqueCommentLike(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.putCritiqueCommentLike(critiqueId, commentId);
    }

    @PutMapping(path = "/{critiqueId}/comments/{commentId}/dislikes")
    public ResponseEntity putCritiqueCommentDislike(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.putCritiqueCommentDislike(critiqueId, commentId);
    }

//=================================================================================================================================
//DELETE
    @DeleteMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity deleteCritique(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long id) {
        return critiqueService.deleteCritique(id);
    }

    @DeleteMapping(path = "/{id}/likes")
    public ResponseEntity deleteCritiqueLike(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long id) {
        return critiqueService.deleteCritiqueLike(id);
    }

    @DeleteMapping(path = "/{id}/dislikes")
    public ResponseEntity deleteCritiqueDislike(@PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long id) {
        return critiqueService.deleteCritiqueDislike(id);
    }

    @DeleteMapping(path = "/{critiqueId}/comments/{commentId}")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity deleteCritiqueComment(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.deleteCritiqueComment(critiqueId, commentId);
    }

    @DeleteMapping(path = "/{critiqueId}/comments/{commentId}/likes")
    public ResponseEntity deleteCritiqueCommentLike(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.deleteCritiqueCommentLike(critiqueId, commentId);
    }

    @DeleteMapping(path = "/{critiqueId}/comments/{commentId}/dislikes")
    public ResponseEntity deleteCritiqueCommentDislike(
            @PathVariable @Min(value = 1, message = "Critique id must be greater than or equal to 1") long critiqueId,
            @PathVariable @Min(value = 1, message = "Comment id must be greater than or equal to 1") long commentId
    ) {
        return critiqueService.deleteCritiqueCommentDislike(critiqueId, commentId);
    }

}
