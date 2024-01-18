/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.config;

import com.borak.kinweb.backend.seeder.DatabaseSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class SeederRunner implements ApplicationRunner {

    private final DatabaseSeeder seeder;

    @Autowired
    public SeederRunner(DatabaseSeeder seeder) {
        this.seeder = seeder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("seed")) {
            seeder.seed();
        }
    }

}
