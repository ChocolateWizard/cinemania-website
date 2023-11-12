/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.repository.jdbc.MovieRepositoryJDBC;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(2)
public class MovieRepositoryJDBCTest {

    @Autowired
    private MovieRepositoryJDBC repo;

    @Test
    @Order(1)
    @DisplayName("Tests normal functionality of findAllNoRelationship method of MovieRepositoryJDBC class")
    void findAllNoRelationShip_Test() {
        List<MovieJDBC> movies = repo.findAllNoRelationships();

        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(4);

        assertThat(movies.get(0)).isNotNull();
        assertThat(movies.get(0).getId()).isNotNull().isEqualTo(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Mulholland Drive");
        assertThat(movies.get(0).getCoverImage()).isEqualTo("1.jpg");
        assertThat(movies.get(0).getReleaseDate()).isEqualTo(LocalDate.of(2001, Month.MAY, 16));
        assertThat(movies.get(0).getDescription()).isEqualTo("After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.");
        assertThat(movies.get(0).getAudienceRating()).isEqualTo(79);
        assertThat(movies.get(0).getLength()).isEqualTo(147);

        assertThat(movies.get(1)).isNotNull();
        assertThat(movies.get(1).getId()).isNotNull().isEqualTo(2);
        assertThat(movies.get(1).getTitle()).isEqualTo("Inland Empire");
        assertThat(movies.get(1).getCoverImage()).isEqualTo("2.jpg");
        assertThat(movies.get(1).getReleaseDate()).isEqualTo(LocalDate.of(2006, Month.SEPTEMBER, 6));
        assertThat(movies.get(1).getDescription()).isEqualTo("As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.");
        assertThat(movies.get(1).getAudienceRating()).isEqualTo(68);
        assertThat(movies.get(1).getLength()).isEqualTo(180);

        assertThat(movies.get(2)).isNotNull();
        assertThat(movies.get(2).getId()).isNotNull().isEqualTo(4);
        assertThat(movies.get(2).getTitle()).isEqualTo("The Lighthouse");
        assertThat(movies.get(2).getCoverImage()).isEqualTo("4.jpg");
        assertThat(movies.get(2).getReleaseDate()).isEqualTo(LocalDate.of(2019, Month.MAY, 19));
        assertThat(movies.get(2).getDescription()).isEqualTo("Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.");
        assertThat(movies.get(2).getAudienceRating()).isEqualTo(74);
        assertThat(movies.get(2).getLength()).isEqualTo(109);

        assertThat(movies.get(3)).isNotNull();
        assertThat(movies.get(3).getId()).isNotNull().isEqualTo(5);
        assertThat(movies.get(3).getTitle()).isEqualTo("2001: A Space Odyssey");
        assertThat(movies.get(3).getCoverImage()).isNull();
        assertThat(movies.get(3).getReleaseDate()).isEqualTo(LocalDate.of(1968, Month.APRIL, 2));
        assertThat(movies.get(3).getDescription()).isEqualTo("After uncovering a mysterious artifact buried beneath the Lunar surface, a spacecraft is sent to Jupiter to find its origins: a spacecraft manned by two men and the supercomputer HAL 9000.");
        assertThat(movies.get(3).getAudienceRating()).isEqualTo(83);
        assertThat(movies.get(3).getLength()).isEqualTo(149);

        for (MovieJDBC movie : movies) {
            assertThat(movie.getCriticRating()).isNull();
            assertThat(movie.getGenres()).isNotNull().isEmpty();
            assertThat(movie.getDirectors()).isNotNull().isEmpty();
            assertThat(movie.getWriters()).isNotNull().isEmpty();
            assertThat(movie.getCritiques()).isNotNull().isEmpty();
            assertThat(movie.getActings()).isNotNull().isEmpty();
        }

    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of findAllRelationshipGenres method of MovieRepositoryJDBC class")
    void findAllRelationshipGenres_Test() {
        List<MovieJDBC> movies = repo.findAllRelationshipGenres();

        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(4);

        assertThat(movies.get(0)).isNotNull();
        assertThat(movies.get(0).getId()).isNotNull().isEqualTo(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Mulholland Drive");
        assertThat(movies.get(0).getCoverImage()).isEqualTo("1.jpg");
        assertThat(movies.get(0).getReleaseDate()).isEqualTo(LocalDate.of(2001, Month.MAY, 16));
        assertThat(movies.get(0).getDescription()).isEqualTo("After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.");
        assertThat(movies.get(0).getAudienceRating()).isEqualTo(79);
        assertThat(movies.get(0).getLength()).isEqualTo(147);

        assertThat(movies.get(0).getGenres()).isNotNull();
        assertThat(movies.get(0).getGenres().size()).isEqualTo(3);

        assertThat(movies.get(0).getGenres().get(0)).isNotNull();
        assertThat(movies.get(0).getGenres().get(0).getId()).isEqualTo(6);
        assertThat(movies.get(0).getGenres().get(0).getName()).isEqualTo("Drama");

        assertThat(movies.get(0).getGenres().get(1)).isNotNull();
        assertThat(movies.get(0).getGenres().get(1).getId()).isEqualTo(11);
        assertThat(movies.get(0).getGenres().get(1).getName()).isEqualTo("Thriller");

        assertThat(movies.get(0).getGenres().get(2)).isNotNull();
        assertThat(movies.get(0).getGenres().get(2).getId()).isEqualTo(12);
        assertThat(movies.get(0).getGenres().get(2).getName()).isEqualTo("Mystery");

        assertThat(movies.get(1)).isNotNull();
        assertThat(movies.get(1).getId()).isNotNull().isEqualTo(2);
        assertThat(movies.get(1).getTitle()).isEqualTo("Inland Empire");
        assertThat(movies.get(1).getCoverImage()).isEqualTo("2.jpg");
        assertThat(movies.get(1).getReleaseDate()).isEqualTo(LocalDate.of(2006, Month.SEPTEMBER, 6));
        assertThat(movies.get(1).getDescription()).isEqualTo("As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.");
        assertThat(movies.get(1).getAudienceRating()).isEqualTo(68);
        assertThat(movies.get(1).getLength()).isEqualTo(180);

        assertThat(movies.get(1).getGenres()).isNotNull();
        assertThat(movies.get(1).getGenres().size()).isEqualTo(3);

        assertThat(movies.get(1).getGenres().get(0)).isNotNull();
        assertThat(movies.get(1).getGenres().get(0).getId()).isEqualTo(6);
        assertThat(movies.get(1).getGenres().get(0).getName()).isEqualTo("Drama");

        assertThat(movies.get(1).getGenres().get(1)).isNotNull();
        assertThat(movies.get(1).getGenres().get(1).getId()).isEqualTo(12);
        assertThat(movies.get(1).getGenres().get(1).getName()).isEqualTo("Mystery");

        assertThat(movies.get(1).getGenres().get(2)).isNotNull();
        assertThat(movies.get(1).getGenres().get(2).getId()).isEqualTo(13);
        assertThat(movies.get(1).getGenres().get(2).getName()).isEqualTo("Fantasy");

        assertThat(movies.get(2)).isNotNull();
        assertThat(movies.get(2).getId()).isNotNull().isEqualTo(4);
        assertThat(movies.get(2).getTitle()).isEqualTo("The Lighthouse");
        assertThat(movies.get(2).getCoverImage()).isEqualTo("4.jpg");
        assertThat(movies.get(2).getReleaseDate()).isEqualTo(LocalDate.of(2019, Month.MAY, 19));
        assertThat(movies.get(2).getDescription()).isEqualTo("Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.");
        assertThat(movies.get(2).getAudienceRating()).isEqualTo(74);
        assertThat(movies.get(2).getLength()).isEqualTo(109);

        assertThat(movies.get(2).getGenres()).isNotNull();
        assertThat(movies.get(2).getGenres().size()).isEqualTo(3);

        assertThat(movies.get(2).getGenres().get(0)).isNotNull();
        assertThat(movies.get(2).getGenres().get(0).getId()).isEqualTo(6);
        assertThat(movies.get(2).getGenres().get(0).getName()).isEqualTo("Drama");

        assertThat(movies.get(2).getGenres().get(1)).isNotNull();
        assertThat(movies.get(2).getGenres().get(1).getId()).isEqualTo(8);
        assertThat(movies.get(2).getGenres().get(1).getName()).isEqualTo("Horror");

        assertThat(movies.get(2).getGenres().get(2)).isNotNull();
        assertThat(movies.get(2).getGenres().get(2).getId()).isEqualTo(13);
        assertThat(movies.get(2).getGenres().get(2).getName()).isEqualTo("Fantasy");

        assertThat(movies.get(3)).isNotNull();
        assertThat(movies.get(3).getId()).isNotNull().isEqualTo(5);
        assertThat(movies.get(3).getTitle()).isEqualTo("2001: A Space Odyssey");
        assertThat(movies.get(3).getCoverImage()).isNull();
        assertThat(movies.get(3).getReleaseDate()).isEqualTo(LocalDate.of(1968, Month.APRIL, 2));
        assertThat(movies.get(3).getDescription()).isEqualTo("After uncovering a mysterious artifact buried beneath the Lunar surface, a spacecraft is sent to Jupiter to find its origins: a spacecraft manned by two men and the supercomputer HAL 9000.");
        assertThat(movies.get(3).getAudienceRating()).isEqualTo(83);
        assertThat(movies.get(3).getLength()).isEqualTo(149);

        assertThat(movies.get(3).getGenres()).isNotNull().isEmpty();

        for (MovieJDBC movie : movies) {
            assertThat(movie.getCriticRating()).isNull();
            for (GenreJDBC genre : movie.getGenres()) {
                assertThat(genre.getMedias()).isNotNull().isEmpty();
            }
            assertThat(movie.getCritiques()).isNotNull().isEmpty();
            assertThat(movie.getDirectors()).isNotNull().isEmpty();
            assertThat(movie.getWriters()).isNotNull().isEmpty();
            assertThat(movie.getActings()).isNotNull().isEmpty();
        }

    }

    @Test
    @Order(3)
    @DisplayName("Tests normal functionality of count method of MovieRepositoryJDBC class")
    void count_Test() {
        long count = repo.count();
        assertThat(count).isEqualTo(4);
    }

    @Test
    @Order(4)
    @DisplayName("Tests normal functionality of findAll method of MovieRepositoryJDBC class")
    void findAll_Test() {
        List<MovieJDBC> movies = repo.findAll();

        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(4);
        //==========================================================================
        //Test movie num 1
        //==========================================================================
        assertThat(movies.get(0)).isNotNull();
        assertThat(movies.get(0).getId()).isNotNull().isEqualTo(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Mulholland Drive");
        assertThat(movies.get(0).getCoverImage()).isEqualTo("1.jpg");
        assertThat(movies.get(0).getReleaseDate()).isEqualTo(LocalDate.of(2001, Month.MAY, 16));
        assertThat(movies.get(0).getDescription()).isEqualTo("After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.");
        assertThat(movies.get(0).getAudienceRating()).isEqualTo(79);
        assertThat(movies.get(0).getLength()).isEqualTo(147);

        assertThat(movies.get(0).getGenres()).isNotNull();
        assertThat(movies.get(0).getGenres().size()).isEqualTo(3);

        assertThat(movies.get(0).getGenres().get(0)).isNotNull();
        assertThat(movies.get(0).getGenres().get(0).getId()).isEqualTo(6);
        assertThat(movies.get(0).getGenres().get(0).getName()).isEqualTo("Drama");

        assertThat(movies.get(0).getGenres().get(1)).isNotNull();
        assertThat(movies.get(0).getGenres().get(1).getId()).isEqualTo(11);
        assertThat(movies.get(0).getGenres().get(1).getName()).isEqualTo("Thriller");

        assertThat(movies.get(0).getGenres().get(2)).isNotNull();
        assertThat(movies.get(0).getGenres().get(2).getId()).isEqualTo(12);
        assertThat(movies.get(0).getGenres().get(2).getName()).isEqualTo("Mystery");

        assertThat(movies.get(0).getDirectors()).isNotNull();
        assertThat(movies.get(0).getDirectors().size()).isEqualTo(1);

        assertThat(movies.get(0).getDirectors().get(0)).isNotNull();
        assertThat(movies.get(0).getDirectors().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getDirectors().get(0).getFirstName()).isEqualTo("David");
        assertThat(movies.get(0).getDirectors().get(0).getLastName()).isEqualTo("Lynch");
        assertThat(movies.get(0).getDirectors().get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(0).getDirectors().get(0).getProfilePhoto()).isEqualTo("1.jpg");

        assertThat(movies.get(0).getWriters()).isNotNull();
        assertThat(movies.get(0).getWriters().size()).isEqualTo(1);

        assertThat(movies.get(0).getWriters().get(0)).isNotNull();
        assertThat(movies.get(0).getWriters().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getWriters().get(0).getFirstName()).isEqualTo("David");
        assertThat(movies.get(0).getWriters().get(0).getLastName()).isEqualTo("Lynch");
        assertThat(movies.get(0).getWriters().get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(0).getWriters().get(0).getProfilePhoto()).isEqualTo("1.jpg");

        assertThat(movies.get(0).getActings()).isNotNull();
        assertThat(movies.get(0).getActings().size()).isEqualTo(5);

        assertThat(movies.get(0).getActings().get(0)).isNotNull();
        assertThat(movies.get(0).getActings().get(0).isStarring()).isTrue();
        assertThat(movies.get(0).getActings().get(0).getActor()).isNotNull();
        assertThat(movies.get(0).getActings().get(0).getActor().getId()).isEqualTo(2);
        assertThat(movies.get(0).getActings().get(0).getActor().getFirstName()).isEqualTo("Naomi");
        assertThat(movies.get(0).getActings().get(0).getActor().getLastName()).isEqualTo("Watts");
        assertThat(movies.get(0).getActings().get(0).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(0).getActings().get(0).getActor().getProfilePhoto()).isEqualTo("2.jpg");
        assertThat(movies.get(0).getActings().get(0).getActor().isStar()).isTrue();

        assertThat(movies.get(0).getActings().get(0).getRoles()).isNotNull();
        assertThat(movies.get(0).getActings().get(0).getRoles().size()).isEqualTo(2);

        assertThat(movies.get(0).getActings().get(0).getRoles().get(0)).isNotNull();
        assertThat(movies.get(0).getActings().get(0).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getActings().get(0).getRoles().get(0).getName()).isEqualTo("Betty Elms");

        assertThat(movies.get(0).getActings().get(0).getRoles().get(1)).isNotNull();
        assertThat(movies.get(0).getActings().get(0).getRoles().get(1).getId()).isEqualTo(2);
        assertThat(movies.get(0).getActings().get(0).getRoles().get(1).getName()).isEqualTo("Diane Selwyn");

        assertThat(movies.get(0).getActings().get(1)).isNotNull();
        assertThat(movies.get(0).getActings().get(1).isStarring()).isTrue();
        assertThat(movies.get(0).getActings().get(1).getActor()).isNotNull();
        assertThat(movies.get(0).getActings().get(1).getActor().getId()).isEqualTo(3);
        assertThat(movies.get(0).getActings().get(1).getActor().getFirstName()).isEqualTo("Laura");
        assertThat(movies.get(0).getActings().get(1).getActor().getLastName()).isEqualTo("Harring");
        assertThat(movies.get(0).getActings().get(1).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(0).getActings().get(1).getActor().getProfilePhoto()).isEqualTo("3.jpg");
        assertThat(movies.get(0).getActings().get(1).getActor().isStar()).isTrue();

        assertThat(movies.get(0).getActings().get(1).getRoles()).isNotNull();
        assertThat(movies.get(0).getActings().get(1).getRoles().size()).isEqualTo(2);

        assertThat(movies.get(0).getActings().get(1).getRoles().get(0)).isNotNull();
        assertThat(movies.get(0).getActings().get(1).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getActings().get(1).getRoles().get(0).getName()).isEqualTo("Rita");

        assertThat(movies.get(0).getActings().get(1).getRoles().get(1)).isNotNull();
        assertThat(movies.get(0).getActings().get(1).getRoles().get(1).getId()).isEqualTo(2);
        assertThat(movies.get(0).getActings().get(1).getRoles().get(1).getName()).isEqualTo("Camilla Rhodes");

        assertThat(movies.get(0).getActings().get(2)).isNotNull();
        assertThat(movies.get(0).getActings().get(2).isStarring()).isTrue();
        assertThat(movies.get(0).getActings().get(2).getActor()).isNotNull();
        assertThat(movies.get(0).getActings().get(2).getActor().getId()).isEqualTo(4);
        assertThat(movies.get(0).getActings().get(2).getActor().getFirstName()).isEqualTo("Justin");
        assertThat(movies.get(0).getActings().get(2).getActor().getLastName()).isEqualTo("Theroux");
        assertThat(movies.get(0).getActings().get(2).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(0).getActings().get(2).getActor().getProfilePhoto()).isEqualTo("4.jpg");
        assertThat(movies.get(0).getActings().get(2).getActor().isStar()).isTrue();

        assertThat(movies.get(0).getActings().get(2).getRoles()).isNotNull();
        assertThat(movies.get(0).getActings().get(2).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(0).getActings().get(2).getRoles().get(0)).isNotNull();
        assertThat(movies.get(0).getActings().get(2).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getActings().get(2).getRoles().get(0).getName()).isEqualTo("Adam");

        assertThat(movies.get(0).getActings().get(3)).isNotNull();
        assertThat(movies.get(0).getActings().get(3).isStarring()).isFalse();
        assertThat(movies.get(0).getActings().get(3).getActor()).isNotNull();
        assertThat(movies.get(0).getActings().get(3).getActor().getId()).isEqualTo(5);
        assertThat(movies.get(0).getActings().get(3).getActor().getFirstName()).isEqualTo("Patrick");
        assertThat(movies.get(0).getActings().get(3).getActor().getLastName()).isEqualTo("Fischler");
        assertThat(movies.get(0).getActings().get(3).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(0).getActings().get(3).getActor().getProfilePhoto()).isEqualTo("5.jpg");
        assertThat(movies.get(0).getActings().get(3).getActor().isStar()).isFalse();

        assertThat(movies.get(0).getActings().get(3).getRoles()).isNotNull();
        assertThat(movies.get(0).getActings().get(3).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(0).getActings().get(3).getRoles().get(0)).isNotNull();
        assertThat(movies.get(0).getActings().get(3).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getActings().get(3).getRoles().get(0).getName()).isEqualTo("Dan");

        assertThat(movies.get(0).getActings().get(4)).isNotNull();
        assertThat(movies.get(0).getActings().get(4).isStarring()).isFalse();
        assertThat(movies.get(0).getActings().get(4).getActor()).isNotNull();
        assertThat(movies.get(0).getActings().get(4).getActor().getId()).isEqualTo(6);
        assertThat(movies.get(0).getActings().get(4).getActor().getFirstName()).isEqualTo("Jeanne");
        assertThat(movies.get(0).getActings().get(4).getActor().getLastName()).isEqualTo("Bates");
        assertThat(movies.get(0).getActings().get(4).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(0).getActings().get(4).getActor().getProfilePhoto()).isEqualTo("6.jpg");
        assertThat(movies.get(0).getActings().get(4).getActor().isStar()).isFalse();

        assertThat(movies.get(0).getActings().get(4).getRoles()).isNotNull();
        assertThat(movies.get(0).getActings().get(4).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(0).getActings().get(4).getRoles().get(0)).isNotNull();
        assertThat(movies.get(0).getActings().get(4).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(0).getActings().get(4).getRoles().get(0).getName()).isEqualTo("Irene");

        //==========================================================================
        //Test movie num 2
        //==========================================================================
        assertThat(movies.get(1)).isNotNull();
        assertThat(movies.get(1).getId()).isNotNull().isEqualTo(2);
        assertThat(movies.get(1).getTitle()).isEqualTo("Inland Empire");
        assertThat(movies.get(1).getCoverImage()).isEqualTo("2.jpg");
        assertThat(movies.get(1).getReleaseDate()).isEqualTo(LocalDate.of(2006, Month.SEPTEMBER, 6));
        assertThat(movies.get(1).getDescription()).isEqualTo("As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.");
        assertThat(movies.get(1).getAudienceRating()).isEqualTo(68);
        assertThat(movies.get(1).getLength()).isEqualTo(180);

        assertThat(movies.get(1).getGenres()).isNotNull();
        assertThat(movies.get(1).getGenres().size()).isEqualTo(3);

        assertThat(movies.get(1).getGenres().get(0)).isNotNull();
        assertThat(movies.get(1).getGenres().get(0).getId()).isEqualTo(6);
        assertThat(movies.get(1).getGenres().get(0).getName()).isEqualTo("Drama");

        assertThat(movies.get(1).getGenres().get(1)).isNotNull();
        assertThat(movies.get(1).getGenres().get(1).getId()).isEqualTo(12);
        assertThat(movies.get(1).getGenres().get(1).getName()).isEqualTo("Mystery");

        assertThat(movies.get(1).getGenres().get(2)).isNotNull();
        assertThat(movies.get(1).getGenres().get(2).getId()).isEqualTo(13);
        assertThat(movies.get(1).getGenres().get(2).getName()).isEqualTo("Fantasy");

        assertThat(movies.get(1).getDirectors()).isNotNull();
        assertThat(movies.get(1).getDirectors().size()).isEqualTo(1);

        assertThat(movies.get(1).getDirectors().get(0)).isNotNull();
        assertThat(movies.get(1).getDirectors().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getDirectors().get(0).getFirstName()).isEqualTo("David");
        assertThat(movies.get(1).getDirectors().get(0).getLastName()).isEqualTo("Lynch");
        assertThat(movies.get(1).getDirectors().get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(1).getDirectors().get(0).getProfilePhoto()).isEqualTo("1.jpg");

        assertThat(movies.get(1).getWriters()).isNotNull();
        assertThat(movies.get(1).getWriters().size()).isEqualTo(1);

        assertThat(movies.get(1).getWriters().get(0)).isNotNull();
        assertThat(movies.get(1).getWriters().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getWriters().get(0).getFirstName()).isEqualTo("David");
        assertThat(movies.get(1).getWriters().get(0).getLastName()).isEqualTo("Lynch");
        assertThat(movies.get(1).getWriters().get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(1).getWriters().get(0).getProfilePhoto()).isEqualTo("1.jpg");

        assertThat(movies.get(1).getActings()).isNotNull();
        assertThat(movies.get(1).getActings().size()).isEqualTo(7);

        assertThat(movies.get(1).getActings().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(0).isStarring()).isTrue();
        assertThat(movies.get(1).getActings().get(0).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(0).getActor().getId()).isEqualTo(4);
        assertThat(movies.get(1).getActings().get(0).getActor().getFirstName()).isEqualTo("Justin");
        assertThat(movies.get(1).getActings().get(0).getActor().getLastName()).isEqualTo("Theroux");
        assertThat(movies.get(1).getActings().get(0).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(1).getActings().get(0).getActor().getProfilePhoto()).isEqualTo("4.jpg");
        assertThat(movies.get(1).getActings().get(0).getActor().isStar()).isTrue();

        assertThat(movies.get(1).getActings().get(0).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(0).getRoles().size()).isEqualTo(2);

        assertThat(movies.get(1).getActings().get(0).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(0).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(0).getRoles().get(0).getName()).isEqualTo("Devon Berk");

        assertThat(movies.get(1).getActings().get(0).getRoles().get(1)).isNotNull();
        assertThat(movies.get(1).getActings().get(0).getRoles().get(1).getId()).isEqualTo(2);
        assertThat(movies.get(1).getActings().get(0).getRoles().get(1).getName()).isEqualTo("Billy Side");

        assertThat(movies.get(1).getActings().get(1)).isNotNull();
        assertThat(movies.get(1).getActings().get(1).isStarring()).isTrue();
        assertThat(movies.get(1).getActings().get(1).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(1).getActor().getId()).isEqualTo(7);
        assertThat(movies.get(1).getActings().get(1).getActor().getFirstName()).isEqualTo("Karolina");
        assertThat(movies.get(1).getActings().get(1).getActor().getLastName()).isEqualTo("Gruszka");
        assertThat(movies.get(1).getActings().get(1).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(1).getActings().get(1).getActor().getProfilePhoto()).isEqualTo("7.jpg");
        assertThat(movies.get(1).getActings().get(1).getActor().isStar()).isTrue();

        assertThat(movies.get(1).getActings().get(1).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(1).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(1).getActings().get(1).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(1).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(1).getRoles().get(0).getName()).isEqualTo("Lost Girl");

        assertThat(movies.get(1).getActings().get(2)).isNotNull();
        assertThat(movies.get(1).getActings().get(2).isStarring()).isTrue();
        assertThat(movies.get(1).getActings().get(2).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(2).getActor().getId()).isEqualTo(8);
        assertThat(movies.get(1).getActings().get(2).getActor().getFirstName()).isEqualTo("Krzysztof");
        assertThat(movies.get(1).getActings().get(2).getActor().getLastName()).isEqualTo("Majchrzak");
        assertThat(movies.get(1).getActings().get(2).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(1).getActings().get(2).getActor().getProfilePhoto()).isEqualTo("8.jpg");
        assertThat(movies.get(1).getActings().get(2).getActor().isStar()).isTrue();

        assertThat(movies.get(1).getActings().get(2).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(2).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(1).getActings().get(2).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(2).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(2).getRoles().get(0).getName()).isEqualTo("Phantom");

        assertThat(movies.get(1).getActings().get(3)).isNotNull();
        assertThat(movies.get(1).getActings().get(3).isStarring()).isTrue();
        assertThat(movies.get(1).getActings().get(3).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(3).getActor().getId()).isEqualTo(9);
        assertThat(movies.get(1).getActings().get(3).getActor().getFirstName()).isEqualTo("Grace");
        assertThat(movies.get(1).getActings().get(3).getActor().getLastName()).isEqualTo("Zabriskie");
        assertThat(movies.get(1).getActings().get(3).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(1).getActings().get(3).getActor().getProfilePhoto()).isEqualTo("9.jpg");
        assertThat(movies.get(1).getActings().get(3).getActor().isStar()).isTrue();

        assertThat(movies.get(1).getActings().get(3).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(3).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(1).getActings().get(3).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(3).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(3).getRoles().get(0).getName()).isEqualTo("Visitor #1");

        assertThat(movies.get(1).getActings().get(4)).isNotNull();
        assertThat(movies.get(1).getActings().get(4).isStarring()).isFalse();
        assertThat(movies.get(1).getActings().get(4).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(4).getActor().getId()).isEqualTo(10);
        assertThat(movies.get(1).getActings().get(4).getActor().getFirstName()).isEqualTo("Laura");
        assertThat(movies.get(1).getActings().get(4).getActor().getLastName()).isEqualTo("Dern");
        assertThat(movies.get(1).getActings().get(4).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(1).getActings().get(4).getActor().getProfilePhoto()).isEqualTo("10.jpg");
        assertThat(movies.get(1).getActings().get(4).getActor().isStar()).isTrue();

        assertThat(movies.get(1).getActings().get(4).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(4).getRoles().size()).isEqualTo(2);

        assertThat(movies.get(1).getActings().get(4).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(4).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(4).getRoles().get(0).getName()).isEqualTo("Nikki Grace");

        assertThat(movies.get(1).getActings().get(4).getRoles().get(1)).isNotNull();
        assertThat(movies.get(1).getActings().get(4).getRoles().get(1).getId()).isEqualTo(2);
        assertThat(movies.get(1).getActings().get(4).getRoles().get(1).getName()).isEqualTo("Susan Blue");

        assertThat(movies.get(1).getActings().get(5)).isNotNull();
        assertThat(movies.get(1).getActings().get(5).isStarring()).isFalse();
        assertThat(movies.get(1).getActings().get(5).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(5).getActor().getId()).isEqualTo(11);
        assertThat(movies.get(1).getActings().get(5).getActor().getFirstName()).isEqualTo("Harry Dean");
        assertThat(movies.get(1).getActings().get(5).getActor().getLastName()).isEqualTo("Stanton");
        assertThat(movies.get(1).getActings().get(5).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(1).getActings().get(5).getActor().getProfilePhoto()).isEqualTo("11.jpg");
        assertThat(movies.get(1).getActings().get(5).getActor().isStar()).isFalse();

        assertThat(movies.get(1).getActings().get(5).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(5).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(1).getActings().get(5).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(5).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(5).getRoles().get(0).getName()).isEqualTo("Freddie Howard");

        assertThat(movies.get(1).getActings().get(6)).isNotNull();
        assertThat(movies.get(1).getActings().get(6).isStarring()).isFalse();
        assertThat(movies.get(1).getActings().get(6).getActor()).isNotNull();
        assertThat(movies.get(1).getActings().get(6).getActor().getId()).isEqualTo(12);
        assertThat(movies.get(1).getActings().get(6).getActor().getFirstName()).isEqualTo("Peter J.");
        assertThat(movies.get(1).getActings().get(6).getActor().getLastName()).isEqualTo("Lucas");
        assertThat(movies.get(1).getActings().get(6).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(1).getActings().get(6).getActor().getProfilePhoto()).isEqualTo("12.jpg");
        assertThat(movies.get(1).getActings().get(6).getActor().isStar()).isFalse();

        assertThat(movies.get(1).getActings().get(6).getRoles()).isNotNull();
        assertThat(movies.get(1).getActings().get(6).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(1).getActings().get(6).getRoles().get(0)).isNotNull();
        assertThat(movies.get(1).getActings().get(6).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(1).getActings().get(6).getRoles().get(0).getName()).isEqualTo("Piotrek Krol");

        //==========================================================================
        //Test movie num 3
        //==========================================================================
        assertThat(movies.get(2)).isNotNull();
        assertThat(movies.get(2).getId()).isNotNull().isEqualTo(4);
        assertThat(movies.get(2).getTitle()).isEqualTo("The Lighthouse");
        assertThat(movies.get(2).getCoverImage()).isEqualTo("4.jpg");
        assertThat(movies.get(2).getReleaseDate()).isEqualTo(LocalDate.of(2019, Month.MAY, 19));
        assertThat(movies.get(2).getDescription()).isEqualTo("Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.");
        assertThat(movies.get(2).getAudienceRating()).isEqualTo(74);
        assertThat(movies.get(2).getLength()).isEqualTo(109);

        assertThat(movies.get(2).getGenres()).isNotNull();
        assertThat(movies.get(2).getGenres().size()).isEqualTo(3);

        assertThat(movies.get(2).getGenres().get(0)).isNotNull();
        assertThat(movies.get(2).getGenres().get(0).getId()).isEqualTo(6);
        assertThat(movies.get(2).getGenres().get(0).getName()).isEqualTo("Drama");

        assertThat(movies.get(2).getGenres().get(1)).isNotNull();
        assertThat(movies.get(2).getGenres().get(1).getId()).isEqualTo(8);
        assertThat(movies.get(2).getGenres().get(1).getName()).isEqualTo("Horror");

        assertThat(movies.get(2).getGenres().get(2)).isNotNull();
        assertThat(movies.get(2).getGenres().get(2).getId()).isEqualTo(13);
        assertThat(movies.get(2).getGenres().get(2).getName()).isEqualTo("Fantasy");

        assertThat(movies.get(2).getDirectors()).isNotNull();
        assertThat(movies.get(2).getDirectors().size()).isEqualTo(1);

        assertThat(movies.get(2).getDirectors().get(0)).isNotNull();
        assertThat(movies.get(2).getDirectors().get(0).getId()).isEqualTo(26);
        assertThat(movies.get(2).getDirectors().get(0).getFirstName()).isEqualTo("Robert");
        assertThat(movies.get(2).getDirectors().get(0).getLastName()).isEqualTo("Eggers");
        assertThat(movies.get(2).getDirectors().get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(2).getDirectors().get(0).getProfilePhoto()).isEqualTo("26.jpg");

        assertThat(movies.get(2).getWriters()).isNotNull();
        assertThat(movies.get(2).getWriters().size()).isEqualTo(2);

        assertThat(movies.get(2).getWriters().get(0)).isNotNull();
        assertThat(movies.get(2).getWriters().get(0).getId()).isEqualTo(26);
        assertThat(movies.get(2).getWriters().get(0).getFirstName()).isEqualTo("Robert");
        assertThat(movies.get(2).getWriters().get(0).getLastName()).isEqualTo("Eggers");
        assertThat(movies.get(2).getWriters().get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(2).getWriters().get(0).getProfilePhoto()).isEqualTo("26.jpg");

        assertThat(movies.get(2).getWriters().get(1)).isNotNull();
        assertThat(movies.get(2).getWriters().get(1).getId()).isEqualTo(27);
        assertThat(movies.get(2).getWriters().get(1).getFirstName()).isEqualTo("Max");
        assertThat(movies.get(2).getWriters().get(1).getLastName()).isEqualTo("Eggers");
        assertThat(movies.get(2).getWriters().get(1).getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(2).getWriters().get(1).getProfilePhoto()).isEqualTo("27.jpg");

        assertThat(movies.get(2).getActings()).isNotNull();
        assertThat(movies.get(2).getActings().size()).isEqualTo(3);

        assertThat(movies.get(2).getActings().get(0)).isNotNull();
        assertThat(movies.get(2).getActings().get(0).isStarring()).isTrue();
        assertThat(movies.get(2).getActings().get(0).getActor()).isNotNull();
        assertThat(movies.get(2).getActings().get(0).getActor().getId()).isEqualTo(28);
        assertThat(movies.get(2).getActings().get(0).getActor().getFirstName()).isEqualTo("Robert");
        assertThat(movies.get(2).getActings().get(0).getActor().getLastName()).isEqualTo("Pattinson");
        assertThat(movies.get(2).getActings().get(0).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(2).getActings().get(0).getActor().getProfilePhoto()).isEqualTo("28.jpg");
        assertThat(movies.get(2).getActings().get(0).getActor().isStar()).isTrue();

        assertThat(movies.get(2).getActings().get(0).getRoles()).isNotNull();
        assertThat(movies.get(2).getActings().get(0).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(2).getActings().get(0).getRoles().get(0)).isNotNull();
        assertThat(movies.get(2).getActings().get(0).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(2).getActings().get(0).getRoles().get(0).getName()).isEqualTo("Thomas Howard");

        assertThat(movies.get(2).getActings().get(1)).isNotNull();
        assertThat(movies.get(2).getActings().get(1).isStarring()).isTrue();
        assertThat(movies.get(2).getActings().get(1).getActor()).isNotNull();
        assertThat(movies.get(2).getActings().get(1).getActor().getId()).isEqualTo(29);
        assertThat(movies.get(2).getActings().get(1).getActor().getFirstName()).isEqualTo("Willem");
        assertThat(movies.get(2).getActings().get(1).getActor().getLastName()).isEqualTo("Dafoe");
        assertThat(movies.get(2).getActings().get(1).getActor().getGender()).isEqualTo(Gender.MALE);
        assertThat(movies.get(2).getActings().get(1).getActor().getProfilePhoto()).isEqualTo("29.jpg");
        assertThat(movies.get(2).getActings().get(1).getActor().isStar()).isTrue();

        assertThat(movies.get(2).getActings().get(1).getRoles()).isNotNull();
        assertThat(movies.get(2).getActings().get(1).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(2).getActings().get(1).getRoles().get(0)).isNotNull();
        assertThat(movies.get(2).getActings().get(1).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(2).getActings().get(1).getRoles().get(0).getName()).isEqualTo("Thomas Wake");

        assertThat(movies.get(2).getActings().get(2)).isNotNull();
        assertThat(movies.get(2).getActings().get(2).isStarring()).isTrue();
        assertThat(movies.get(2).getActings().get(2).getActor()).isNotNull();
        assertThat(movies.get(2).getActings().get(2).getActor().getId()).isEqualTo(30);
        assertThat(movies.get(2).getActings().get(2).getActor().getFirstName()).isEqualTo("Valeriia");
        assertThat(movies.get(2).getActings().get(2).getActor().getLastName()).isEqualTo("Karaman");
        assertThat(movies.get(2).getActings().get(2).getActor().getGender()).isEqualTo(Gender.FEMALE);
        assertThat(movies.get(2).getActings().get(2).getActor().getProfilePhoto()).isEqualTo("30.jpg");
        assertThat(movies.get(2).getActings().get(2).getActor().isStar()).isFalse();

        assertThat(movies.get(2).getActings().get(2).getRoles()).isNotNull();
        assertThat(movies.get(2).getActings().get(2).getRoles().size()).isEqualTo(1);

        assertThat(movies.get(2).getActings().get(2).getRoles().get(0)).isNotNull();
        assertThat(movies.get(2).getActings().get(2).getRoles().get(0).getId()).isEqualTo(1);
        assertThat(movies.get(2).getActings().get(2).getRoles().get(0).getName()).isEqualTo("Mermaid");

        //==========================================================================
        //Test movie num 4
        //==========================================================================
        assertThat(movies.get(3)).isNotNull();
        assertThat(movies.get(3).getId()).isNotNull().isEqualTo(5);
        assertThat(movies.get(3).getTitle()).isEqualTo("2001: A Space Odyssey");
        assertThat(movies.get(3).getCoverImage()).isNull();
        assertThat(movies.get(3).getReleaseDate()).isEqualTo(LocalDate.of(1968, Month.APRIL, 2));
        assertThat(movies.get(3).getDescription()).isEqualTo("After uncovering a mysterious artifact buried beneath the Lunar surface, a spacecraft is sent to Jupiter to find its origins: a spacecraft manned by two men and the supercomputer HAL 9000.");
        assertThat(movies.get(3).getAudienceRating()).isEqualTo(83);
        assertThat(movies.get(3).getLength()).isEqualTo(149);

        assertThat(movies.get(3).getGenres()).isNotNull().isEmpty();
        assertThat(movies.get(3).getDirectors()).isNotNull().isEmpty();
        assertThat(movies.get(3).getWriters()).isNotNull().isEmpty();
        assertThat(movies.get(3).getActings()).isNotNull().isEmpty();

        //==========================================================================
        for (MovieJDBC movie : movies) {
            assertThat(movie.getCritiques()).isNotNull().isEmpty();
            assertThat(movie.getCriticRating()).isNull();
            for (GenreJDBC genre : movie.getGenres()) {
                assertThat(genre.getMedias()).isNotNull().isEmpty();
            }
            for (ActingJDBC acting : movie.getActings()) {
                assertThat(acting.getMedia() == movie).isTrue();
                for (ActingRoleJDBC role : acting.getRoles()) {
                    assertThat(role.getActing() == acting).isTrue();
                }
            }
        }

    }

    @Test
    @Order(5)
    @DisplayName("Tests normal functionality of findAllNoRelationshipsPaginated method of MovieRepositoryJDBC class")
    void findAllNoRelationshipsPaginated_Test() {
        List<MovieJDBC> movies = new ArrayList<>();
        int page;
        int size;

        page = 0;
        size = 1;
        movies = repo.findAllNoRelationshipsPaginated(page, size);
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(1);
        assertThat(movies.get(0)).isNotNull();
        assertThat(movies.get(0).getId()).isNotNull().isEqualTo(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Mulholland Drive");
        assertThat(movies.get(0).getCoverImage()).isEqualTo("1.jpg");
        assertThat(movies.get(0).getReleaseDate()).isEqualTo(LocalDate.of(2001, Month.MAY, 16));
        assertThat(movies.get(0).getDescription()).isEqualTo("After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.");
        assertThat(movies.get(0).getAudienceRating()).isEqualTo(79);
        assertThat(movies.get(0).getLength()).isEqualTo(147);

        page = 0;
        size = 10;
        movies = repo.findAllNoRelationshipsPaginated(page, size);
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(4);

        page = 0;
        size = 3;
        movies = repo.findAllNoRelationshipsPaginated(page, size);
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(3);

        page = 1;
        size = 1;
        movies = repo.findAllNoRelationshipsPaginated(page, size);
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(1);
        assertThat(movies.get(0)).isNotNull();
        assertThat(movies.get(0).getId()).isNotNull().isEqualTo(2);
        assertThat(movies.get(0).getTitle()).isEqualTo("Inland Empire");
        assertThat(movies.get(0).getCoverImage()).isEqualTo("2.jpg");
        assertThat(movies.get(0).getReleaseDate()).isEqualTo(LocalDate.of(2006, Month.SEPTEMBER, 6));
        assertThat(movies.get(0).getDescription()).isEqualTo("As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.");
        assertThat(movies.get(0).getAudienceRating()).isEqualTo(68);
        assertThat(movies.get(0).getLength()).isEqualTo(180);

        page = 2;
        size = 1;
        movies = repo.findAllNoRelationshipsPaginated(page, size);
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(1);
        assertThat(movies.get(0)).isNotNull();
        assertThat(movies.get(0).getId()).isNotNull().isEqualTo(4);
        assertThat(movies.get(0).getTitle()).isEqualTo("The Lighthouse");
        assertThat(movies.get(0).getCoverImage()).isEqualTo("4.jpg");
        assertThat(movies.get(0).getReleaseDate()).isEqualTo(LocalDate.of(2019, Month.MAY, 19));
        assertThat(movies.get(0).getDescription()).isEqualTo("Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.");
        assertThat(movies.get(0).getAudienceRating()).isEqualTo(74);
        assertThat(movies.get(0).getLength()).isEqualTo(109);

        page = 10;
        size = 2;
        movies = repo.findAllNoRelationshipsPaginated(page, size);
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(0);

    }

}
