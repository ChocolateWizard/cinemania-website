/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.cwb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.cwb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.WriterJDBC;
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

    @Autowired
    private ConfigProperties config;
    @Autowired
    private Util util;

    public PersonResponseDTO toResponseFromJDBC(PersonWrapperJDBC wrapper) throws IllegalArgumentException {
        if (wrapper == null || wrapper.getPerson() == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        PersonResponseDTO person = new PersonResponseDTO();
        person.setId(wrapper.getPerson().getId());
        person.setFirstName(wrapper.getPerson().getFirstName());
        person.setLastName(wrapper.getPerson().getLastName());
        person.setGender(wrapper.getPerson().getGender());
        if (wrapper.getPerson().getProfilePhoto() != null && !wrapper.getPerson().getProfilePhoto().isEmpty()) {
            person.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + wrapper.getPerson().getProfilePhoto());
        }
        if (wrapper.getDirector() != null && wrapper.getDirector() instanceof DirectorJDBC) {
            PersonResponseDTO.Director director = new PersonResponseDTO.Director();
            director.setWorkedOn(wrapper.getDirector().getMedias().stream().map(MediaJDBC::getId).collect(Collectors.toList()));
            person.getProfessions().add(director);
        }
        if (wrapper.getWriter() != null && wrapper.getWriter() instanceof WriterJDBC) {
            PersonResponseDTO.Writer writer = new PersonResponseDTO.Writer();
            writer.setWorkedOn(wrapper.getWriter().getMedias().stream().map(MediaJDBC::getId).collect(Collectors.toList()));
            person.getProfessions().add(writer);
        }
        if (wrapper.getActor() != null && wrapper.getActor() instanceof ActorJDBC) {
            PersonResponseDTO.Actor actor = new PersonResponseDTO.Actor();
            actor.setStar(wrapper.getActor().isStar());
            List<PersonResponseDTO.Actor.Acting> actings = new ArrayList<>();
            for (ActingJDBC acting : wrapper.getActor().getActings()) {
                PersonResponseDTO.Actor.Acting responseActing = new PersonResponseDTO.Actor.Acting();
                responseActing.setMediaId(acting.getMedia().getId());
                responseActing.setStarring(acting.isStarring());
                for (ActingRoleJDBC role : acting.getRoles()) {
                    responseActing.getRoles().add(new PersonResponseDTO.Actor.Acting.Role(role.getId(), role.getName()));
                }
                actings.add(responseActing);
            }
            actor.setWorkedOn(actings);
            person.getProfessions().add(actor);
        }
        return person;
    }

    public PersonResponseDTO toResponseFromJPA(PersonJPA wrapper, String option) throws IllegalArgumentException {
        if (wrapper == null || option == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(wrapper.getId());
        response.setFirstName(wrapper.getFirstName());
        response.setLastName(wrapper.getLastName());
        response.setGender(wrapper.getGender());
        if (wrapper.getProfilePhoto() != null && !wrapper.getProfilePhoto().isEmpty()) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + wrapper.getProfilePhoto());
        }
        if (wrapper.getDirectorInfo() != null) {
            PersonResponseDTO.Director director = new PersonResponseDTO.Director();
            if (option.equals("workedOn")) {
                director.setWorkedOn(wrapper.getDirectorInfo().getMedias().stream().map(MediaJPA::getId).collect(Collectors.toList()));
            }
            response.getProfessions().add(director);
        }
        if (wrapper.getWriterInfo() != null) {
            PersonResponseDTO.Writer writer = new PersonResponseDTO.Writer();
            if (option.equals("workedOn")) {
                writer.setWorkedOn(wrapper.getWriterInfo().getMedias().stream().map(MediaJPA::getId).collect(Collectors.toList()));
            }
            response.getProfessions().add(writer);
        }
        if (wrapper.getActorInfo() != null) {
            PersonResponseDTO.Actor actor = new PersonResponseDTO.Actor();
            actor.setStar(wrapper.getActorInfo().getStar());
            if (option.equals("workedOn")) {
                List<PersonResponseDTO.Actor.Acting> actings = new ArrayList<>();
                for (ActingJPA acting : wrapper.getActorInfo().getActings()) {
                    PersonResponseDTO.Actor.Acting responseActing = new PersonResponseDTO.Actor.Acting();
                    responseActing.setMediaId(acting.getMedia().getId());
                    responseActing.setStarring(acting.getStarring());
                    for (ActingRoleJPA role : acting.getRoles()) {
                        responseActing.getRoles().add(new PersonResponseDTO.Actor.Acting.Role(role.getId().getId(), role.getName()));
                    }
                    actings.add(responseActing);
                }
                actor.setWorkedOn(actings);
            }
            response.getProfessions().add(actor);
        }
        return response;
    }

    public PersonWrapperJDBC toPersonWrapperJDBC(PersonRequestDTO request) throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
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
                if (profession instanceof PersonRequestDTO.Director director) {
                    List<Long> sortedIds = util.sortAsc(director.getWorkedOn());
                    List<MediaJDBC> medias = sortedIds.stream().map(id -> new MediaJDBC(id)).collect(Collectors.toList());
                    d = new DirectorJDBC(request.getId());
                    d.setMedias(medias);
                }
                if (profession instanceof PersonRequestDTO.Writer writer) {
                    List<Long> sortedIds = util.sortAsc(writer.getWorkedOn());
                    List<MediaJDBC> medias = sortedIds.stream().map(id -> new MediaJDBC(id)).collect(Collectors.toList());
                    w = new WriterJDBC(request.getId());
                    w.setMedias(medias);
                }
                if (profession instanceof PersonRequestDTO.Actor actor) {
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
            }
        }
        return new PersonWrapperJDBC(p, d, w, a);
    }

    public PersonJPA toPersonJPA(PersonRequestDTO request) throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
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
                if (profession instanceof PersonRequestDTO.Director director) {
                    List<Long> sortedIds = util.sortAsc(director.getWorkedOn());
                    List<MediaJPA> medias = sortedIds.stream().map(id -> new MediaJPA(id)).collect(Collectors.toList());
                    d = new DirectorJPA(request.getId(), person);
                    d.setMedias(medias);
                    continue;
                }
                if (profession instanceof PersonRequestDTO.Writer writer) {
                    List<Long> sortedIds = util.sortAsc(writer.getWorkedOn());
                    List<MediaJPA> medias = sortedIds.stream().map(id -> new MediaJPA(id)).collect(Collectors.toList());
                    w = new WriterJPA(request.getId(), person);
                    w.setMedias(medias);
                    continue;
                }
                if (profession instanceof PersonRequestDTO.Actor actor) {
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
            }
        }
        person.setDirectorInfo(d);
        person.setWriterInfo(w);
        person.setActorInfo(a);
        return person;
    }

    //=========================================================================================================
    public List<PersonResponseDTO> toResponseFromJDBC(List<PersonWrapperJDBC> wrapperList) throws IllegalArgumentException {
        if (wrapperList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<PersonResponseDTO> list = new ArrayList<>();
        for (PersonWrapperJDBC wr : wrapperList) {
            list.add(toResponseFromJDBC(wr));
        }
        return list;
    }

    public List<PersonResponseDTO> toResponseFromJPA(List<PersonJPA> personList, String option) throws IllegalArgumentException {
        if (personList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<PersonResponseDTO> list = new ArrayList<>();
        for (PersonJPA p : personList) {
            list.add(toResponseFromJPA(p, option));
        }
        return list;
    }

}
