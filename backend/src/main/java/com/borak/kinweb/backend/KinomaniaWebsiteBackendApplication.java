package com.borak.kinweb.backend;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.enums.Gender;
import static com.borak.kinweb.backend.domain.enums.Gender.FEMALE;
import static com.borak.kinweb.backend.domain.enums.Gender.MALE;
import static com.borak.kinweb.backend.domain.enums.Gender.OTHER;
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


//        String pom = "...txt.png.txt";
//        String pom2 = "    ..        ..          ..          txt.";
//        String pom3 = "";
//        String pom4 = ".";
//        System.out.println("Index: " + pom.lastIndexOf(".") + ", SIze: " + pom.length());
//        System.out.println("Index: " + pom2.lastIndexOf(".") + ", SIze: " + pom2.length());
//        System.out.println("Index: " + pom3.lastIndexOf(".") + ", SIze: " + pom3.length());
//        System.out.println("Index: " + pom4.lastIndexOf(".") + ", SIze: " + pom4.length());
    }

}
