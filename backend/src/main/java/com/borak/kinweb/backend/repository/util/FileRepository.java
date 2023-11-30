/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.util;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        try {
            Files.write(Path.of(config.getMediaImagesFolderPath() + image.getFullName()), image.getBytes());
        } catch (IOException ex) {
            throw new DatabaseException("Unable to save media cover image");
        }
    }

    public void deleteIfExistsMediaCoverImage(String imageName) {
        try {
            Files.deleteIfExists(Path.of(config.getMediaImagesFolderPath() + imageName));
        } catch (IOException ex) {
            throw new DatabaseException("Unable to delete media cover image");
        }
    }

}
