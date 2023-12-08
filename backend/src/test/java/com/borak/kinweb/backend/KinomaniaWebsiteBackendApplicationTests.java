package com.borak.kinweb.backend;

import com.borak.kinweb.backend.config.ConfigProperties;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Order(1)
class KinomaniaWebsiteBackendApplicationTests {

    @Autowired
    private ConfigProperties properties;

    @Value("${kinweb.property.mediaImagesBackupFolderPath}")
    private String mediaImagesBackupFolderPath;

    @Value("${kinweb.property.personImagesBackupFolderPath}")
    private String personImagesBackupFolderPath;

    @Value("${kinweb.property.userImagesBackupFolderPath}")
    private String userImagesBackupFolderPath;

    @Test     
    @DisplayName(value = "Tests functionality of ConfigProperties.class and if valid properties are set in application.properties and application-test.properties")
    void configPropertiesAndProperties_InitializedProperly() {
        assertThat(mediaImagesBackupFolderPath).isEqualTo("src/test/resources/database/images/media/");
        assertThat(personImagesBackupFolderPath).isEqualTo("src/test/resources/database/images/person/");
        assertThat(userImagesBackupFolderPath).isEqualTo("src/test/resources/database/images/user/");

        assertThat(properties).isNotNull();
        assertThat(properties.getMediaImagesFolderPath()).isEqualTo("src/test/resources/static/images/media/");
        assertThat(properties.getPersonImagesFolderPath()).isEqualTo("src/test/resources/static/images/person/");
        assertThat(properties.getUserImagesFolderPath()).isEqualTo("src/test/resources/static/images/user/");

        Integer port = properties.getServerPort();
        String address = properties.getServerAddress();

        assertThat(port).isNotNull().isBetween(8080, 8100);
        assertThat(address).isNotNull().isEqualTo("http://localhost");

        assertThat(properties.getMediaImagesBaseUrl()).isEqualTo(address + ":" + port + "/test/images/media/");
        assertThat(properties.getPersonImagesBaseUrl()).isEqualTo(address + ":" + port + "/test/images/person/");
        assertThat(properties.getUserImagesBaseUrl()).isEqualTo(address + ":" + port + "/test/images/user/");
    }

}
