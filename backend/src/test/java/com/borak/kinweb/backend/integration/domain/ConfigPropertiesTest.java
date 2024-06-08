/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.domain;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(2)
public class ConfigPropertiesTest {

    @Autowired
    private ConfigProperties properties;

    @Value("${custom.property.jsonResponsesPropertiesFilePath}")
    private String jsonResponsesPropertiesFilePath;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("configPropertiesAndProperties_InitializedProperly", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

//=======================================================================================================
    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(TestResultsHelper.didInitialTestPass());
    }

    @Test
    @DisplayName(value = "Tests functionality of ConfigProperties.class and if valid properties are set in application.properties and application-test.properties")
    void configPropertiesAndProperties_InitializedProperly() {
        assertThat(properties).isNotNull();
        assertThat(properties.getMediaImagesFolderPath()).isEqualTo(DataInitializer.mediaImagesFolderPath);
        assertThat(properties.getPersonImagesFolderPath()).isEqualTo(DataInitializer.personImagesFolderPath);
        assertThat(properties.getUserImagesFolderPath()).isEqualTo(DataInitializer.userImagesFolderPath);

        Integer port = properties.getServerPort();
        String address = properties.getServerAddress();

        assertThat(port).isNotNull().isEqualTo(DataInitializer.port);
        assertThat(address).isNotNull().isEqualTo(DataInitializer.address);

        assertThat(properties.getJwtCookieName()).isEqualTo(DataInitializer.jwtCookieName);
        assertThat(properties.getJwtExpirationMs()).isNotNull().isEqualTo(DataInitializer.jwtExpirationMs);
        assertThat(properties.getJwtSecret()).isEqualTo(DataInitializer.jwtSecret);

        assertThat(jsonResponsesPropertiesFilePath).isEqualTo(DataInitializer.jsonResponsesPropertiesFilePath);

        testsPassed.put("configPropertiesAndProperties_InitializedProperly", true);
    }

}
