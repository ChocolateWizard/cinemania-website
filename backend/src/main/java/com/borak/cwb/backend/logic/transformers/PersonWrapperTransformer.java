/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.cwb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.PersonJDBC;
import com.borak.cwb.backend.domain.jdbc.PersonWrapperJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.ActingRoleJPA;
import com.borak.cwb.backend.domain.jpa.ActorJPA;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.PersonJPA;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
import com.borak.cwb.backend.logic.util.Util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class PersonWrapperTransformer {

    private final String PERSON_IMAGES_BASE_URL;

    @Autowired
    public PersonWrapperTransformer(ConfigProperties config) {
        this.PERSON_IMAGES_BASE_URL = config.getPersonImagesBaseUrl();
    }

//=================================================================================================================================
//JPA


    public PersonResponseDTO jpaToPersonResponseWithProfessions(PersonJPA person) {
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setGender(person.getGender());
        if (person.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + person.getProfilePhoto());
        }
        if (person.getDirector() != null) {
            response.getProfessions().add(new PersonResponseDTO.Director());
        }
        if (person.getWriter() != null) {
            response.getProfessions().add(new PersonResponseDTO.Writer());
        }
        if (person.getActor() != null) {
            PersonResponseDTO.Actor actor = new PersonResponseDTO.Actor();
            actor.setStar(person.getActor().getStar());
            response.getProfessions().add(actor);
        }
        return response;
    }

    public PersonResponseDTO jpaToPersonResponseWithDetails(PersonJPA person) {
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setGender(person.getGender());
        if (person.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + person.getProfilePhoto());
        }
        if (person.getDirector() != null) {
            PersonResponseDTO.Director director = new PersonResponseDTO.Director();
            director.setWorkedOn(person.getDirector().getMedias().stream().map(MediaJPA::getId).collect(Collectors.toList()));
            response.getProfessions().add(director);
        }
        if (person.getWriter() != null) {
            PersonResponseDTO.Writer writer = new PersonResponseDTO.Writer();
            writer.setWorkedOn(person.getWriter().getMedias().stream().map(MediaJPA::getId).collect(Collectors.toList()));
            response.getProfessions().add(writer);
        }
        if (person.getActor() != null) {
            PersonResponseDTO.Actor actor = new PersonResponseDTO.Actor();
            actor.setStar(person.getActor().getStar());
            List<PersonResponseDTO.Actor.Acting> actings = new ArrayList<>();
            for (ActingJPA acting : person.getActor().getActings()) {
                PersonResponseDTO.Actor.Acting responseActing = new PersonResponseDTO.Actor.Acting();
                responseActing.setMediaId(acting.getMedia().getId());
                responseActing.setStarring(acting.getStarring());
                for (ActingRoleJPA role : acting.getRoles()) {
                    responseActing.getRoles().add(new PersonResponseDTO.Actor.Acting.Role(role.getId().getOrderNumber(), role.getName()));
                }
                actings.add(responseActing);
            }
            actor.setWorkedOn(actings);
            response.getProfessions().add(actor);
        }
        return response;
    }

    public PersonJPA toPersonJPA(PersonRequestDTO request) throws IllegalArgumentException {
        PersonJPA person = new PersonJPA();
        person.setId(request.getId());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setGender(request.getGender());
        if (request.getProfilePhoto() != null) {
            person.setProfilePhoto(request.getProfilePhoto().getFullName());
        }
        DirectorJPA d = null;
        WriterJPA w = null;
        ActorJPA a = null;
        if (request.getProfessions() != null) {
            for (PersonRequestDTO.Profession profession : request.getProfessions()) {
                switch (profession) {
                    case PersonRequestDTO.Director director -> {
                        List<Long> sortedIds = Util.sortAsc(director.getWorkedOn());
                        List<MediaJPA> medias = sortedIds.stream().map(id -> new MediaJPA(id)).collect(Collectors.toList());
                        d = new DirectorJPA(request.getId(), person);
                        d.setMedias(medias);
                    }
                    case PersonRequestDTO.Writer writer -> {
                        List<Long> sortedIds = Util.sortAsc(writer.getWorkedOn());
                        List<MediaJPA> medias = sortedIds.stream().map(id -> new MediaJPA(id)).collect(Collectors.toList());
                        w = new WriterJPA(request.getId(), person);
                        w.setMedias(medias);
                    }
                    case PersonRequestDTO.Actor actor -> {
                        List<PersonRequestDTO.Actor.Acting> pomActings = new ArrayList<>(actor.getWorkedOn());
                        pomActings.sort(Comparator.comparingLong(PersonRequestDTO.Actor.Acting::getMediaId));
                        a = new ActorJPA(request.getId(), person, actor.isStar());

                        List<ActingJPA> actings = new ArrayList<>(pomActings.size());
                        for (PersonRequestDTO.Actor.Acting pomActing : pomActings) {
                            ActingJPA acting = new ActingJPA(new MediaJPA(pomActing.getMediaId()), a, pomActing.isStarring());

                            List<ActingRoleJPA> roles = new ArrayList<>(pomActing.getRoles().size());
                            Long rId = 1l;
                            for (String role : pomActing.getRoles()) {
                                roles.add(new ActingRoleJPA(new ActingRoleJPA.ID(acting, rId), role));
                                rId++;
                            }
                            acting.setRoles(roles);
                            actings.add(acting);
                        }
                        a.setActings(actings);
                    }
                    default -> {
                        throw new IllegalArgumentException("Unknown profession!");
                    }
                }
            }
        }
        person.setDirector(d);
        person.setWriter(w);
        person.setActor(a);
        return person;
    }

