package com.borak.kinweb.backend;

import com.borak.kinweb.backend.config.ConfigProperties;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {ConfigProperties.class})
//@ConfigurationPropertiesScan(basePackages = {"com.borak.kinweb.backend.config"})
public class KinomaniaWebsiteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinomaniaWebsiteBackendApplication.class, args);


    }

}
