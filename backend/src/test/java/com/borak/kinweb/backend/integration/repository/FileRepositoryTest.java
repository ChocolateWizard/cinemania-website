/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(9)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileRepositoryTest {

    @Autowired
    private FileRepository repo;  
    private DataInitializer init=new DataInitializer();

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
        init.initImages();

    }

    @Test
    @Order(1)
    void saveMediaCoverImage() {

        testsPassed.put("saveMediaCoverImage", true);
    }

    @Test
    @Order(2)
    void deleteIfExistsMediaCoverImage() {

        testsPassed.put("deleteIfExistsMediaCoverImage", true);
    }

}
