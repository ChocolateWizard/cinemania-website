/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.critique;

import com.borak.cwb.backend.domain.dto.DTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Mr. Poyo
 */
public class CritiqueCommentRequestDTO implements DTO {

    @NotNull
    @Min(value = 1, message = "Critique id must be greater than or equal to 1")
    private Long critiqueId;
    
    @NotBlank
    @Size(max = 300, message = "Comment content size must not be greater than 300")
    private String content;

    public CritiqueCommentRequestDTO() {
    }

    public CritiqueCommentRequestDTO(Long critiqueId, String content) {
        this.critiqueId = critiqueId;
        this.content = content;
    }

    public Long getCritiqueId() {
        return critiqueId;
    }

    public void setCritiqueId(Long critiqueId) {
        this.critiqueId = critiqueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
