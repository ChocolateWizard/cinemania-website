/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder;

import com.borak.kinweb.backend.seeder.domain.db.GenreDB;
import com.borak.kinweb.backend.seeder.domain.db.MovieDB;
import com.borak.kinweb.backend.seeder.domain.db.PersonWrapperDB;
import com.borak.kinweb.backend.seeder.domain.db.TVShowDB;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class DatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final PasswordEncoder passwordEncoder;
    private final DataRetreiver retreiver;
    private final DataTransformer transformer;
    private final Datastore datastore;

    @Autowired
    public DatabaseSeeder(PasswordEncoder passwordEncoder, DataRetreiver retreiver, DataTransformer transformer, Datastore datastore) {
        this.passwordEncoder = passwordEncoder;
        this.retreiver = retreiver;
        this.transformer = transformer;
        this.datastore = datastore;
    }

//=============================================================================================
    public void seed() throws RuntimeException {
        try {
            log.info("====>Started seeding database...");
            log.info("========>Retreiving json data from API...");
            List<GenreDB> genresApi = retreiver.getGenres();
            List<MovieDB> moviesApi = retreiver.getMovies();
            List<TVShowDB> showsApi = retreiver.getTVShows();           
            List<PersonWrapperDB> personsApi = transformer.extractPersons(moviesApi, showsApi);
            log.info("========>Collecting data...");
            datastore.storeGenres(genresApi);
            datastore.storeMovies(moviesApi);
            datastore.storeTVShows(showsApi);
            datastore.storePersons(personsApi);
            datastore.createAndStoreUsers(passwordEncoder);
            datastore.connectData();
            datastore.resetIDs();
            log.info("========>Persisting data...");
            datastore.persistData();
            log.info("====>Finished seeding database!");
        } catch (Exception e) {
            throw new RuntimeException("Unable to seed database!", e);
        }
    }

}
