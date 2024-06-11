/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.seeder;

import com.borak.cwb.backend.seeder.domain.db.ActingDB;
import com.borak.cwb.backend.seeder.domain.db.ActorDB;
import com.borak.cwb.backend.seeder.domain.db.DirectorDB;
import com.borak.cwb.backend.seeder.domain.db.MediaDB;
import com.borak.cwb.backend.seeder.domain.db.MovieDB;
import com.borak.cwb.backend.seeder.domain.db.PersonDB;
import com.borak.cwb.backend.seeder.domain.db.PersonWrapperDB;
import com.borak.cwb.backend.seeder.domain.db.TVShowDB;
import com.borak.cwb.backend.seeder.domain.db.WriterDB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class DataTransformer {

    List<PersonWrapperDB> extractPersons(List<MovieDB> moviesApi, List<TVShowDB> showsApi) {
        List<MediaDB> medias = new ArrayList<>();
        medias.addAll(moviesApi);
        medias.addAll(showsApi);
        HashMap<Long, PersonWrapperDB> map = new HashMap<>();
        for (MediaDB media : medias) {
            for (DirectorDB director : media.getDirectors()) {
                updatePersonWrapperMap(map, director, DirectorDB.class);
            }
            for (WriterDB writer : media.getWriters()) {
                updatePersonWrapperMap(map, writer, WriterDB.class);
            }
            for (ActingDB acting : media.getActings()) {
                updatePersonWrapperMap(map, acting.getActor(), ActorDB.class);
            }
        }
        return new ArrayList<>(map.values());
    }

    private void updatePersonWrapperMap(HashMap<Long, PersonWrapperDB> map, PersonDB person, Class role) {
        PersonWrapperDB pw = map.get(person.getId());
        if (pw == null) {
            pw = new PersonWrapperDB();
            pw.setPerson(new PersonDB(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getGender(),
                    person.getProfilePhotoName(),
                    person.getProfilePhotoPath(),
                    person.getProfilePhoto()));
            map.put(person.getId(), pw);
        }
        if (role == DirectorDB.class) {
            pw.setDirector((DirectorDB) person);
        } else if (role == WriterDB.class) {
            pw.setWriter((WriterDB) person);
        } else if (role == ActorDB.class) {
            pw.setActor((ActorDB) person);
        }
    }

}
