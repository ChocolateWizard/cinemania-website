/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.MediaRepositoryJDBC;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class MediaRepositoryJDBCTest {

    @Autowired
    private MediaRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("existsById_Test", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(ConfigPropertiesTest.didAllTestsPass());
    }

    //============================================================================================================ 
    //===========================================TESTS============================================================ 
    //============================================================================================================ 
    @Test
    @Order(1)
    @DisplayName("Tests normal functionality of existsById method of MediaRepositoryJDBC class")
    void existsById_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.existsById(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id=null;
        boolean actual;
        for (MediaJDBC media : init.getMedias()) {
            id = media.getId();
            actual = repo.existsById(id);
            assertThat(actual).isTrue();
        }

        actual = repo.existsById(id + 1l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 2l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 20l);
        assertThat(actual).isFalse();

        id = Long.MAX_VALUE;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();
        testsPassed.put("existsById_Test", true);
    }

}
