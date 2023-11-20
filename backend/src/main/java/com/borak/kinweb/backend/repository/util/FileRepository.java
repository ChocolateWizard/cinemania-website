/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.util;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.classes.MyImage;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class FileRepository {

    @Autowired
    ConfigProperties config;

    public void saveMediaCoverImage(MyImage image) {
//        try {
//            image.getFile().transferTo(new File(config.getMediaImagesFolderPath() + image.getFullName()));
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new DatabaseException("Unable to store media cover image");
//        }

    }

    public void deleteIfExists(String filePath) throws DatabaseException {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new DatabaseException("Unable to delete file");
        }
    }

    public boolean doesFileExist(String filePath) {
        try {
            return Files.exists(Path.of(filePath));
        } catch (Exception e) {
            throw new DatabaseException("Unable to see if file exists");
        }
    }

}
