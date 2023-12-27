/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.PersonWrapperRepositoryJDBC;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class PersonWrapperRepositoryJDBCTest {

    @Autowired
    private PersonWrapperRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("findAllPaginated_Test", false);
        testsPassed.put("findAllWithRelationsPaginated_Test", false);
        testsPassed.put("findById_Test", false);
        testsPassed.put("findByIdWithRelations_Test", false);
        testsPassed.put("insert_Test", false);
        testsPassed.put("update_Test", false);
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
    @Disabled
    @DisplayName("Tests normal functionality of findAllPaginated method of PersonWrapperRepositoryJDBC class")
    void findAllPaginated_Test() {
    }

    @Test
    @Order(2)
    @Disabled
    @DisplayName("Tests normal functionality of findAllWithRelationsPaginated method of PersonWrapperRepositoryJDBC class")
    void findAllWithRelationsPaginated_Test() {

    }

    @Test
    @Order(3)
    @Disabled
    @DisplayName("Tests normal functionality of findById method of PersonWrapperRepositoryJDBC class")
    void findById_Test() {

    }

    @Test
    @Order(4)
    @Disabled
    @DisplayName("Tests normal functionality of findByIdWithRelations method of PersonWrapperRepositoryJDBC class")
    void findByIdWithRelations_Test() {

    }

    @Test
    @Order(5)
    @Disabled
    @DisplayName("Tests normal functionality of insert method of PersonWrapperRepositoryJDBC class")
    void insert_Test() {

    }

    @Test
    @Order(6)
    @Disabled
    @DisplayName("Tests normal functionality of update method of PersonWrapperRepositoryJDBC class")
    void update_Test() {

    }

}
