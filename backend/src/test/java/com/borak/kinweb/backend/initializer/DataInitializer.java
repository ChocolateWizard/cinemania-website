/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.initializer;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class DataInitializer {

    private List<MovieJDBC> movies;
    private List<TVShowJDBC> shows;

    private List<GenreJDBC> genres;

    private List<DirectorJDBC> directors;
    private List<WriterJDBC> writers;
    private List<ActorJDBC> actors;

    public DataInitializer() {
        initGenres();
        initDirectors();
        initWriters();
        initActors();
        initMovies();
        initTVShows();
    }

    public void initMediaImages() {

    }

    public List<MovieJDBC> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieJDBC> movies) {
        this.movies = movies;
    }

    public List<TVShowJDBC> getShows() {
        return shows;
    }

    public void setShows(List<TVShowJDBC> shows) {
        this.shows = shows;
    }

    public List<GenreJDBC> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreJDBC> genres) {
        this.genres = genres;
    }

    public List<DirectorJDBC> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorJDBC> directors) {
        this.directors = directors;
    }

    public List<WriterJDBC> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterJDBC> writers) {
        this.writers = writers;
    }

    public List<ActorJDBC> getActors() {
        return actors;
    }

    public void setActors(List<ActorJDBC> actors) {
        this.actors = actors;
    }

    public MovieJDBC getMullholadDrive() {
        return movies.get(0);
    }

    public MovieJDBC getInlandEmpire() {
        return movies.get(1);
    }

    public MovieJDBC getTheLighthouse() {
        return movies.get(2);
    }

