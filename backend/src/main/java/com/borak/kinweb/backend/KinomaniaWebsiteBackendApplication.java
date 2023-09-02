package com.borak.kinweb.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.borak.kinweb.backend.config"})
public class KinomaniaWebsiteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinomaniaWebsiteBackendApplication.class, args);

    }

}
