/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.domain;

import com.borak.cwb.backend.domain.MyImage;
import com.borak.cwb.backend.helpers.DataInitializer;
import com.borak.cwb.backend.helpers.Pair;
import com.borak.cwb.backend.helpers.Triple;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
@Order(3)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyImageTest {

    private final DataInitializer init = new DataInitializer();

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();

    static {
        TESTS_PASSED.put("staticAttributes_Test", false);
        TESTS_PASSED.put("extractNameAndExtension_Test", false);
        TESTS_PASSED.put("constructor_Test", false);
        TESTS_PASSED.put("setName_Test", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : TESTS_PASSED.values()) {
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
        List<String> actualExt = MyImage.VALID_EXTENSIONS;
        List<String> expectedExt = DataInitializer.MYIMAGE_VALID_EXTENSIONS;
        assertThat(actualExt).isEqualTo(expectedExt);

        long actualSize = MyImage.IMAGE_MAX_SIZE;
        long expectedSize = DataInitializer.MYIMAGE_IMAGE_MAX_SIZE;
        assertThat(actualSize).isEqualTo(expectedSize);

        TESTS_PASSED.put("staticAttributes_Test", true);
    }

    @Test
    @Order(2)
    void extractNameAndExtension_Test() {
        Assumptions.assumeTrue(TESTS_PASSED.get("staticAttributes_Test"));
        int i = 0;
        int j = 0;
        for (Triple<Class<? extends Throwable>, String, String[]> t : getExtractNameAndExtensionInputInvalidThrowsException()) {
            for (String input : t.getR()) {
                assertThatExceptionOfType(t.getL()).as("Code: i=%d; j=%d; exception message=%s; input=%s", i, j, t.getM(), input).isThrownBy(() -> {
                    MyImage.extractNameAndExtension(input);
                }).withMessage(t.getM());
                j++;
            }
            i++;
        }
        i = 0;
        String[] actual;
        Pair<String, String[]>[] validInputs = getExtractNameAndExtensionInputValid();
        try {
            for (Pair<String, String[]> p : validInputs) {
                actual = MyImage.extractNameAndExtension(p.getL());
                assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
                assertThat(actual[0]).isEqualTo(p.getR()[0]);
                assertThat(actual[1]).isEqualTo(p.getR()[1]);
                i++;
            }
        } catch (AssertionError e) {
            fail("Code number i=" + i + "; input=" + String.valueOf(validInputs[i].getR()), e);
        }

        TESTS_PASSED.put("extractNameAndExtension_Test", true);
    }

    @Test
    @Order(3)
    void constructor_Test() {
        Assumptions.assumeTrue(TESTS_PASSED.get("staticAttributes_Test"));
        Assumptions.assumeTrue(TESTS_PASSED.get("extractNameAndExtension_Test"));

        int i = 0;
        int j = 0;
        for (Triple<Class<? extends Throwable>, String, MockMultipartFile[]> t : getMyImageConstructorInputInvalidThrowsException()) {
            for (MockMultipartFile input : t.getR()) {
                assertThatExceptionOfType(t.getL()).as("Code: i=%d; j=%d; exception message=%s", i, j, t.getM()).isThrownBy(() -> {
                    new MyImage(input);
                }).withMessage(t.getM());
                j++;
            }
            i++;
        }

        i = 0;
        j = 0;
        MyImage actual;
        try {
            for (Pair<Boolean, MockMultipartFile[]> p : getMyImageConstructorInputValid()) {
                for (MockMultipartFile expected : p.getR()) {
                    actual = new MyImage(expected);
                    checkValues(actual, expected, p.getL());
                    j++;
                }
                i++;
            }
        } catch (Throwable t) {
            fail("Code failed at (i,j)=(" + i + "," + j + ")", t);
        }

        TESTS_PASSED.put("constructor_Test", true);
    }

    @Test
    @Order(4)
    void setName_Test() {
        Assumptions.assumeTrue(TESTS_PASSED.get("staticAttributes_Test"));
        Assumptions.assumeTrue(TESTS_PASSED.get("extractNameAndExtension_Test"));
        Assumptions.assumeTrue(TESTS_PASSED.get("constructor_Test"));

        int i = 0;
        int j = 0;
        for (Triple<Class<? extends Throwable>, String, String[]> t : getSetNameInputInvalidThrowsException()) {
            for (String input : t.getR()) {
                assertThatExceptionOfType(t.getL()).as("Code: i=%d; j=%d; exception message=%s; input=%s", i, j, t.getM(), input).isThrownBy(() -> {
                    MyImage image = new MyImage(new MockMultipartFile("cover_image", "Dummy.jpg", "image/jpg", new byte[10]));
                    image.setName(input);
                }).withMessage(t.getM());
                j++;
            }
            i++;
        }

        i = 0;
        MyImage actual = new MyImage(new MockMultipartFile("cover_image", "Dummy.jpg", "image/jpg", new byte[10]));
        for (String input : getSetNameInputValid()) {
            actual.setName(input);
            assertThat(actual.getName()).as("Code: i=%d; input=%s", i++, input).isEqualTo(input);
        }

        TESTS_PASSED.put("setName_Test", true);
    }

//=================================================================================================================================
//PRIVATE METHODS
//=================================================================================================================================
//static attributes  
    private void checkValues(MyImage actual, MultipartFile expected, boolean withURL) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(actual.getFullName()).isEqualTo(actual.getName() + "." + actual.getExtension());
        if (withURL) {
            String name = expected.getOriginalFilename().trim();
            name = name.substring(name.replaceAll("\\\\", "/").lastIndexOf("/") + 1);
            assertThat(actual.getFullName()).isEqualTo(name);
        } else {
            assertThat(actual.getFullName()).isEqualTo(expected.getOriginalFilename());
        }
        try {
            assertThat(actual.getBytes()).isEqualTo(expected.getBytes());
        } catch (IOException ex) {
            fail("MockMultipartFile was not supposed to throw exception", ex);
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------
//extractNameAndExtension()
    private Triple<Class<? extends Throwable>, String, String[]>[] getExtractNameAndExtensionInputInvalidThrowsException() {
        return new Triple[]{
            new Triple(IllegalArgumentException.class, "Invalid argument: image name must not be null or blank!", new String[]{
                null, "", " ", "        "
            }),
            new Triple(IllegalArgumentException.class, "Invalid argument: unable to infer image extension!", new String[]{
                "jpg", "aaaaaaaa", "..", ".jpg.", ".png.", ".", " . ", "jpg.", "png.",
                "http://www.website.com/images/.jpg.", ".jpg/", ".jpg/website", ".jpg/jpg", "..jpg", "aaa aaa aaa..jpg"}),
            new Triple(IllegalArgumentException.class, "Invalid argument: unrecognized image extension! Image extension must be one of following: " + MyImage.VALID_EXTENSIONS,
            new String[]{".gpj", ".g", "aaaaa.g", "  a  aa  aa.g", "http://www.google.com/images/aaaa.jgp"})
        };
    }

    private Pair<String, String[]>[] getExtractNameAndExtensionInputValid() {
        return new Pair[]{
            new Pair("aaaaaa.jpg", new String[]{"aaaaaa", "jpg"}),
            new Pair("  aaa  aaa  .jpg", new String[]{"aaa__aaa", "jpg"}),
            new Pair("http://www.google.com/images/aaaaaa.png", new String[]{"aaaaaa", "png"}),
            new Pair("http://www.google.com/images/  aaa  aaa  .png", new String[]{"aaa__aaa", "png"}),
            new Pair(".png", new String[]{"", "png"}),
            new Pair("http://www.google.com/images/.jpg", new String[]{"", "jpg"})
        };
    }
//---------------------------------------------------------------------------------------------------------------------------------
//MyImage()

    private Triple<Class<? extends Throwable>, String, MockMultipartFile[]>[] getMyImageConstructorInputInvalidThrowsException() {
        return new Triple[]{
            new Triple(IllegalArgumentException.class, "Invalid argument: unable to infer image parameters!", new MockMultipartFile[]{null}),
            new Triple(IllegalArgumentException.class, "Invalid argument: image name must not be null or blank!", new MockMultipartFile[]{
                new MockMultipartFile("name", null, null, new byte[10]),
                new MockMultipartFile("name", null, "content type", new byte[10]),
                new MockMultipartFile("name", "", "content type", new byte[10]),
                new MockMultipartFile("name", "              ", "content type", new byte[10])
            }),
            new Triple(IllegalArgumentException.class, "Invalid argument: unable to infer image extension!", new MockMultipartFile[]{
                new MockMultipartFile("name", "jpg", "content type", new byte[10]),
                new MockMultipartFile("name", "aaaaaaaaa", "content type", new byte[10]),
                new MockMultipartFile("name", "..", "content type", new byte[10]),
                new MockMultipartFile("name", "...", "content type", new byte[10]),
                new MockMultipartFile("name", " .   . ", "content type", new byte[10]),
                new MockMultipartFile("name", ".", "content type", new byte[10]),
                new MockMultipartFile("name", " . ", "content type", new byte[10]),
                new MockMultipartFile("name", ".jpg.", "content type", new byte[10]),
                new MockMultipartFile("name", ".png.", "content type", new byte[10]),
                new MockMultipartFile("name", "jpg.", "content type", new byte[10]),
                new MockMultipartFile("name", "..jpg", "content type", new byte[10]),
                new MockMultipartFile("name", "png.", "content type", new byte[10]),
                new MockMultipartFile("name", "http://www.website.com/images/.jpg.", "content type", new byte[10]),
                new MockMultipartFile("name", ".jpg/", "content type", new byte[10]),
                new MockMultipartFile("name", ".jpg/website", "content type", new byte[10]),
                new MockMultipartFile("name", ".jpg/jpg", "content type", new byte[10])
            }),
            new Triple(IllegalArgumentException.class, "Invalid argument: unrecognized image extension! Image extension must be one of following: " + MyImage.VALID_EXTENSIONS, new MockMultipartFile[]{
                new MockMultipartFile("name", ".gpj", "content type", new byte[10]),
                new MockMultipartFile("name", ".g", "content type", new byte[10]),
                new MockMultipartFile("name", "aaaa.g", "content type", new byte[10]),
                new MockMultipartFile("name", "  a  aa  aa.g", "content type", new byte[10]),
                new MockMultipartFile("name", "http://www.google.com/images/aaaa.jgp", "content type", new byte[10])
            }),
            new Triple(IllegalArgumentException.class, "Invalid argument: image size to big! Max allowed size is " + MyImage.IMAGE_MAX_SIZE + " bytes!", new MockMultipartFile[]{
                new MockMultipartFile("name", "aaaa.jpg", "content type", new byte[8388998])
            }),
            new Triple(IllegalArgumentException.class, "Invalid argument: unable to infer image content!", new MockMultipartFile[]{
                new MockMultipartFile("name", "aaaa.jpg", "content type", new byte[0])
            })
        };
    }

    private Pair<Boolean, MockMultipartFile[]>[] getMyImageConstructorInputValid() throws IOException {
        return new Pair[]{
            new Pair(false, new MockMultipartFile[]{
                new MockMultipartFile("Dummy name", "dummy.jpg", "image/jpg", new byte[10]),
                new MockMultipartFile(
                "cover_image",
                init.getMullholadDrive().getCoverImage(),
                "image/" + init.getMullholadDrive().getCoverImage().substring(init.getMullholadDrive().getCoverImage().lastIndexOf(".") + 1),
                Files.readAllBytes(Paths.get(DataInitializer.mediaImagesFolderPath + init.getMullholadDrive().getCoverImage()))
                )
            }),
            new Pair(true, new MockMultipartFile[]{
                new MockMultipartFile(
                "cover_image",
                "http://www.google.com/images/" + init.getInlandEmpire().getCoverImage(),
                "image/" + init.getInlandEmpire().getCoverImage().substring(init.getInlandEmpire().getCoverImage().lastIndexOf(".") + 1),
                Files.readAllBytes(Paths.get(DataInitializer.mediaImagesFolderPath + init.getInlandEmpire().getCoverImage()))
                )
            })
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//setName()
    private Triple<Class<? extends Throwable>, String, String[]>[] getSetNameInputInvalidThrowsException() {
        return new Triple[]{
            new Triple(IllegalArgumentException.class, "Invalid argument: MyImage name mustn't be null and it mustn't contain any dots, slashes or empty spaces!",
            new String[]{
                null, " ", "/", "\\", ".", "aaaaa aaaaa", "   aaaaaa", "aaaaaa  ",
                "aaaa.aaaa..aaa", "..", "/////", "/aaa/aaa/aa", "aaa\\aaaa\\aaaa",
                "/aaa..aaa..aa..aa", "http://www.google.com/aaaa.jpg"
            })
        };
    }

    private String[] getSetNameInputValid() {
        return new String[]{
            "", "__"
        };
    }

}
