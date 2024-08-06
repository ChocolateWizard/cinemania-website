package com.borak.cwb.backend;

import com.borak.cwb.backend.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(value = {ConfigProperties.class})
/*
This signals spring boot at which package to search for my jpa repository interfaces. This isn't necesseary
as spring will search via classpath for those interfaces, but this way it improves startup time by half a second
 */
@EnableJpaRepositories(basePackages = "com.borak.cwb.backend.repository.jpa")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
