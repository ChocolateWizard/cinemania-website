/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.seeder.domain.db.ActorDB;
import com.borak.kinweb.backend.seeder.domain.db.CountryDB;
import com.borak.kinweb.backend.seeder.domain.db.DirectorDB;
import com.borak.kinweb.backend.seeder.domain.db.GenreDB;
import com.borak.kinweb.backend.seeder.domain.db.MediaDB;
import com.borak.kinweb.backend.seeder.domain.db.MovieDB;
import com.borak.kinweb.backend.seeder.domain.db.PersonWrapperDB;
import com.borak.kinweb.backend.seeder.domain.db.TVShowDB;
import com.borak.kinweb.backend.seeder.domain.db.UserDB;
import com.borak.kinweb.backend.seeder.domain.db.WriterDB;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class Datastore {

    private List<GenreDB> genres = new ArrayList<>();
    private List<MovieDB> movies = new ArrayList<>();
    private List<TVShowDB> shows = new ArrayList<>();
    private List<PersonWrapperDB> persons = new ArrayList<>();
    private UserDB user;

    private final Database database;
    private static final Logger log = LoggerFactory.getLogger(Datastore.class);

    @Autowired
    public Datastore(Database database) {
        this.database = database;
    }

    void storeGenres(List<GenreDB> genres) {
        this.genres = genres;
        Collections.sort(this.genres, (g1, g2) -> g1.getName().compareTo(g2.getName()));
    }

    void storeMovies(List<MovieDB> moviesApi) {
        this.movies = moviesApi;
    }

    void storeTVShows(List<TVShowDB> showsApi) {
        this.shows = showsApi;
    }

    void storePersons(List<PersonWrapperDB> personsApi) {
        this.persons = personsApi;
    }

    void createAndStoreUser(PasswordEncoder passwordEncoder) {
        LocalDateTime now = LocalDateTime.now();
        this.user = new UserDB(1l,
                "Admin",
                "Admin",
                Gender.OTHER,
                "Admin",
                "default.png",
                "admin",
                "admin@gmail.com",
                passwordEncoder.encode("admin"),
                UserRole.ADMINISTRATOR,
                now,
                now,
                new CountryDB(198l));
    }

    void connectData() throws Exception {
        List<MediaDB> medias = new ArrayList<>();
        medias.addAll(movies);
        medias.addAll(shows);
        for (int mi = 0; mi < medias.size(); mi++) {
            MediaDB currMedia = medias.get(mi);
            for (int gi = 0; gi < currMedia.getGenres().size(); gi++) {
                currMedia.getGenres()
                        .set(gi, genres.get(
                                genres.indexOf(
                                        currMedia.getGenres().get(gi))));
            }
            for (int di = 0; di < currMedia.getDirectors().size(); di++) {
                currMedia.getDirectors().set(di, findDirector(currMedia.getDirectors().get(di)));
            }
            for (int wi = 0; wi < currMedia.getWriters().size(); wi++) {
                currMedia.getWriters().set(wi, findWriter(currMedia.getWriters().get(wi)));
            }
            for (int ai = 0; ai < currMedia.getActings().size(); ai++) {
                currMedia.getActings().get(ai).setActor(findActor(currMedia.getActings().get(ai).getActor()));
            }
        }
    }

    void resetIDs() {
        long i = 1;
        for (GenreDB genre : genres) {
            genre.setId(i++);
        }
        i = 1;
        for (MovieDB movie : movies) {
            movie.setId(i++);
        }
        for (TVShowDB show : shows) {
            show.setId(i++);
        }
        i = 1;
        for (PersonWrapperDB person : persons) {
            person.setId(i++);
        }
        i = 1;
        user.setId(i);
    }

    void persistData() throws Exception {
        log.info("----------->Removing all table data...");
        database.removeAllTableData();
        log.info("----------->Storing genres table data...");
        database.storeAllGenres(genres);
        log.info("----------->Storing persons table data...");
        database.storeAllPersons(persons);
        log.info("----------->Storing movies table data...");
        database.storeAllMovies(movies);
        log.info("----------->Storing tv shows table data...");
        database.storeAllShows(shows);
        log.info("----------->Storing user table data...");
        database.storeUser(user);
        log.info("----------->Removing all images...");
        database.removeAllImages();
        List<MediaDB> medias = new ArrayList<>();
        medias.addAll(movies);
        medias.addAll(shows);
        log.info("----------->Retreiving and persisting media images...");
        database.storeMediaImages(medias);
        log.info("----------->Retreiving and persisting persons images...");
        database.storePersonImages(persons);

        log.info("----------->Persisting user image...");
        database.storeUserImage(user);
        
        //remove pointers to arrays of data so garbage collector can dispose of them
        genres = new ArrayList<>();
        movies = new ArrayList<>();
        shows = new ArrayList<>();
        persons = new ArrayList<>();
        user = null;
    }
//==============================================================================================================

    private DirectorDB findDirector(DirectorDB director) throws Exception {
        for (int pi = 0; pi < persons.size(); pi++) {
            if (persons.get(pi).getPerson().getId().equals(director.getId())) {
                return persons.get(pi).getDirector();
            }
        }
        throw new Exception("There should have been a coresponding director in persons list!");
    }

    private WriterDB findWriter(WriterDB writer) throws Exception {
        for (int pi = 0; pi < persons.size(); pi++) {
            if (persons.get(pi).getPerson().getId().equals(writer.getId())) {
                return persons.get(pi).getWriter();
            }
        }
        throw new Exception("There should have been a coresponding writer in persons list!");
    }

    private ActorDB findActor(ActorDB actor) throws Exception {
        for (int pi = 0; pi < persons.size(); pi++) {
            if (persons.get(pi).getPerson().getId().equals(actor.getId())) {
                return persons.get(pi).getActor();
            }
        }
        throw new Exception("There should have been a coresponding actor in persons list!");
    }

}
