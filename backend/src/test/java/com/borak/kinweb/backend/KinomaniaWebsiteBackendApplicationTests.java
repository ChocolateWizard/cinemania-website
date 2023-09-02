package com.borak.kinweb.backend;

import com.borak.kinweb.backend.config.ConfigProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class KinomaniaWebsiteBackendApplicationTests {

    @Autowired
    ConfigProperties properties;
    
    @Value("${kinweb.property.mediaImagesBackupFolderPath}")
    String mediaImagesBackupFolderPath;
    
    @Value("${kinweb.property.personImagesBackupFolderPath}")
    String personImagesBackupFolderPath;
    
    @Value("${kinweb.property.userImagesBackupFolderPath}")
    String userImagesBackupFolderPath;

    @Test
    void configPropertiesInitializedProperly() {
        Assertions.assertThat(mediaImagesBackupFolderPath).isEqualTo("src/test/resources/database/images/media/");
        Assertions.assertThat(personImagesBackupFolderPath).isEqualTo("src/test/resources/database/images/person/");
        Assertions.assertThat(userImagesBackupFolderPath).isEqualTo("src/test/resources/database/images/user/");
        
        
        Assertions.assertThat(properties).isNotNull();
        Assertions.assertThat(properties.getMediaImagesFolderPath()).isEqualTo("src/test/resources/static/images/media/");
        Assertions.assertThat(properties.getPersonImagesFolderPath()).isEqualTo("src/test/resources/static/images/person/");
        Assertions.assertThat(properties.getUserImagesFolderPath()).isEqualTo("src/test/resources/static/images/user/");

        Integer port = properties.getServerPort();
        String address = properties.getServerAddress();

        Assertions.assertThat(port).isNotNull().isBetween(8080, 8100);
        Assertions.assertThat(address).isNotNull().isEqualTo("http://localhost");

        Assertions.assertThat(properties.getMediaImagesBaseUrl()).isEqualTo(address + ":" + port + "/test/images/media/");
        Assertions.assertThat(properties.getPersonImagesBaseUrl()).isEqualTo(address + ":" + port + "/test/images/person/");
        Assertions.assertThat(properties.getUserImagesBaseUrl()).isEqualTo(address + ":" + port + "/test/images/user/");
    }

}
