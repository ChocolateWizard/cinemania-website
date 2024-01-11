/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.user;

import com.borak.kinweb.backend.domain.dto.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Mr. Poyo
 */
public class UserLoginDTO implements DTO {

    @NotBlank(message = "Username not provided!")
    @Size(max = 300, message = "Invalid username!")
    private String username;

    @NotBlank(message = "Password not provided!")
    @Size(max = 300, message = "Invalid password!")
    private String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
