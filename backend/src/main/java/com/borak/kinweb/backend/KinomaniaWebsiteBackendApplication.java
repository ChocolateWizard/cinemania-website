package com.borak.kinweb.backend;

import com.borak.kinweb.backend.config.ConfigProperties;
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

//        int a = Integer.MAX_VALUE;
//        int b = Integer.MAX_VALUE;
//        System.out.println("A: " + a + "\nB: " + b);
//        for (int i = 0; i < 10; i++) {
//            int c = (a-1) * b;
//            System.out.println("  C: " + c);
//        }

    }

}
