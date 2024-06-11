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

    public PersonResponseDTO toPersonResponseDTO(PersonWrapperJDBC wrapper) throws IllegalArgumentException {
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

    public List<PersonResponseDTO> toPersonResponseDTO(List<PersonWrapperJDBC> wrapperList) throws IllegalArgumentException {
        if (wrapperList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<PersonResponseDTO> list = new ArrayList<>();
        for (PersonWrapperJDBC wr : wrapperList) {
            list.add(toPersonResponseDTO(wr));
        }
        return list;
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

}
