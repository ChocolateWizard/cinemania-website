/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.ActorRepositoryJDBC;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
@Order(3)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class ActorRepositoryJDBCTest {

    @Autowired
    private ActorRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("findAllByMediaId_Test", false);
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
//=================================================================================================

    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(ConfigPropertiesTest.didAllTestsPass());
    }

    @Test
    @Order(1)
    @DisplayName("Tests normal functionality of findAllByMediaId method of ActorRepositoryJDBC class")
    void findAllByMediaId_Test() {
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findAllByMediaId(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        List<ActorJDBC> expected;
        List<ActorJDBC> actual;
        for (MediaJDBC media : init.getMedias()) {
            expected = media.getActings().stream()
                    .map(acting -> acting.getActor())
                    .collect(Collectors.toList());
            actual = repo.findAllByMediaId(media.getId());
            checkValues(actual, expected);
        }

        actual = repo.findAllByMediaId(10l);
        assertThat(actual).isNotNull().isEmpty();

        actual = repo.findAllByMediaId(150l);
        assertThat(actual).isNotNull().isEmpty();

        actual = repo.findAllByMediaId(Long.MAX_VALUE);
        assertThat(actual).isNotNull().isEmpty();

        testsPassed.put("findAllByMediaId_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of existsById method of ActorRepositoryJDBC class")
    void existsById_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.existsById(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        long id;
        boolean actual;
        for (ActorJDBC actor : init.getActors()) {
            actual = repo.existsById(actor.getId());
            assertThat(actual).isTrue();
        }

        id = 15l;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();

        id = 60l;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();

        id = Long.MAX_VALUE;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();

        testsPassed.put("existsById_Test", true);
    }

//==============================================================================================================
//===========================================PRIVATE METHODS====================================================
//==============================================================================================================
    private void checkValues(List<ActorJDBC> actual, List<ActorJDBC> expected) {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(actual.size() == expected.size()).isTrue();
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).getId()).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getFirstName()).isEqualTo(expected.get(i).getFirstName());
            assertThat(actual.get(i).getLastName()).isEqualTo(expected.get(i).getLastName());
            assertThat(actual.get(i).getGender()).isEqualTo(expected.get(i).getGender());
            assertThat(actual.get(i).getProfilePhoto()).isEqualTo(expected.get(i).getProfilePhoto());
            assertThat(actual.get(i).isStar()).isEqualTo(expected.get(i).isStar());
        }
    }

}
