/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.util;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.exceptions.InvalidInputException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class FileRepository {

    @Autowired
    private ConfigProperties config;

    public void saveMediaCoverImage(MyImage image) throws DatabaseException {
        try {
            Files.write(Path.of(config.getMediaImagesFolderPath() + image.getFullName()), image.getBytes());
        } catch (NullPointerException | IOException ex) {
            throw new DatabaseException("Unable to save media cover image");
        }
    }

    public void savePersonProfilePhoto(MyImage image) throws DatabaseException {
        try {
            Files.write(Path.of(config.getPersonImagesFolderPath() + image.getFullName()), image.getBytes());
        } catch (NullPointerException | IOException ex) {
            throw new DatabaseException("Unable to save person profile photo");
        }
    }

    public void saveUserProfileImage(MyImage image) throws DatabaseException {
        try {
            Files.write(Path.of(config.getUserImagesFolderPath() + image.getFullName()), image.getBytes());
        } catch (NullPointerException | IOException ex) {
            throw new DatabaseException("Unable to save user profile image");
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

    public void deleteIfExistsPersonPhotoImage(String imageName) throws IllegalArgumentException, DatabaseException {
        try {
            String[] parts = MyImage.extractNameAndExtension(imageName);
            Files.deleteIfExists(Path.of(config.getPersonImagesFolderPath() + parts[0] + "." + parts[1]));
        } catch (IOException ex) {
            throw new DatabaseException("Unable to delete person photo");
        }
    }

//===================================================================================================
    public Resource getMediaCoverImage(String filename) throws InvalidInputException {
        try {
            return new UrlResource(Paths.get(config.getMediaImagesFolderPath() + filename).toUri());
        } catch (MalformedURLException ex) {
            throw new InvalidInputException("Invalid image name!");
        }
    }

    public Resource getPersonProfilePhoto(String filename) throws InvalidInputException {
        try {
            return new UrlResource(Paths.get(config.getPersonImagesFolderPath() + filename).toUri());
        } catch (MalformedURLException ex) {
            throw new InvalidInputException("Invalid image name!");
        }
    }

    public Resource getUserProfileImage(String filename) throws InvalidInputException {
        try {
            return new UrlResource(Paths.get(config.getUserImagesFolderPath() + filename).toUri());
        } catch (MalformedURLException ex) {
            throw new InvalidInputException("Invalid image name!");
        }
    }
}