//-------------------------------------------------------------------------------------------------    
    public List<PersonResponseDTO> jpaToPersonResponseWithProfessions(List<PersonJPA> persons) {
        List<PersonResponseDTO> list = new ArrayList<>(persons.size());
        for (PersonJPA person : persons) {
            list.add(jpaToPersonResponseWithProfessions(person));
        }
        return list;
    }

    public List<PersonResponseDTO> jpaToPersonResponseWithDetails(List<PersonJPA> persons) {
        List<PersonResponseDTO> list = new ArrayList<>(persons.size());
        for (PersonJPA person : persons) {
            list.add(jpaToPersonResponseWithDetails(person));
        }
        return list;
    }

//=================================================================================================================================
//JDBC
    public PersonResponseDTO jdbcToPersonResponse(PersonWrapperJDBC person) {
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(person.getPerson().getId());
        response.setFirstName(person.getPerson().getFirstName());
        response.setLastName(person.getPerson().getLastName());
        response.setGender(person.getPerson().getGender());
        if (person.getPerson().getProfilePhoto() != null) {
            response.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + person.getPerson().getProfilePhoto());
        }
        if (person.getDirector() != null) {
            PersonResponseDTO.Director director = new PersonResponseDTO.Director();
            director.setWorkedOn(person.getDirector().getMedias().stream().map(MediaJDBC::getId).collect(Collectors.toList()));
            response.getProfessions().add(director);
        }
        if (person.getWriter() != null) {
            PersonResponseDTO.Writer writer = new PersonResponseDTO.Writer();
            writer.setWorkedOn(person.getWriter().getMedias().stream().map(MediaJDBC::getId).collect(Collectors.toList()));
            response.getProfessions().add(writer);
        }
        if (person.getActor() != null) {
            PersonResponseDTO.Actor actor = new PersonResponseDTO.Actor();
            actor.setStar(person.getActor().isStar());
            List<PersonResponseDTO.Actor.Acting> actings = new ArrayList<>();
            for (ActingJDBC acting : person.getActor().getActings()) {
                PersonResponseDTO.Actor.Acting responseActing = new PersonResponseDTO.Actor.Acting();
                responseActing.setMediaId(acting.getMedia().getId());
                responseActing.setStarring(acting.isStarring());
                for (ActingRoleJDBC role : acting.getRoles()) {
                    responseActing.getRoles().add(new PersonResponseDTO.Actor.Acting.Role(role.getId(), role.getName()));
                }
                actings.add(responseActing);
            }
            actor.setWorkedOn(actings);
            response.getProfessions().add(actor);
        }
        return response;
    }

    public PersonWrapperJDBC toPersonWrapperJDBC(PersonRequestDTO request) throws IllegalArgumentException {
        PersonJDBC p = new PersonJDBC();
        p.setId(request.getId());
        p.setFirstName(request.getFirstName());
        p.setLastName(request.getLastName());
        p.setGender(request.getGender());
        if (request.getProfilePhoto() != null) {
            p.setProfilePhoto(request.getProfilePhoto().getFullName());
        }
        DirectorJDBC d = null;
        WriterJDBC w = null;
        ActorJDBC a = null;
        if (request.getProfessions() != null) {
            for (PersonRequestDTO.Profession profession : request.getProfessions()) {
                switch (profession) {
                    case PersonRequestDTO.Director director -> {
                        List<Long> sortedIds = Util.sortAsc(director.getWorkedOn());
                        List<MediaJDBC> medias = sortedIds.stream().map(id -> new MediaJDBC(id)).collect(Collectors.toList());
                        d = new DirectorJDBC(request.getId());
                        d.setMedias(medias);
                    }
                    case PersonRequestDTO.Writer writer -> {
                        List<Long> sortedIds = Util.sortAsc(writer.getWorkedOn());
                        List<MediaJDBC> medias = sortedIds.stream().map(id -> new MediaJDBC(id)).collect(Collectors.toList());
                        w = new WriterJDBC(request.getId());
                        w.setMedias(medias);
                    }
                    case PersonRequestDTO.Actor actor -> {
                        List<PersonRequestDTO.Actor.Acting> pomActings = new ArrayList<>(actor.getWorkedOn());
                        pomActings.sort(Comparator.comparingLong(PersonRequestDTO.Actor.Acting::getMediaId));
                        a = new ActorJDBC(request.getId());

                        List<ActingJDBC> actings = new ArrayList<>(pomActings.size());
                        for (PersonRequestDTO.Actor.Acting pomActing : pomActings) {
                            ActingJDBC acting = new ActingJDBC(new MediaJDBC(pomActing.getMediaId()), a, pomActing.isStarring());

                            List<ActingRoleJDBC> roles = new ArrayList<>(pomActing.getRoles().size());
                            Long rId = 1l;
                            for (String role : pomActing.getRoles()) {
                                roles.add(new ActingRoleJDBC(acting, rId, role));
                                rId++;
                            }
                            acting.setRoles(roles);
                            actings.add(acting);
                        }
                        a.setStar(actor.isStar());
                        a.setActings(actings);
                    }
                    default -> {
                        throw new IllegalArgumentException("Unknown profession!");
                    }
                }
            }
        }
        return new PersonWrapperJDBC(p, d, w, a);
    }

//-------------------------------------------------------------------------------------------------  
    public List<PersonResponseDTO> jdbcToPersonResponse(List<PersonWrapperJDBC> persons) {
        List<PersonResponseDTO> list = new ArrayList<>(persons.size());
        for (PersonWrapperJDBC person : persons) {
            list.add(jdbcToPersonResponse(person));
        }
        return list;
    }
}
