/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.MyImage;
import com.borak.cwb.backend.logic.security.JwtUtils;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class TestUtil {

    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final List<String> IMAGE_EXTENSIONS = MyImage.VALID_EXTENSIONS;
    private static final String[] EMAIL_DOMAINS = {
        "gmail.com",
        "yahoo.com",
        "outlook.com",
        "hotmail.com",
        "aol.com",
        "icloud.com",
        "mail.com",
        "yandex.com",
        "protonmail.com",
        "zoho.com"
    };

    private final ConfigProperties config;
    private final JwtUtils jwtUtils;

    @Autowired
    public TestUtil(ConfigProperties config, JwtUtils jwtUtils) {
        this.config = config;
        this.jwtUtils = jwtUtils;
    }

//=================================================================================================================================
//STATIC PUBLIC METHODS
    /**
     * Returns random string of passed length. If invalid values passed, method
     * behaviour is untested. Not thread safe. String can contain the following
     * characters:{@value #ALPHABET}.
     *
     * @param length the length of the random string
     * @return a random string of the specified length
     */
    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            char randomChar = ALPHABET.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    /**
     * Returns random email domain as string.
     *
     * @return a string representing a random email domain
     */
    public static String getRandomValidEmailDomain() {
        int index = RANDOM.nextInt(EMAIL_DOMAINS.length);
        return EMAIL_DOMAINS[index];
    }

    /**
     * Returns bytes of the passed string value, encoded by UTF_8. If invalid
     * values passed, method behaviour is untested. Not thread safe
     *
     * @param text plain string
     * @return bytes of the passed string
     */
    public static byte[] getBytes(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Method that returns a random int between min and max (inclusive). If
     * invalid values passed, method behaviour is untested. Not thread safe
     *
     * @param min left boundary value
     * @param max right boundary value
     * @return random int between 2 integers
     */
    public static int getRandomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    /**
     * Method to return a random enum from a passed array of enums. If invalid
     * values passed, method behaviour is untested. Not thread safe
     *
     * @param <T> enum type
     * @param enumValues array of enums
     * @return random enum from the passed array
     */
    public static <T extends Enum<?>> T getRandomEnum(T[] enumValues) {
        int index = RANDOM.nextInt(enumValues.length);
        return enumValues[index];
    }

    /**
     * Creates a random dummy MockMultipartFile object representing a image
     * file.
     *
     * @return image file as MockMultipartFile
     */
    public static MockMultipartFile getRandomValidImage() {
        int index = RANDOM.nextInt(IMAGE_EXTENSIONS.size());
        String extension = IMAGE_EXTENSIONS.get(index);
        String contentType = MyImage.parseContentType(extension).toString();
        String name = getRandomString(getRandomInt(0, 20)) + "." + extension;
        byte[] bytes = getBytes(getRandomString(getRandomInt(10, 100)));
        return new MockMultipartFile(name, name, contentType, bytes);
    }

    public static MockMultipartFile[] getInvalidImages() {
        return new MockMultipartFile[]{
            new MockMultipartFile("name", null, "image/png", new byte[10]),
            new MockMultipartFile("name", "", "image/png", new byte[10]),
            new MockMultipartFile("name", "              ", "image/png", new byte[10]),
            new MockMultipartFile("name", "jpg", "image/png", new byte[10]),
            new MockMultipartFile("name", "aaaaaaaaa", "image/png", new byte[10]),
            new MockMultipartFile("name", "..", "image/png", new byte[10]),
            new MockMultipartFile("name", "...", "image/png", new byte[10]),
            new MockMultipartFile("name", " .   . ", "image/png", new byte[10]),
            new MockMultipartFile("name", ".", "image/png", new byte[10]),
            new MockMultipartFile("name", " . ", "image/png", new byte[10]),
            new MockMultipartFile("name", ".jpg.", "image/png", new byte[10]),
            new MockMultipartFile("name", ".png.", "image/png", new byte[10]),
            new MockMultipartFile("name", "jpg.", "image/png", new byte[10]),
            new MockMultipartFile("name", "..jpg", "image/png", new byte[10]),
            new MockMultipartFile("name", "png.", "image/png", new byte[10]),
            new MockMultipartFile("name", "http://www.website.com/images/.jpg.", "image/png", new byte[10]),
            new MockMultipartFile("name", ".jpg/", "image/png", new byte[10]),
            new MockMultipartFile("name", ".jpg/website", "image/png", new byte[10]),
            new MockMultipartFile("name", ".jpg/jpg", "image/png", new byte[10]),
            new MockMultipartFile("name", ".gpj", "image/png", new byte[10]),
            new MockMultipartFile("name", ".g", "image/png", new byte[10]),
            new MockMultipartFile("name", "aaaa.g", "image/png", new byte[10]),
            new MockMultipartFile("name", "  a  aa  aa.g", "image/png", new byte[10]),
            new MockMultipartFile("name", "http://www.google.com/images/aaaa.jgp", "image/png", new byte[10]),
            new MockMultipartFile("name", "aaaa.png", "image/png", new byte[8388998]),
            new MockMultipartFile("name", "aaaa.png", "image/png", new byte[0])
        };
    }

    public static void assertResourcesEqual(List<Resource> actual, List<Resource> expected) throws AssertionError {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(expected).isNotNull().isNotEmpty();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(expected.get(i)).isNotNull();
            assertThat(actual.get(i).exists()).isTrue().isEqualTo(expected.get(i).exists());
            assertThat(actual.get(i).isFile()).isTrue().isEqualTo(expected.get(i).isFile());
            assertThat(actual.get(i).isReadable()).isTrue().isEqualTo(expected.get(i).isReadable());
            assertThat(actual.get(i).getFilename()).isNotBlank().isEqualTo(expected.get(i).getFilename());
            try {
                assertThat(actual.get(i).getContentAsByteArray()).isEqualTo(expected.get(i).getContentAsByteArray());
            } catch (IOException ex) {
                fail("Unable to read resource bytes at i=" + i, ex);
            }
        }
    }

//=================================================================================================================================
//NON-STATIC PUBLIC METHODS
    public List<Resource> getAllUserProfileImages() throws RuntimeException {
        return getAllImagesFromFolder(config.getUserImagesFolderPath());
    }

    public List<Resource> getAllMediaCoverImages() throws RuntimeException {
        return getAllImagesFromFolder(config.getMediaImagesFolderPath());
    }

    public List<Resource> getAllPersonProfilePhotos() throws RuntimeException {
        return getAllImagesFromFolder(config.getPersonImagesFolderPath());
    }

    public HttpCookie constructJWTCookie(String username) {
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

//=================================================================================================================================
//PRIVATE METHODS
    private List<Resource> getAllImagesFromFolder(String folderPath) throws RuntimeException {
        try {
            Path path = Path.of(folderPath);
            try (Stream<Path> paths = Files.walk(path)) {
                return paths.filter(Files::isRegularFile)
                        .map(p -> {
                            try {
                                return new UrlResource(p.toUri());
                            } catch (MalformedURLException e) {
                                throw new RuntimeException("Issue in reading the file from directory: " + folderPath, e);
                            }
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue in reading files from the directory: " + folderPath, e);
        }
    }

}
