/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.classes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
public class MyImage {

    public static final List<String> VALID_EXTENSIONS = Collections.unmodifiableList(Arrays.asList("png", "jpg", "jpeg"));
    public static final long IMAGE_MAX_SIZE = 8388608L;

    private String name;
    private final String extension;
    private String fullName;
    private final byte[] bytes;

    /**
     * Constructs a new {@code MyImage} object from a {@link MultipartFile}.
     *
     * This constructor extracts the name and extension from the original
     * filename of the {@code MultipartFile}, constructs full name by adding
     * them together with a separator '.' and reads the file's bytes into
     * memory. If original filename contains URL elements, then they will be
     * ignored up until the last slash.
     *
     * @param file {@code MultipartFile} to create the {@code MyImage} from
     * @throws IllegalArgumentException if the {@code MultipartFile} original
     * filename is null, blank, invalid file name structure, the extension part
     * is not found in {@link MyImage#VALID_EXTENSIONS}, it's size is 0 or
     * greater than {@link MyImage#IMAGE_MAX_SIZE} or an error occurs while
     * reading its bytes.
     */
    public MyImage(MultipartFile file) throws IllegalArgumentException {
        try {
            String[] parts = extractNameAndExtension(file.getOriginalFilename());
            this.name = parts[0];
            this.extension = parts[1];
            this.fullName = parts[0] + "." + parts[1];
            if (file.getSize() > IMAGE_MAX_SIZE) {
                throw new IllegalArgumentException("Invalid argument: image size to big! Max allowed size is " + IMAGE_MAX_SIZE + " bytes!");
            }
            if (file.getSize() <= 0) {
                throw new IllegalArgumentException("Invalid argument: unable to infer image content!");
            }
            this.bytes = file.getBytes();
        } catch (NullPointerException | IOException e) {
            throw new IllegalArgumentException("Invalid argument: unable to infer image parameters!");
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

    /**
     * Sets the name of the MyImage object and constructs fullName by adding '.'
     * and extension to it.
     *
     * @param name The new name for the MyImage object.
     * @throws IllegalArgumentException If the provided name is null or contains
     * any dots, slashes, or empty spaces.
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.matches(".*[\\.\\/\\\\ ].*")) {
            throw new IllegalArgumentException("Invalid argument: MyImage name mustn't be null and it mustn't contain any dots, slashes or empty spaces!");
        }
        this.name = name;
        this.fullName = name + "." + this.extension;
    }

    /**
     * Extracts the name and extension from the given image file name, returning
     * them as a String array of 2 elements, where first element is the name,
     * and second is the extension. If provided parameter violates image file
     * naming structure then an exception is thrown. Leading and trailing space
     * is removed. If an URL with file name at end is provided, it will ignore
     * string up until the last slash. Every white space in file name is
     * replaced by a underscore - '_'
     *
     * @param imageFileName the image file name to extract the name and
     * extension from. Can be a URL with file name at the end of it
     * @return String array of size 2 where the first element is the name of the
     * image file with white space replaced by underscore - '_', and the second
     * element is the extension of the image file without separator '.' and
     * leading and trailing spaces.
     * @throws IllegalArgumentException if the input imageFileName is null,
     * blank, or if image file name contains multiple '.' characters, or if the
     * extension part is not found in {@link MyImage#VALID_EXTENSIONS}.
     */
    public static String[] extractNameAndExtension(String imageFileName) throws IllegalArgumentException {
        if (imageFileName == null || imageFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid argument: image name must not be null or blank!");
        }
        String trimmedName = imageFileName.trim();
        String fileName = trimmedName.substring(trimmedName.replaceAll("\\\\", "/").lastIndexOf("/") + 1);
        String[] parts = fileName.split("\\.");
        if (parts.length != 2 || (fileName.length() - fileName.replace(".", "").length()) != 1) {
            throw new IllegalArgumentException("Invalid argument: unable to infer image extension!");
        }
        parts[0] = parts[0].trim().replace(" ", "_");
        if (!MyImage.VALID_EXTENSIONS.contains(parts[1])) {
            throw new IllegalArgumentException("Invalid argument: unrecognized image extension! Image extension must be one of following: " + MyImage.VALID_EXTENSIONS);
        }
        return parts;
    }

}
