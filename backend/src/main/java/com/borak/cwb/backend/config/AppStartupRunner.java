/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.config;

import com.borak.cwb.backend.exceptions.StartupException;
import com.borak.cwb.backend.seeder.DatabaseSeeder;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class AppStartupRunner implements ApplicationRunner {

    private final DatabaseSeeder seeder;
    private final ConfigProperties config;
    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    public AppStartupRunner(DatabaseSeeder seeder, ConfigProperties config) {
        this.seeder = seeder;
        this.config = config;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //runs database seeder if keyword --seed was present during the start of this app
        if (args.containsOption("seed")) {
            seeder.seed();
        }
        validateStartupConditions();
        log.info("Application has started");
    }

    private void validateStartupConditions() throws StartupException {
        if (!Files.exists(Paths.get(config.getMediaImagesFolderPath()))) {
            throw new StartupException("Media images database folder not present!");
        }
        if (!Files.exists(Paths.get(config.getPersonImagesFolderPath()))) {
            throw new StartupException("Person images database folder not present!");
        }
        if (!Files.exists(Paths.get(config.getUserImagesFolderPath()))) {
            throw new StartupException("User images database folder not present!");
        }
    }

}
