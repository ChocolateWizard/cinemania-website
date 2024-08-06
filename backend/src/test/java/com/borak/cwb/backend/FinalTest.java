/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend;

import com.borak.cwb.backend.helpers.TestResultsHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(Integer.MAX_VALUE)
public class FinalTest {

    @Test
    @DisplayName("Tests whether or not all tests have passed")
    void didAllTestsPass() {
        boolean didAllTestsPass = TestResultsHelper.didAllTestsPass();
        assertThat(didAllTestsPass).isTrue();
    }

}
