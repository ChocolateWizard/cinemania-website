/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.domain;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(9)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyImageTest {

    private DataInitializer init = new DataInitializer();

    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("testStaticParameters", false);
        testsPassed.put("testFunctionality", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
//=================================================================================================

    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(ConfigPropertiesTest.didAllTestsPass());
        init.initImages();
    }

    @Test
    @Order(1)
    void testStaticParameters() {
        String[] expected = DataInitializer.MYIMAGE_VALID_EXTENSIONS;
        String[] actual = MyImage.VALID_EXTENSIONS;
        assertThat(actual).isEqualTo(expected);

        long actualSize = MyImage.IMAGE_MAX_SIZE;
        long expectedSize = DataInitializer.MYIMAGE_IMAGE_MAX_SIZE;
        assertThat(actualSize).isEqualTo(expectedSize);

        testsPassed.put("testStaticParameters", true);
    }

    @Test
    @Order(2)
    void testFunctionality() {
        List<MultipartFile> invalidInput = new ArrayList<>() {
            {
                add(null);
                add(new MockMultipartFile("name", null, null, new byte[10]));
                add(new MockMultipartFile("name", null, "content type", new byte[10]));
                add(new MockMultipartFile("name", "", "content type", new byte[10]));
                add(new MockMultipartFile("name", "jpg", "content type", new byte[10]));
                add(new MockMultipartFile("name", "aaaaaaaaa", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".", "content type", new byte[10]));
                add(new MockMultipartFile("name", "..", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".jpg.", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".png.", "content type", new byte[10]));
                add(new MockMultipartFile("name", "jpg.", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".gpj", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".g", "content type", new byte[10]));
                add(new MockMultipartFile("name", "aaaa.g", "content type", new byte[10]));
                add(new MockMultipartFile("name", "aaaa.jpg", "content type", new byte[8388998]));
                add(new MockMultipartFile("name", "aaaa.jpg", "content type", new byte[0]));
            }
        };
        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final String message;
            if (i == 0) {
                message = "Invalid argument: unable to infer image parameters!";
            } else if (i <= 5) {
                message = "Invalid argument: unable to infer image extension!";
            } else if (i <= 13) {
                message = "Invalid argument: unrecognized image extension! Image extension must be one of following: " + Arrays.toString(DataInitializer.MYIMAGE_VALID_EXTENSIONS);
            } else if (i <= 14) {
                message = "Invalid argument: image size to big! Max allowed size is " + DataInitializer.MYIMAGE_IMAGE_MAX_SIZE + " bytes!";
            } else {
                message = "Invalid argument: unable to infer image content!";
            }
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(invalidInput.get(i));
            }).withMessage(message);

        }

        MultipartFile expected = new MockMultipartFile("Dummy name", "dummy.jpg", "image/jpg", new byte[10]);
        MyImage actual = new MyImage(expected);
        checkValues(actual, expected);

        String name = "cover_image";
        String originalFileName = init.getMullholadDrive().getCoverImage();
        Path path = Paths.get(DataInitializer.mediaImagesFolderPath + originalFileName);
        String contentType = "image/" + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }
        expected = new MockMultipartFile(name, originalFileName, contentType, content);
        actual = new MyImage(expected);
        checkValues(actual, expected);

        name = "cover_image";
        originalFileName = init.getInlandEmpire().getCoverImage();
        path = Paths.get(DataInitializer.mediaImagesFolderPath + originalFileName);
        contentType = "image/" + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }
        expected = new MockMultipartFile(name, originalFileName, contentType, content);
        actual = new MyImage(expected);
        checkValues(actual, expected);

        testsPassed.put("testFunctionality", true);
    }
//=================================================================================================================
//======================================PRIVATE METHODS============================================================
//=================================================================================================================

    private void checkValues(MyImage actual, MultipartFile expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getFullName()).isEqualTo(actual.getName() + "." + actual.getExtension());
        assertThat(actual.getFullName()).isEqualTo(expected.getOriginalFilename());
        try {
            assertThat(actual.getBytes()).isEqualTo(expected.getBytes());
        } catch (IOException ex) {
            fail("MockMultipartFile was not supposed to throw exception");
        }
    }

}
