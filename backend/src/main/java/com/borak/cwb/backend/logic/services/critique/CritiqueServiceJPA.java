/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.critique;

import com.borak.cwb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.domain.dto.critique.CritiqueCommentRequestDTO;
import com.borak.cwb.backend.domain.dto.critique.CritiqueCommentResponseDTO;
import com.borak.cwb.backend.domain.jpa.CommentJPA;
import com.borak.cwb.backend.domain.jpa.CommentLikeDislikeJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueLikeDislikeJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.exceptions.DuplicateResourceException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.CommentTransformer;
import com.borak.cwb.backend.logic.transformers.CritiqueTransformer;
import com.borak.cwb.backend.repository.jpa.CommentLikeDislikeRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.CommentRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.CritiqueLikeDislikeRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.CritiqueRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.MediaRepositoryJPA;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class CritiqueServiceJPA implements ICritiqueService<CritiqueRequestDTO, Long, CritiqueCommentRequestDTO, Long> {

    @Autowired
    private CritiqueRepositoryJPA critiqueRepo;
    @Autowired
    private CritiqueLikeDislikeRepositoryJPA critiqueLikeDislikeRepo;
    @Autowired
    private CommentRepositoryJPA commentRepo;
    @Autowired
    private CommentLikeDislikeRepositoryJPA commentLikeDislikeRepo;
    @Autowired
    private MediaRepositoryJPA mediaRepo;

    @Autowired
    private CritiqueTransformer critiqueTransformer;
    @Autowired
    private CommentTransformer commentTransformer;

//=================================================================================================================================
//POST
    @Override
    public ResponseEntity postCritique(CritiqueRequestDTO critiqueRequest) {
        if (!mediaRepo.existsById(critiqueRequest.getMediaId())) {
            throw new ResourceNotFoundException("Media with id: " + critiqueRequest.getMediaId() + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueJPA critiqueToSave = critiqueTransformer.toCritiqueJPA(
                critiqueRequest,
                new UserJPA(loggedUser.getId(), loggedUser.getProfileName(), loggedUser.getProfileImage())
        );
        if (critiqueRepo.existsByUserAndMedia(critiqueToSave.getUser(), critiqueToSave.getMedia())) {
            throw new DuplicateResourceException("Duplicate critique for media with id: " + critiqueRequest.getMediaId());
        }
        critiqueToSave = critiqueRepo.save(critiqueToSave);
        return new ResponseEntity(critiqueTransformer.toCritiqueResponse(critiqueToSave), HttpStatus.OK);
    }

    @Override
    public ResponseEntity postCritiqueLike(Long critiqueId) {
        if (critiqueRepo.existsById(critiqueId)) {
            throw new ResourceNotFoundException("Critique with id: " + critiqueId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueLikeDislikeJPA.ID likeDislikeId = new CritiqueLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CritiqueJPA(critiqueId)
        );
        if (critiqueLikeDislikeRepo.existsById(likeDislikeId)) {
            throw new DuplicateResourceException("Users critique like/dislike for critique with id:" + critiqueId + " already present in database");
        }
        CritiqueLikeDislikeJPA likeDislikeToInsert = new CritiqueLikeDislikeJPA(likeDislikeId, true);
        critiqueLikeDislikeRepo.save(likeDislikeToInsert);
        return new ResponseEntity(HttpStatus.OK);

    }

    @Override
    public ResponseEntity postCritiqueDislike(Long critiqueId) {
        if (critiqueRepo.existsById(critiqueId)) {
            throw new ResourceNotFoundException("Critique with id: " + critiqueId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueLikeDislikeJPA.ID likeDislikeId = new CritiqueLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CritiqueJPA(critiqueId)
        );
        if (critiqueLikeDislikeRepo.existsById(likeDislikeId)) {
            throw new DuplicateResourceException("Users critique like/dislike for critique with id:" + critiqueId + " already present in database");
        }
        CritiqueLikeDislikeJPA likeDislikeToInsert = new CritiqueLikeDislikeJPA(likeDislikeId, false);
        critiqueLikeDislikeRepo.save(likeDislikeToInsert);
        return new ResponseEntity(HttpStatus.OK);

    }

    @Override
    public ResponseEntity postCritiqueComment(CritiqueCommentRequestDTO commentRequest) {
        if (!critiqueRepo.existsById(commentRequest.getCritiqueId())) {
            throw new ResourceNotFoundException("Critique with id: " + commentRequest.getCritiqueId() + " does not exist in database");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserJPA user = new UserJPA(loggedUser.getId(), loggedUser.getProfileName(), loggedUser.getProfileImage());
        CritiqueJPA critique = new CritiqueJPA(commentRequest.getCritiqueId());
        if (commentRepo.existsByUserAndCritique(user, critique)) {
            throw new DuplicateResourceException("User already has a comment for critique with id: " + commentRequest.getCritiqueId());
        }
        CommentJPA commentToSave = new CommentJPA(user, critique, commentRequest.getContent(), LocalDateTime.now());
        commentToSave = commentRepo.save(commentToSave);
        return new ResponseEntity(commentTransformer.toCritiqueCommentResponse(commentToSave), HttpStatus.OK);
    }

    @Override
    public ResponseEntity postCritiqueCommentLike(Long critiqueID, Long commentID) {
        CritiqueJPA critique = new CritiqueJPA(critiqueID);
        if (!commentRepo.existsByIdAndCritique(commentID, critique)) {
            throw new ResourceNotFoundException("Comment with id: " + commentID + " does not exist under critique with id: " + critiqueID);
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentLikeDislikeJPA.ID likeDislikeID = new CommentLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CommentJPA(commentID)
        );
        if (commentLikeDislikeRepo.existsById(likeDislikeID)) {
            throw new DuplicateResourceException("Users like/dislike for comment with id: " + commentID + " under critique with id: " + critiqueID + " already present in database");
        }
        CommentLikeDislikeJPA likeDislike = new CommentLikeDislikeJPA(likeDislikeID, true);
        commentLikeDislikeRepo.save(likeDislike);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity postCritiqueCommentDislike(Long critiqueID, Long commentID) {
        CritiqueJPA critique = new CritiqueJPA(critiqueID);
        if (!commentRepo.existsByIdAndCritique(commentID, critique)) {
            throw new ResourceNotFoundException("Comment with id: " + commentID + " does not exist under critique with id: " + critiqueID);
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentLikeDislikeJPA.ID likeDislikeID = new CommentLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CommentJPA(commentID)
        );
        if (commentLikeDislikeRepo.existsById(likeDislikeID)) {
            throw new DuplicateResourceException("Users like/dislike for comment with id: " + commentID + " under critique with id: " + critiqueID + " already present in database");
        }
        CommentLikeDislikeJPA likeDislike = new CommentLikeDislikeJPA(likeDislikeID, false);
        commentLikeDislikeRepo.save(likeDislike);
        return new ResponseEntity(HttpStatus.OK);
    }

//=================================================================================================================================
//PUT  
    @Override
    public ResponseEntity putCritiqueLike(Long critiqueId) {
        if (critiqueRepo.existsById(critiqueId)) {
            throw new ResourceNotFoundException("Critique with id: " + critiqueId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueLikeDislikeJPA.ID likeDislikeId = new CritiqueLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CritiqueJPA(critiqueId)
        );
        if (critiqueLikeDislikeRepo.existsById(likeDislikeId)) {
            CritiqueLikeDislikeJPA likeDislike = new CritiqueLikeDislikeJPA(likeDislikeId, true);
            critiqueLikeDislikeRepo.save(likeDislike);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Critique like/dislike for critique with id: " + critiqueId + " does not exist in database!");
        }
    }

    @Override
    public ResponseEntity putCritiqueDislike(Long critiqueId) {
        if (critiqueRepo.existsById(critiqueId)) {
            throw new ResourceNotFoundException("Critique with id: " + critiqueId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueLikeDislikeJPA.ID likeDislikeId = new CritiqueLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CritiqueJPA(critiqueId)
        );
        if (critiqueLikeDislikeRepo.existsById(likeDislikeId)) {
            CritiqueLikeDislikeJPA likeDislike = new CritiqueLikeDislikeJPA(likeDislikeId, false);
            critiqueLikeDislikeRepo.save(likeDislike);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Critique like/dislike for critique with id: " + critiqueId + " does not exist in database!");
        }
    }

    @Override
    public ResponseEntity putCritiqueCommentLike(Long critiqueID, Long commentID) {
        CritiqueJPA critique = new CritiqueJPA(critiqueID);
        if (!commentRepo.existsByIdAndCritique(commentID, critique)) {
            throw new ResourceNotFoundException("Comment with id: " + commentID + " does not exist under critique with id: " + critiqueID);
        }

        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentLikeDislikeJPA.ID likeDislikeID = new CommentLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CommentJPA(commentID)
        );
        if (commentLikeDislikeRepo.existsById(likeDislikeID)) {
            CommentLikeDislikeJPA likeDislike = new CommentLikeDislikeJPA(likeDislikeID, true);
            commentLikeDislikeRepo.save(likeDislike);
            return new ResponseEntity(HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Comment like/dislike for comment with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
    }

    @Override
    public ResponseEntity putCritiqueCommentDislike(Long critiqueID, Long commentID) {
        CritiqueJPA critique = new CritiqueJPA(critiqueID);
        if (!commentRepo.existsByIdAndCritique(commentID, critique)) {
            throw new ResourceNotFoundException("Comment with id: " + commentID + " does not exist under critique with id: " + critiqueID);
        }

        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentLikeDislikeJPA.ID likeDislikeID = new CommentLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CommentJPA(commentID)
        );
        if (commentLikeDislikeRepo.existsById(likeDislikeID)) {
            CommentLikeDislikeJPA likeDislike = new CommentLikeDislikeJPA(likeDislikeID, false);
            commentLikeDislikeRepo.save(likeDislike);
            return new ResponseEntity(HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Comment like/dislike for comment with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
    }

//=================================================================================================================================
//DELETE
    @Override
    public ResponseEntity deleteCritique(Long id) {
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CritiqueJPA> critiqueDB = critiqueRepo.findById(id);
        if (critiqueDB.isEmpty() || !Objects.equals(critiqueDB.get().getUser().getId(), loggedUser.getId())) {
            throw new ResourceNotFoundException("Users critique with id: " + id + " does not exist in database!");
        }
        critiqueRepo.deleteById(id);
        return new ResponseEntity(critiqueTransformer.toCritiqueResponse(critiqueDB.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteCritiqueLike(Long critiqueId) {
        if (critiqueRepo.existsById(critiqueId)) {
            throw new ResourceNotFoundException("Critique with id: " + critiqueId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueLikeDislikeJPA.ID likeDislikeId = new CritiqueLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CritiqueJPA(critiqueId)
        );
        Optional<CritiqueLikeDislikeJPA> critLikeDislike = critiqueLikeDislikeRepo.findById(likeDislikeId);
        if (critLikeDislike.isEmpty()) {
            throw new ResourceNotFoundException("Users critique like/dislike for critique with id: " + critiqueId + " does not exist in database!");
        }
        if (critLikeDislike.get().getIsLike() == false) {
            throw new ResourceNotFoundException("Users critique like for critique with id: " + critiqueId + " does not exist in database!");
        }
        critiqueLikeDislikeRepo.deleteById(likeDislikeId);
        return new ResponseEntity(HttpStatus.OK);

    }

    @Override
    public ResponseEntity deleteCritiqueDislike(Long critiqueId) {
        if (critiqueRepo.existsById(critiqueId)) {
            throw new ResourceNotFoundException("Critique with id: " + critiqueId + " does not exist in database!");
        }
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CritiqueLikeDislikeJPA.ID likeDislikeId = new CritiqueLikeDislikeJPA.ID(
                new UserJPA(loggedUser.getId()),
                new CritiqueJPA(critiqueId)
        );
        Optional<CritiqueLikeDislikeJPA> critLikeDislike = critiqueLikeDislikeRepo.findById(likeDislikeId);
        if (critLikeDislike.isEmpty()) {
            throw new ResourceNotFoundException("Users critique like/dislike for critique with id: " + critiqueId + " does not exist in database!");

        }
        if (critLikeDislike.get().getIsLike()) {
            throw new ResourceNotFoundException("Users critique dislike for critique with id: " + critiqueId + " does not exist in database!");
        }
        critiqueLikeDislikeRepo.deleteById(likeDislikeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteCritiqueComment(Long critiqueID, Long commentID) {
        SecurityUser loggedUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserJPA user = new UserJPA(loggedUser.getId(), loggedUser.getProfileName(), loggedUser.getProfileImage());
        Optional<CommentJPA> commentDB = commentRepo.findByIdAndUserAndCritique(commentID, user, new CritiqueJPA(critiqueID));
        if (commentDB.isEmpty()) {
            throw new ResourceNotFoundException("Users comment with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
        }
        CritiqueCommentResponseDTO response = commentTransformer.toCritiqueCommentResponse(commentDB.get());
        commentRepo.deleteById(commentID);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteCritiqueCommentLike(Long critiqueID, Long commentID) {
        Long userID = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        CommentLikeDislikeJPA.ID likeDislikeID = new CommentLikeDislikeJPA.ID(new UserJPA(userID), new CommentJPA(commentID));
        Optional<CommentLikeDislikeJPA> likeDislike = commentLikeDislikeRepo.findById(likeDislikeID);
        if (likeDislike.isEmpty()) {
            throw new ResourceNotFoundException("Users comment like/dislike with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
        }
        if (likeDislike.get().getIsLike() == false) {
            throw new ResourceNotFoundException("Users comment like with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
        }
        commentLikeDislikeRepo.deleteById(likeDislikeID);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteCritiqueCommentDislike(Long critiqueID, Long commentID) {
        Long userID = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        CommentLikeDislikeJPA.ID likeDislikeID = new CommentLikeDislikeJPA.ID(new UserJPA(userID), new CommentJPA(commentID));
        Optional<CommentLikeDislikeJPA> likeDislike = commentLikeDislikeRepo.findById(likeDislikeID);
        if (likeDislike.isEmpty()) {
            throw new ResourceNotFoundException("Users comment like/dislike with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
        }
        if (likeDislike.get().getIsLike()) {
            throw new ResourceNotFoundException("Users comment dislike with id: " + commentID + " under critique with id: " + critiqueID + " does not exist in database");
        }
        commentLikeDislikeRepo.deleteById(likeDislikeID);
        return new ResponseEntity(HttpStatus.OK);
    }

}
