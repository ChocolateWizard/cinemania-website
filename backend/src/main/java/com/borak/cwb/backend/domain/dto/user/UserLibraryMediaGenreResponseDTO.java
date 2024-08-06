/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.dto.user;

import com.borak.cwb.backend.domain.dto.DTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "id",
    "name"
})
public class UserLibraryMediaGenreResponseDTO implements DTO {

    private Long id;
    private String name;

    public UserLibraryMediaGenreResponseDTO() {
    }

    public UserLibraryMediaGenreResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
