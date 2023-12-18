/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.integration.domain.MyImageTest;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
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

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileRepositoryTest {

    @Autowired
    private FileRepository repo;

    private DataInitializer init = new DataInitializer();

    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("saveMediaCoverImage", false);
        testsPassed.put("deleteIfExistsMediaCoverImage", false);
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
        Assumptions.assumeTrue(MyImageTest.didAllTestsPass());
        init.initImages();
    }

    @Test
    @Order(1)
    void saveMediaCoverImage() {
        assertThatExceptionOfType(DatabaseException.class).isThrownBy(() -> {
            repo.saveMediaCoverImage(null);
        }).withMessage("Unable to save media cover image");

        MyImage image = new MyImage(new MockMultipartFile("cover_image", "dummy.jpg", "image/jpg", new byte[10]));
        repo.saveMediaCoverImage(image);
        File file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        Path path = Paths.get(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        assertThat(file.isDirectory()).isFalse();
        assertThat(file.getName()).isEqualTo(image.getFullName());
        try {
            assertThat(image.getBytes()).isEqualTo(Files.readAllBytes(path));
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }

        image = new MyImage(new MockMultipartFile("cover_image", "  dum  my  .jpg", "image/jpg", new byte[10]));
        repo.saveMediaCoverImage(image);
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        path = Paths.get(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        assertThat(file.isDirectory()).isFalse();
        assertThat(file.getName()).isEqualTo(image.getFullName());
        try {
            assertThat(image.getBytes()).isEqualTo(Files.readAllBytes(path));
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }

        image = new MyImage(new MockMultipartFile("cover_image", "http://www.website.com/images/  dum  my  .jpg", "image/jpg", new byte[10]));
        repo.saveMediaCoverImage(image);
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        path = Paths.get(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        assertThat(file.isDirectory()).isFalse();
        assertThat(file.getName()).isEqualTo(image.getFullName());
        try {
            assertThat(image.getBytes()).isEqualTo(Files.readAllBytes(path));
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }

        String name = "cover_image";
        String originalFileName = init.getMullholadDrive().getCoverImage();
        path = Paths.get(DataInitializer.mediaImagesFolderPath + originalFileName);
        String contentType = "image/" + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }
        image = new MyImage(new MockMultipartFile(name, originalFileName, contentType, content));
        repo.saveMediaCoverImage(image);
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        assertThat(file.isDirectory()).isFalse();
        assertThat(file.getName()).isEqualTo(image.getFullName());
        try {
            assertThat(image.getBytes()).isEqualTo(Files.readAllBytes(path));
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }

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
        image = new MyImage(new MockMultipartFile(name, originalFileName, contentType, content));
        image.setName("dummy_name_2");
        repo.saveMediaCoverImage(image);
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        path = Paths.get(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        assertThat(file.isDirectory()).isFalse();
        assertThat(file.getName()).isEqualTo(image.getFullName());
        try {
            assertThat(image.getBytes()).isEqualTo(Files.readAllBytes(path));
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }

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
        image = new MyImage(new MockMultipartFile(name, originalFileName, contentType, content));
        image.setName("");
        repo.saveMediaCoverImage(image);
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        path = Paths.get(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        assertThat(file.isDirectory()).isFalse();
        assertThat(file.getName()).isEqualTo(image.getFullName());
        try {
            assertThat(image.getBytes()).isEqualTo(Files.readAllBytes(path));
        } catch (IOException e) {
            fail("Files.readAllBytes() was not supposed to throw exception");
        }

        testsPassed.put("saveMediaCoverImage", true);
    }

    @Test
    @Order(2)
    void deleteIfExistsMediaCoverImage() {
        Assumptions.assumeTrue(testsPassed.get("saveMediaCoverImage"));

        String[] invalidInput = new String[]{null, "", " ", "        "};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.deleteIfExistsMediaCoverImage(input);
            }).withMessage("Invalid argument: image name must not be null or blank!");
        }
        invalidInput = new String[]{"jpg", "aaaaaaaa", "..", ".jpg.", ".png.", ".", " . ", "jpg.", "png.",
            "http://www.website.com/images/.jpg.", ".jpg/", ".jpg/website", ".jpg/jpg", "..jpg", "aaa aaa aaa..jpg"};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.deleteIfExistsMediaCoverImage(input);
            }).withMessage("Invalid argument: unable to infer image extension!");
        }

        invalidInput = new String[]{".gpj", ".g", "aaaaa.g", "  a  aa  aa.g", "http://www.google.com/images/aaaa.jgp"};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.deleteIfExistsMediaCoverImage(input);
            }).withMessage("Invalid argument: unrecognized image extension! Image extension must be one of following: " + MyImage.VALID_EXTENSIONS);
        }

        String fileName = init.getMullholadDrive().getCoverImage();
        File file = new File(DataInitializer.mediaImagesFolderPath + fileName);
        assertThat(file.exists()).isTrue();
        repo.deleteIfExistsMediaCoverImage(fileName);
        file = new File(DataInitializer.mediaImagesFolderPath + fileName);
        assertThat(file.exists()).isFalse();

        fileName = init.getInlandEmpire().getCoverImage();
        file = new File(DataInitializer.mediaImagesFolderPath + fileName);
        assertThat(file.exists()).isTrue();
        repo.deleteIfExistsMediaCoverImage(fileName);
        file = new File(DataInitializer.mediaImagesFolderPath + fileName);
        assertThat(file.exists()).isFalse();

        fileName = "NonExistantFileName.jpg";
        file = new File(DataInitializer.mediaImagesFolderPath + fileName);
        assertThat(file.exists()).isFalse();
        repo.deleteIfExistsMediaCoverImage(fileName);
        file = new File(DataInitializer.mediaImagesFolderPath + fileName);
        assertThat(file.exists()).isFalse();

        MyImage image = new MyImage(new MockMultipartFile("cover_image", "  dum  my  .jpg", "image/jpg", new byte[10]));
        repo.saveMediaCoverImage(image);
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isTrue();
        repo.deleteIfExistsMediaCoverImage(image.getFullName());
        file = new File(DataInitializer.mediaImagesFolderPath + image.getFullName());
        assertThat(file.exists()).isFalse();

        testsPassed.put("deleteIfExistsMediaCoverImage", true);
    }

}
