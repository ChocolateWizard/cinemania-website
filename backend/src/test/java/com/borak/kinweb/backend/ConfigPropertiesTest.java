/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.helpers.DataInitializer;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
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
    @Value("${kinweb.property.mediaImagesBackupFolderPath}")
    private String mediaImagesBackupFolderPath;

    @Value("${kinweb.property.personImagesBackupFolderPath}")
    private String personImagesBackupFolderPath;

    @Value("${kinweb.property.userImagesBackupFolderPath}")
    private String userImagesBackupFolderPath;

    @Test
    @DisplayName(value = "Tests functionality of ConfigProperties.class and if valid properties are set in application.properties and application-test.properties")
    void configPropertiesAndProperties_InitializedProperly() {
        assertThat(mediaImagesBackupFolderPath).isEqualTo(DataInitializer.mediaImagesBackupFolderPath);
        assertThat(personImagesBackupFolderPath).isEqualTo(DataInitializer.personImagesBackupFolderPath);
        assertThat(userImagesBackupFolderPath).isEqualTo(DataInitializer.userImagesBackupFolderPath);

        assertThat(properties).isNotNull();
        assertThat(properties.getMediaImagesFolderPath()).isEqualTo(DataInitializer.mediaImagesFolderPath);
        assertThat(properties.getPersonImagesFolderPath()).isEqualTo(DataInitializer.personImagesFolderPath);
        assertThat(properties.getUserImagesFolderPath()).isEqualTo(DataInitializer.userImagesFolderPath);

        Integer port = properties.getServerPort();
        String address = properties.getServerAddress();

        assertThat(port).isNotNull().isEqualTo(DataInitializer.port);
        assertThat(address).isNotNull().isEqualTo(DataInitializer.address);

        assertThat(properties.getMediaImagesBaseUrl()).isEqualTo(DataInitializer.mediaImagesBaseUrl);
        assertThat(properties.getPersonImagesBaseUrl()).isEqualTo(DataInitializer.personImagesBaseUrl);
        assertThat(properties.getUserImagesBaseUrl()).isEqualTo(DataInitializer.userImagesBaseUrl);

        testsPassed.put("configPropertiesAndProperties_InitializedProperly", true);
    }

}
