/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.user;

import com.borak.cwb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "commentId",
    "like"
})
public class UserCommentLikeDislikesDTO implements DTO {

    @JsonProperty(value = "comment_id")
    private Long commentId;
    private Boolean like;

    public UserCommentLikeDislikesDTO() {
    }

    public UserCommentLikeDislikesDTO(Long commentId, Boolean like) {
        this.commentId = commentId;
        this.like = like;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

}
