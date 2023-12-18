/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.domain;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.helpers.DataInitializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyImageTest {

    private DataInitializer init = new DataInitializer();

    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("staticAttributes_Test", false);
        testsPassed.put("extractNameAndExtension_Test", false);
        testsPassed.put("constructor_Test", false);
        testsPassed.put("setName_Test", false);
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
    void staticAttributes_Test() {
        List<String> expected = DataInitializer.MYIMAGE_VALID_EXTENSIONS;
        List<String> actual = MyImage.VALID_EXTENSIONS;
        assertThat(actual).isEqualTo(expected);

        long actualSize = MyImage.IMAGE_MAX_SIZE;
        long expectedSize = DataInitializer.MYIMAGE_IMAGE_MAX_SIZE;
        assertThat(actualSize).isEqualTo(expectedSize);

        testsPassed.put("staticAttributes_Test", true);
    }

    @Test
    @Order(2)
    void extractNameAndExtension_Test() {
        Assumptions.assumeTrue(testsPassed.get("staticAttributes_Test"));
        String[] invalidInput = new String[]{null, "", " ", "        "};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                MyImage.extractNameAndExtension(input);
            }).withMessage("Invalid argument: image name must not be null or blank!");
        }
        invalidInput = new String[]{"jpg", "aaaaaaaa", "..", ".jpg.", ".png.", ".", " . ", "jpg.", "png.",
            "http://www.website.com/images/.jpg.", ".jpg/", ".jpg/website", ".jpg/jpg", "..jpg", "aaa aaa aaa..jpg"};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                MyImage.extractNameAndExtension(input);
            }).withMessage("Invalid argument: unable to infer image extension!");
        }

        invalidInput = new String[]{".gpj", ".g", "aaaaa.g", "  a  aa  aa.g", "http://www.google.com/images/aaaa.jgp"};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                MyImage.extractNameAndExtension(input);
            }).withMessage("Invalid argument: unrecognized image extension! Image extension must be one of following: " + MyImage.VALID_EXTENSIONS);
        }
        String[] actual = MyImage.extractNameAndExtension("aaaaaa.jpg");
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        assertThat(actual[0]).isEqualTo("aaaaaa");
        assertThat(actual[1]).isEqualTo("jpg");

        actual = MyImage.extractNameAndExtension("  aaa  aaa  .jpg");
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        assertThat(actual[0]).isEqualTo("aaa__aaa");
        assertThat(actual[1]).isEqualTo("jpg");

        actual = MyImage.extractNameAndExtension("http://www.google.com/images/aaaaaa.png");
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        assertThat(actual[0]).isEqualTo("aaaaaa");
        assertThat(actual[1]).isEqualTo("png");

        actual = MyImage.extractNameAndExtension("http://www.google.com/images/  aaa  aaa  .png");
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        assertThat(actual[0]).isEqualTo("aaa__aaa");
        assertThat(actual[1]).isEqualTo("png");

        actual = MyImage.extractNameAndExtension(".png");
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        assertThat(actual[0]).isEmpty();
        assertThat(actual[1]).isEqualTo("png");

        actual = MyImage.extractNameAndExtension("http://www.google.com/images/.jpg");
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        assertThat(actual[0]).isEmpty();
        assertThat(actual[1]).isEqualTo("jpg");

        testsPassed.put("extractNameAndExtension_Test", true);
    }

    @Test
    @Order(3)
    void constructor_Test() {
        Assumptions.assumeTrue(testsPassed.get("staticAttributes_Test"));
        Assumptions.assumeTrue(testsPassed.get("extractNameAndExtension_Test"));
        List<MultipartFile> invalidInput = new ArrayList<>() {
            {
                add(null);
            }
        };

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final MultipartFile input = invalidInput.get(i);
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(input);
            }).withMessage("Invalid argument: unable to infer image parameters!");
        }

        invalidInput = new ArrayList<>() {
            {
                add(new MockMultipartFile("name", null, null, new byte[10]));
                add(new MockMultipartFile("name", null, "content type", new byte[10]));
                add(new MockMultipartFile("name", "", "content type", new byte[10]));
                add(new MockMultipartFile("name", "              ", "content type", new byte[10]));
            }
        };

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final MultipartFile input = invalidInput.get(i);
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(input);
            }).withMessage("Invalid argument: image name must not be null or blank!");
        }

        invalidInput = new ArrayList<>() {
            {
                add(new MockMultipartFile("name", "jpg", "content type", new byte[10]));
                add(new MockMultipartFile("name", "aaaaaaaaa", "content type", new byte[10]));
                add(new MockMultipartFile("name", "..", "content type", new byte[10]));
                add(new MockMultipartFile("name", "...", "content type", new byte[10]));
                add(new MockMultipartFile("name", " .   . ", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".", "content type", new byte[10]));
                add(new MockMultipartFile("name", " . ", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".jpg.", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".png.", "content type", new byte[10]));
                add(new MockMultipartFile("name", "jpg.", "content type", new byte[10]));
                add(new MockMultipartFile("name", "..jpg", "content type", new byte[10]));
                add(new MockMultipartFile("name", "png.", "content type", new byte[10]));
                add(new MockMultipartFile("name", "http://www.website.com/images/.jpg.", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".jpg/", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".jpg/website", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".jpg/jpg", "content type", new byte[10]));
            }
        };

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final MultipartFile input = invalidInput.get(i);
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(input);
            }).withMessage("Invalid argument: unable to infer image extension!");
        }

        invalidInput = new ArrayList<>() {
            {
                add(new MockMultipartFile("name", ".gpj", "content type", new byte[10]));
                add(new MockMultipartFile("name", ".g", "content type", new byte[10]));
                add(new MockMultipartFile("name", "aaaa.g", "content type", new byte[10]));
                add(new MockMultipartFile("name", "  a  aa  aa.g", "content type", new byte[10]));
                add(new MockMultipartFile("name", "http://www.google.com/images/aaaa.jgp", "content type", new byte[10]));
            }
        };

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final MultipartFile input = invalidInput.get(i);
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(input);
            }).withMessage("Invalid argument: unrecognized image extension! Image extension must be one of following: " + MyImage.VALID_EXTENSIONS);
        }

        invalidInput = new ArrayList<>() {
            {
                add(new MockMultipartFile("name", "aaaa.jpg", "content type", new byte[8388998]));
            }
        };

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final MultipartFile input = invalidInput.get(i);
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(input);
            }).withMessage("Invalid argument: image size to big! Max allowed size is " + MyImage.IMAGE_MAX_SIZE + " bytes!");
        }

        invalidInput = new ArrayList<>() {
            {
                add(new MockMultipartFile("name", "aaaa.jpg", "content type", new byte[0]));
            }
        };

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            final MultipartFile input = invalidInput.get(i);
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                new MyImage(input);
            }).withMessage("Invalid argument: unable to infer image content!");
        }

        MultipartFile expected = new MockMultipartFile("Dummy name", "dummy.jpg", "image/jpg", new byte[10]);
        MyImage actual = new MyImage(expected);
        checkValuesNoURL(actual, expected);

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
        checkValuesNoURL(actual, expected);

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
        expected = new MockMultipartFile(name, "http://www.google.com/images/" + originalFileName, contentType, content);
        actual = new MyImage(expected);
        checkValuesWithURL(actual, expected);

        testsPassed.put("constructor_Test", true);
    }

    @Test
    @Order(4)
    void setName_Test() {
        Assumptions.assumeTrue(testsPassed.get("staticAttributes_Test"));
        Assumptions.assumeTrue(testsPassed.get("extractNameAndExtension_Test"));
        Assumptions.assumeTrue(testsPassed.get("constructor_Test"));

        String[] invalidInput = new String[]{null, " ", "/", "\\", ".", "aaaaa aaaaa", "   aaaaaa", "aaaaaa  ", "aaaa.aaaa..aaa", "..", "/////", "/aaa/aaa/aa", "aaa\\aaaa\\aaaa", "/aaa..aaa..aa..aa", "http://www.google.com/aaaa.jpg"};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final String input = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                MyImage image = new MyImage(new MockMultipartFile("cover_image", "Dummy.jpg", "image/jpg", new byte[10]));
                image.setName(input);
            }).withMessage("Invalid argument: MyImage name mustn't be null and it mustn't contain any dots, slashes or empty spaces!");
        }

        MyImage actual = new MyImage(new MockMultipartFile("cover_image", "Dummy.jpg", "image/jpg", new byte[10]));
        actual.setName("");
        assertThat(actual.getName()).isEmpty();

        actual = new MyImage(new MockMultipartFile("cover_image", "Dummy.jpg", "image/jpg", new byte[10]));
        actual.setName("__");
        assertThat(actual.getName()).isEqualTo("__");

        testsPassed.put("setName_Test", true);
    }

//=================================================================================================================
//======================================PRIVATE METHODS============================================================
//=================================================================================================================
    private void checkValuesNoURL(MyImage actual, MultipartFile expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getFullName()).isEqualTo(actual.getName() + "." + actual.getExtension());
        assertThat(actual.getFullName()).isEqualTo(expected.getOriginalFilename());
        try {
            assertThat(actual.getBytes()).isEqualTo(expected.getBytes());
        } catch (IOException ex) {
            fail("MockMultipartFile was not supposed to throw exception");
        }
    }

    private void checkValuesWithURL(MyImage actual, MultipartFile expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getFullName()).isEqualTo(actual.getName() + "." + actual.getExtension());
        String name = expected.getOriginalFilename().trim();
        name = name.substring(name.replaceAll("\\\\", "/").lastIndexOf("/") + 1);
        assertThat(actual.getFullName()).isEqualTo(name);
        try {
            assertThat(actual.getBytes()).isEqualTo(expected.getBytes());
        } catch (IOException ex) {
            fail("MockMultipartFile was not supposed to throw exception");
        }
    }

}
