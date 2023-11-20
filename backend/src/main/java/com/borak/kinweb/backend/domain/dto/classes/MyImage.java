/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import java.io.File;
import java.io.IOException;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
public class MyImage {

    public static final String[] VALID_EXTENSIONS = {"png", "jpg", "jpeg"};
    public static final long IMAGE_MAX_SIZE = 8388608L;

    private String name;
    private final String extension;
    private String fullName;
    private final byte[] bytes;

    public MyImage(MultipartFile file) throws IllegalArgumentException {
        try {
            int index = file.getOriginalFilename().lastIndexOf(".");
            this.extension = file.getOriginalFilename().substring(index + 1).trim();
            this.name = file.getOriginalFilename().substring(0, index);
            this.fullName = this.name + "." + this.extension;
            this.bytes = file.getBytes();
        } catch (NullPointerException | IOException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Unable to create image!");
        }
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getFullName() {
        return fullName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setName(String name) {
        this.name = name;
        this.fullName = this.name + "." + this.extension;
    }

}