//==========================================================================================================
    private void initGenres() {
        genres = new ArrayList<GenreJDBC>() {
            {
                add(new GenreJDBC(1l, "Action"));
                add(new GenreJDBC(2l, "Adventure"));
                add(new GenreJDBC(3l, "Animation"));
                add(new GenreJDBC(4l, "Comedy"));
                add(new GenreJDBC(5l, "Devotional"));
                add(new GenreJDBC(6l, "Drama"));
                add(new GenreJDBC(7l, "Historical"));
                add(new GenreJDBC(8l, "Horror"));
                add(new GenreJDBC(9l, "Science fiction"));
                add(new GenreJDBC(10l, "Western"));
                add(new GenreJDBC(11l, "Thriller"));
                add(new GenreJDBC(12l, "Mystery"));
                add(new GenreJDBC(13l, "Fantasy"));

            }
        };
    }

    private void initDirectors() {
        directors = new ArrayList<>() {
            {
                add(new DirectorJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
                add(new DirectorJDBC(14l, "Pascal", "Charrue", Gender.MALE, "14.jpg"));
                add(new DirectorJDBC(15l, "Arnaud", "Delord", Gender.MALE, "15.jpg"));
                add(new DirectorJDBC(26l, "Robert", "Eggers", Gender.MALE, "26.jpg"));
                add(new DirectorJDBC(31l, "Jeffrey J.", "Abrams", Gender.MALE, "31.jpg"));
                add(new DirectorJDBC(32l, "Jeffrey", "Lieber", Gender.MALE, null));
                add(new DirectorJDBC(33l, "Damon", "Lindelof", Gender.MALE, "33.jpg"));
                add(new DirectorJDBC(46l, "Trey", "Parker", Gender.MALE, "46.jpg"));
                add(new DirectorJDBC(47l, "Matt", "Stone", Gender.MALE, "47.jpg"));
                add(new DirectorJDBC(48l, "Brian", "Graden", Gender.MALE, "48.jpg"));
            }
        };
    }

    private void initWriters() {
        writers = new ArrayList<>() {
            {
                add(new WriterJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
                add(new WriterJDBC(16l, "Mollie Bickley", "St. John", Gender.FEMALE, null));
                add(new WriterJDBC(17l, "Ash", "Brannon", Gender.MALE, "17.jpg"));
                add(new WriterJDBC(18l, "David", "Dunne", Gender.MALE, null));
                add(new WriterJDBC(19l, "Christian", "Linke", Gender.MALE, "19.jpg"));
                add(new WriterJDBC(26l, "Robert", "Eggers", Gender.MALE, "26.jpg"));
                add(new WriterJDBC(27l, "Max", "Eggers", Gender.MALE, "27.jpg"));
                add(new WriterJDBC(31l, "Jeffrey J.", "Abrams", Gender.MALE, "31.jpg"));
                add(new WriterJDBC(33l, "Damon", "Lindelof", Gender.MALE, "33.jpg"));
                add(new WriterJDBC(34l, "Carlton", "Cuse", Gender.MALE, null));
                add(new WriterJDBC(46l, "Trey", "Parker", Gender.MALE, "46.jpg"));
                add(new WriterJDBC(47l, "Matt", "Stone", Gender.MALE, "47.jpg"));
            }
        };

    }

    private void initActors() {
        actors = new ArrayList<>() {
            {
                add(new ActorJDBC(2l, "Naomi", "Watts", Gender.FEMALE, "2.jpg", true));
                add(new ActorJDBC(3l, "Laura", "Harring", Gender.FEMALE, "3.jpg", true));
                add(new ActorJDBC(4l, "Justin", "Theroux", Gender.MALE, "4.jpg", true));
                add(new ActorJDBC(5l, "Patrick", "Fischler", Gender.MALE, "5.jpg", false));
                add(new ActorJDBC(6l, "Jeanne", "Bates", Gender.FEMALE, "6.jpg", false));
                add(new ActorJDBC(7l, "Karolina", "Gruszka", Gender.FEMALE, "7.jpg", true));
                add(new ActorJDBC(8l, "Krzysztof", "Majchrzak", Gender.MALE, "8.jpg", true));
                add(new ActorJDBC(9l, "Grace", "Zabriskie", Gender.FEMALE, "9.jpg", true));
                add(new ActorJDBC(10l, "Laura", "Dern", Gender.FEMALE, "10.jpg", true));
                add(new ActorJDBC(11l, "Harry Dean", "Stanton", Gender.MALE, "11.jpg", false));
                add(new ActorJDBC(12l, "Peter J.", "Lucas", Gender.MALE, "12.jpg", false));
                add(new ActorJDBC(13l, "Hailee", "Steinfeld", Gender.FEMALE, "13.jpg", true));
                add(new ActorJDBC(20l, "Kevin", "Alejandro", Gender.MALE, "20.jpg", true));
                add(new ActorJDBC(21l, "Jason", "Spisak", Gender.MALE, "21.jpg", false));
                add(new ActorJDBC(22l, "Ella", "Purnell", Gender.FEMALE, "22.jpg", true));
                add(new ActorJDBC(23l, "Katie", "Leung", Gender.FEMALE, "23.jpg", false));
                add(new ActorJDBC(24l, "Harry", "Lloyd", Gender.MALE, "24.jpg", false));
                add(new ActorJDBC(25l, "Mick", "Wingert", Gender.MALE, "25.jpg", false));
                add(new ActorJDBC(28l, "Robert", "Pattinson", Gender.MALE, "28.jpg", true));
                add(new ActorJDBC(29l, "Willem", "Dafoe", Gender.MALE, "29.jpg", true));
                add(new ActorJDBC(30l, "Valeriia", "Karaman", Gender.FEMALE, "30.jpg", false));
                add(new ActorJDBC(35l, "Jorge", "Garcia", Gender.MALE, "35.jpg", true));
                add(new ActorJDBC(36l, "Josh", "Holloway", Gender.MALE, "36.jpg", true));
                add(new ActorJDBC(37l, "Yunjin", "Kim", Gender.FEMALE, "37.jpg", true));
                add(new ActorJDBC(38l, "Evangeline", "Lilly", Gender.FEMALE, "38.jpg", true));
                add(new ActorJDBC(39l, "Terry", "O'Quinn", Gender.MALE, "39.jpg", false));
                add(new ActorJDBC(40l, "Matthew", "Fox", Gender.MALE, "40.jpg", false));
                add(new ActorJDBC(41l, "Daniel", "Dae Kim", Gender.MALE, "41.jpg", true));
                add(new ActorJDBC(42l, "Naveen", "Andrews", Gender.MALE, "42.jpg", false));
                add(new ActorJDBC(43l, "Emilie", "de Ravin", Gender.FEMALE, "43.jpg", false));
                add(new ActorJDBC(44l, "Dominic", "Monaghan", Gender.MALE, null, false));
                add(new ActorJDBC(45l, "Michael", "Emerson", Gender.MALE, "45.jpg", false));
                add(new ActorJDBC(46l, "Trey", "Parker", Gender.MALE, "46.jpg", true));
                add(new ActorJDBC(47l, "Matt", "Stone", Gender.MALE, "47.jpg", true));
                add(new ActorJDBC(49l, "Isaac", "Hayes", Gender.MALE, "49.jpg", false));
                add(new ActorJDBC(50l, "Mona", "Marshall", Gender.FEMALE, null, false));

            }
        };

    }

    private void initMovies() {
        movies = new ArrayList<>();
        MovieJDBC m1 = new MovieJDBC(1l, "Mulholland Drive", "1.jpg", "After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.", LocalDate.of(2001, Month.MAY, 16), 79, null, 147);
        m1.getGenres().add(genres.get(5));
        m1.getGenres().add(genres.get(10));
        m1.getGenres().add(genres.get(11));

        m1.getDirectors().add(directors.get(0));
        m1.getWriters().add(writers.get(0));

        ActingJDBC a11 = new ActingJDBC(m1, actors.get(0), true);
        a11.getRoles().add(new ActingRoleJDBC(a11, 1l, "Betty Elms"));
        a11.getRoles().add(new ActingRoleJDBC(a11, 2l, "Diane Selwyn"));

        ActingJDBC a12 = new ActingJDBC(m1, actors.get(1), true);
        a12.getRoles().add(new ActingRoleJDBC(a12, 1l, "Rita"));
        a12.getRoles().add(new ActingRoleJDBC(a12, 2l, "Camilla Rhodes"));

        ActingJDBC a13 = new ActingJDBC(m1, actors.get(2), true);
        a13.getRoles().add(new ActingRoleJDBC(a13, 1l, "Adam"));

        ActingJDBC a14 = new ActingJDBC(m1, actors.get(3), false);
        a14.getRoles().add(new ActingRoleJDBC(a14, 1l, "Dan"));

        ActingJDBC a15 = new ActingJDBC(m1, actors.get(4), false);
        a15.getRoles().add(new ActingRoleJDBC(a15, 1l, "Irene"));

        m1.getActings().add(a11);
        m1.getActings().add(a12);
        m1.getActings().add(a13);
        m1.getActings().add(a14);
        m1.getActings().add(a15);

//-------------------------------------
        MovieJDBC m2 = new MovieJDBC(2l, "Inland Empire", "2.jpg", "As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.", LocalDate.of(2006, Month.SEPTEMBER, 6), 68, null, 180);
        m2.getGenres().add(genres.get(5));
        m2.getGenres().add(genres.get(11));
        m2.getGenres().add(genres.get(12));

        m2.getDirectors().add(directors.get(0));
        m2.getWriters().add(writers.get(0));

        ActingJDBC a21 = new ActingJDBC(m2, actors.get(2), true);
        a21.getRoles().add(new ActingRoleJDBC(a21, 1l, "Devon Berk"));
        a21.getRoles().add(new ActingRoleJDBC(a21, 2l, "Billy Side"));

        ActingJDBC a22 = new ActingJDBC(m2, actors.get(5), true);
        a22.getRoles().add(new ActingRoleJDBC(a22, 1l, "Lost Girl"));

        ActingJDBC a23 = new ActingJDBC(m2, actors.get(6), true);
        a23.getRoles().add(new ActingRoleJDBC(a23, 1l, "Phantom"));

        ActingJDBC a24 = new ActingJDBC(m2, actors.get(7), true);
        a24.getRoles().add(new ActingRoleJDBC(a24, 1l, "Visitor #1"));

        ActingJDBC a25 = new ActingJDBC(m2, actors.get(8), false);
        a25.getRoles().add(new ActingRoleJDBC(a25, 1l, "Nikki Grace"));
        a25.getRoles().add(new ActingRoleJDBC(a25, 2l, "Susan Blue"));

        ActingJDBC a26 = new ActingJDBC(m2, actors.get(9), false);
        a26.getRoles().add(new ActingRoleJDBC(a26, 1l, "Freddie Howard"));

        ActingJDBC a27 = new ActingJDBC(m2, actors.get(10), false);
        a27.getRoles().add(new ActingRoleJDBC(a27, 1l, "Piotrek Krol"));

        m2.getActings().add(a21);
        m2.getActings().add(a22);
        m2.getActings().add(a23);
        m2.getActings().add(a24);
        m2.getActings().add(a25);
        m2.getActings().add(a26);
        m2.getActings().add(a27);

        //-------------------------------------
        MovieJDBC m3 = new MovieJDBC(4l, "The Lighthouse", null, "Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.", LocalDate.of(2019, Month.MAY, 19), 74, null, 109);
        m3.getGenres().add(genres.get(5));
        m3.getGenres().add(genres.get(7));
        m3.getGenres().add(genres.get(12));

        m3.getDirectors().add(directors.get(3));
        m3.getWriters().add(writers.get(5));
        m3.getWriters().add(writers.get(6));

        ActingJDBC a31 = new ActingJDBC(m3, actors.get(18), true);
        a31.getRoles().add(new ActingRoleJDBC(a31, 1l, "Thomas Howard"));

        ActingJDBC a32 = new ActingJDBC(m3, actors.get(19), true);
        a32.getRoles().add(new ActingRoleJDBC(a32, 1l, "Thomas Wake"));

        ActingJDBC a33 = new ActingJDBC(m3, actors.get(20), true);
        a33.getRoles().add(new ActingRoleJDBC(a33, 1l, "Mermaid"));

        m3.getActings().add(a31);
        m3.getActings().add(a32);
        m3.getActings().add(a33);

        //-------------------------------------
        movies.add(m1);
        movies.add(m2);
        movies.add(m3);

    }

    private void initTVShows() {
        shows = new ArrayList<>();
        TVShowJDBC s1 = new TVShowJDBC(3l, "Arcane", "3.jpg", "Set in Utopian Piltover and the oppressed underground of Zaun, the story follows the origins of two iconic League Of Legends champions and the power that will tear them apart.", LocalDate.of(2021, Month.NOVEMBER, 6), 90, null, 1);
        s1.getGenres().add(genres.get(0));
        s1.getGenres().add(genres.get(1));
        s1.getGenres().add(genres.get(2));

        s1.getDirectors().add(directors.get(1));
        s1.getDirectors().add(directors.get(2));

        s1.getWriters().add(writers.get(1));
        s1.getWriters().add(writers.get(2));
        s1.getWriters().add(writers.get(3));
        s1.getWriters().add(writers.get(4));

        ActingJDBC a11 = new ActingJDBC(s1, actors.get(11), true);
        a11.getRoles().add(new ActingRoleJDBC(a11, 1l, "Vi"));

        ActingJDBC a12 = new ActingJDBC(s1, actors.get(12), true);
        a12.getRoles().add(new ActingRoleJDBC(a12, 1l, "Jayce"));

        ActingJDBC a13 = new ActingJDBC(s1, actors.get(13), true);
        a13.getRoles().add(new ActingRoleJDBC(a13, 1l, "Silico"));
        a13.getRoles().add(new ActingRoleJDBC(a13, 2l, "Pim"));

        ActingJDBC a14 = new ActingJDBC(s1, actors.get(14), false);
        a14.getRoles().add(new ActingRoleJDBC(a14, 1l, "Jinx"));

        ActingJDBC a15 = new ActingJDBC(s1, actors.get(15), false);
        a15.getRoles().add(new ActingRoleJDBC(a15, 1l, "Caitlyn"));

        ActingJDBC a16 = new ActingJDBC(s1, actors.get(16), false);
        a16.getRoles().add(new ActingRoleJDBC(a16, 1l, "Viktor"));

        ActingJDBC a17 = new ActingJDBC(s1, actors.get(17), false);
        a17.getRoles().add(new ActingRoleJDBC(a17, 1l, "Heimerdinger"));
        a17.getRoles().add(new ActingRoleJDBC(a17, 2l, "Duty Captain"));

        s1.getActings().add(a11);
        s1.getActings().add(a12);
        s1.getActings().add(a13);
        s1.getActings().add(a14);
        s1.getActings().add(a15);
        s1.getActings().add(a16);
        s1.getActings().add(a17);
        //-------------------------------------
        TVShowJDBC s2 = new TVShowJDBC(5l, "Lost", null, "The survivors of a plane crash are forced to work together in order to survive on a seemingly deserted tropical island.", LocalDate.of(2004, Month.SEPTEMBER, 22), 83, null, 6);
        s2.getGenres().add(genres.get(1));
        s2.getGenres().add(genres.get(5));
        s2.getGenres().add(genres.get(12));

        s2.getDirectors().add(directors.get(4));
        s2.getDirectors().add(directors.get(5));
        s2.getDirectors().add(directors.get(6));

        s2.getWriters().add(writers.get(7));
        s2.getWriters().add(writers.get(8));
        s2.getWriters().add(writers.get(9));

        ActingJDBC a21 = new ActingJDBC(s2, actors.get(21), true);
        a21.getRoles().add(new ActingRoleJDBC(a21, 1l, "Hugo 'Hurley' Reyes"));

        ActingJDBC a22 = new ActingJDBC(s2, actors.get(22), true);
        a22.getRoles().add(new ActingRoleJDBC(a22, 1l, "James 'Sawyer' Ford"));

        ActingJDBC a23 = new ActingJDBC(s2, actors.get(23), true);
        a23.getRoles().add(new ActingRoleJDBC(a23, 1l, "Sun-Hwa Kwon"));

        ActingJDBC a24 = new ActingJDBC(s2, actors.get(24), false);
        a24.getRoles().add(new ActingRoleJDBC(a24, 1l, "Kate Austen"));

        ActingJDBC a25 = new ActingJDBC(s2, actors.get(25), false);
        a25.getRoles().add(new ActingRoleJDBC(a25, 1l, "John Locke"));
        a25.getRoles().add(new ActingRoleJDBC(a25, 2l, "Man in Black"));

        ActingJDBC a26 = new ActingJDBC(s2, actors.get(26), false);
        a26.getRoles().add(new ActingRoleJDBC(a26, 1l, "Dr. Jack Shephard"));

        ActingJDBC a27 = new ActingJDBC(s2, actors.get(27), false);
        a27.getRoles().add(new ActingRoleJDBC(a27, 1l, "Jin-Soo Kwon"));

        ActingJDBC a28 = new ActingJDBC(s2, actors.get(28), false);
        a28.getRoles().add(new ActingRoleJDBC(a28, 1l, "Sayid Jarrah"));

        ActingJDBC a29 = new ActingJDBC(s2, actors.get(29), false);
        a29.getRoles().add(new ActingRoleJDBC(a29, 1l, "Claire Littleton"));

        ActingJDBC a210 = new ActingJDBC(s2, actors.get(30), false);
        a210.getRoles().add(new ActingRoleJDBC(a210, 1l, "Charlie Pace"));

        ActingJDBC a211 = new ActingJDBC(s2, actors.get(31), false);
        a211.getRoles().add(new ActingRoleJDBC(a211, 1l, "Ben Linus"));
        a211.getRoles().add(new ActingRoleJDBC(a211, 2l, "Henry Gale"));

        s2.getActings().add(a21);
        s2.getActings().add(a22);
        s2.getActings().add(a23);
        s2.getActings().add(a24);
        s2.getActings().add(a25);
        s2.getActings().add(a26);
        s2.getActings().add(a27);
        s2.getActings().add(a28);
        s2.getActings().add(a29);
        s2.getActings().add(a210);
        s2.getActings().add(a211);
        //-------------------------------------
        TVShowJDBC s3 = new TVShowJDBC(6l, "South Park", "6.jpg", "Follows the misadventures of four irreverent grade-schoolers in the quiet, dysfunctional town of South Park, Colorado.", LocalDate.of(1997, Month.AUGUST, 13), 87, null, 26);
        s3.getGenres().add(genres.get(2));
        s3.getGenres().add(genres.get(3));

        s3.getDirectors().add(directors.get(7));
        s3.getDirectors().add(directors.get(8));
        s3.getDirectors().add(directors.get(9));

        s3.getWriters().add(writers.get(10));
        s3.getWriters().add(writers.get(11));

        ActingJDBC a31 = new ActingJDBC(s3, actors.get(32), true);
        a31.getRoles().add(new ActingRoleJDBC(a31, 1l, "Stan Marsh"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 2l, "Eric Cartman"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 3l, "Randy Marsh"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 4l, "Mr. Garrison"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 5l, "Mr. Mackey"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 6l, "Clyde"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 7l, "Jimmy Valmer"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 8l, "Stephen Stotch"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 9l, "Officer Barbrady"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 10l, "News Reporter"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 11l, "TV Announcer"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 12l, "Chris Stotch"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 13l, "Tom the News Reader"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 14l, "Timmy"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 15l, "Dr. Doctor"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 16l, "Narrator"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 17l, "Additional voices"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 18l, "PC Principal"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 19l, "Phillip"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 20l, "Doctor"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 21l, "Sgt. Yates"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 22l, "Clyde Donovan"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 23l, "Mrs. Garrison"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 24l, "Grandpa Marsh"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 25l, "Mr. Hankey"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 26l, "Satan"));
        a31.getRoles().add(new ActingRoleJDBC(a31, 27l, "Santa"));

        ActingJDBC a32 = new ActingJDBC(s3, actors.get(33), true);
        a32.getRoles().add(new ActingRoleJDBC(a32, 1l, "Kyle Broflovski"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 2l, "Kenny McCormick"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 3l, "Gerald Broflovski"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 4l, "Butters Stotch"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 5l, "Butters"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 6l, "Jimbo Kern"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 7l, "Craig"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 8l, "Craig Tucker"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 9l, "Stuart McCormick"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 10l, "Additional voices"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 11l, "Priest Maxi"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 12l, "Terrance"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 13l, "Jesus"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 14l, "Pip Pirrup"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 15l, "Ted"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 16l, "Tweek Tweak"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 17l, "Tweek"));
        a32.getRoles().add(new ActingRoleJDBC(a32, 18l, "Scott Malkinson"));

        ActingJDBC a33 = new ActingJDBC(s3, actors.get(34), true);
        a33.getRoles().add(new ActingRoleJDBC(a33, 1l, "Chef"));

        ActingJDBC a34 = new ActingJDBC(s3, actors.get(35), false);
        a34.getRoles().add(new ActingRoleJDBC(a34, 1l, "Sheila Broflovski"));
        a34.getRoles().add(new ActingRoleJDBC(a34, 2l, "Linda Stotch"));

        s3.getActings().add(a31);
        s3.getActings().add(a32);
        s3.getActings().add(a33);
        s3.getActings().add(a34);

        //-------------------------------------
        shows.add(s1);
        shows.add(s2);
        shows.add(s3);
    }

}
