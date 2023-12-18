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
import java.nio.file.Paths;
import java.util.Arrays;
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

    public void saveMediaCoverImage(MyImage image) throws DatabaseException {
        try {
            Files.write(Path.of(config.getMediaImagesFolderPath() + image.getFullName()), image.getBytes());
        } catch (NullPointerException | IOException ex) {
            throw new DatabaseException("Unable to save media cover image");
        }
    }

    public void deleteIfExistsMediaCoverImage(String imageName) throws IllegalArgumentException, DatabaseException {
        try {
            String[] parts = MyImage.extractNameAndExtension(imageName);
            Files.deleteIfExists(Path.of(config.getMediaImagesFolderPath() + parts[0] + "." + parts[1]));
        } catch (IOException ex) {
            throw new DatabaseException("Unable to delete media cover image");
        }
    }
//===================================================================================================

}
