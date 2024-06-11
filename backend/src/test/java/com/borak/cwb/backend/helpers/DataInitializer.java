/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.helpers;

import com.borak.cwb.backend.integration.domain.ConfigPropertiesTest;
import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.CountryJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.WriterJDBC;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

/**
 *
 * @author Mr. Poyo
 */
public class DataInitializer {

    public static final String mediaImagesFolderPath = "C:/cinemania_images/test/media/";
    public static final String personImagesFolderPath = "C:/cinemania_images/test/person/";
    public static final String userImagesFolderPath = "C:/cinemania_images/test/user/";

    public static final String jsonResponsesPropertiesFilePath = "/routes/json-responses.properties";

    public static final String address = "http://localhost";
    public static final int port = 8080;

    public static final String jwtCookieName = "cwt";
    public static final Integer jwtExpirationMs = 86400000;
    public static final String jwtSecret = "Hesaid3BlessedarethepoorinspiritfortheirsiSthekingdomofheaven4BessedarEthosewhomournfortheywillbecomforted5Blessedarethemeekfortheywillinherittheearth6Blessedarethosewhohungerandthirstforrighteousnessfortheywillbefilled7Blessedarethemercifulfortheywillbeshownmercy8BlessedarethepureinheartfortheywillseeGod9BlessedarethepeacemakersfortheywillbecalledchildrenofGod10Blessedarethosewhoarepersecutedbecauseofrighteousnessfortheirsisthekingdomofheaven";

    public static final List<String> MYIMAGE_VALID_EXTENSIONS = Collections.unmodifiableList(Arrays.asList("png", "jpg", "jpeg"));
    public static final long MYIMAGE_IMAGE_MAX_SIZE = 8388608L;

    private List<MovieJDBC> movies;
    private List<TVShowJDBC> shows;
    private List<MediaJDBC> medias;

    private List<GenreJDBC> genres;

    private List<DirectorJDBC> directors;
    private List<WriterJDBC> writers;
    private List<ActorJDBC> actors;

    private List<PersonJDBC> persons;
    private List<PersonWrapperJDBC> personWrappers;

    private List<UserJDBC> users;
    private List<CritiqueJDBC> critiques;

    public DataInitializer() {
        initGenres();
        initDirectors();
        initWriters();
        initActors();
        initMovies();
        initTVShows();
        initPersons();
        initUsers();
        initUserMedias();
        initCritiques();
        initMedias();
        initPersonsWrappers();
    }

    public List<MediaJDBC> getMedias() {
        return medias;
    }

    public List<MovieJDBC> getMovies() {
        return movies;
    }

    public List<TVShowJDBC> getShows() {
        return shows;
    }

    public List<GenreJDBC> getGenres() {
        return genres;
    }

    public List<DirectorJDBC> getDirectors() {
        return directors;
    }

    public List<WriterJDBC> getWriters() {
        return writers;
    }

    public List<ActorJDBC> getActors() {
        return actors;
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

    public TVShowJDBC getArcane() {
        return shows.get(0);
    }

    public TVShowJDBC getLost() {
        return shows.get(1);
    }

    public TVShowJDBC getSouthPark() {
        return shows.get(2);
    }

    public List<PersonJDBC> getPersons() {
        return persons;
    }

    public List<UserJDBC> getUsers() {
        return users;
    }

    public List<PersonWrapperJDBC> getPersonWrappers() {
        return personWrappers;
    }

    /**
     * Retrieves paginated personWrapper subList from the original PersonWrapper
     * list
     *
     * @param page - index of page
     * @param size - size of page
     * @return subList of PersonWrapper List
     * @throws IllegalArgumentException if provided page is less than 1, and
     * size is less than 0
     */
    public List<PersonWrapperJDBC> getPersonWrappers(int page, int size) throws IllegalArgumentException {
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
        }
        try {
            int startIndex = Math.multiplyExact((page - 1), size);
            if (startIndex >= personWrappers.size()) {
                return new ArrayList<>();
            }
            int endIndex = Math.min(startIndex + size, personWrappers.size());
            return personWrappers.subList(startIndex, endIndex);
        } catch (ArithmeticException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Deletes all files in:
     * <ul>
     * <li><i>{@value #mediaImagesFolderPath}</i></li>
     * <li><i>{@value #personImagesFolderPath}</i></li>
     * <li><i>{@value #userImagesFolderPath}</i></li>
     * </ul>
     * if such folders exist and populates them with dummy random one-color and
     * size images. If folders do not exist, then it creates them.
     * <p>
     * Media image names are got from iterating over a combined list of all
     * media type lists (movie, show), and calling getCoverImage() on each
     * element, while person image names are got from iterating over a combined
     * list of all person type lists (director, writer, actor) and calling
     * getProfilePhoto(). Duplicate person ids are ignored while merging lists.
     * User image names are got from iterating over a list of users, and calling
     * getProfileImage().
     * <p>
     * If media, person or user contain null as their image name, then it wont
     * create a dummy image for that object.
     *
     * @throws IllegalStateException if this method is called prior to
     * ConfigPropertiesTest class passing all of its tests
     * @throws IllegalArgumentException if provided invalid image names in lists
     * @throws RuntimeException if an exception occurred while deleting and
     * creating images
     *
     */
    public void initImages() throws IllegalStateException, IllegalArgumentException, RuntimeException {
        if (ConfigPropertiesTest.didAllTestsPass()) {
            initMediaImages();
            initPersonImages();
            initUserImages();
        } else {
            throw new IllegalStateException("ConfigPropertiesTest class tests must all pass in order to use this method!");
        }
    }

//===============================================================================================================================================
//============================================PRIVATE METHODS====================================================================================
//===============================================================================================================================================
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
        MovieJDBC m1 = new MovieJDBC(1l, "Mulholland Drive", "1.jpg", "After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.", LocalDate.of(2001, Month.MAY, 16), 79, 91, 147);
        m1.getGenres().add(genres.get(5));
        m1.getGenres().add(genres.get(10));
        m1.getGenres().add(genres.get(11));

        m1.getDirectors().add(directors.get(0));
        directors.get(0).getMedias().add(m1);
        m1.getWriters().add(writers.get(0));
        writers.get(0).getMedias().add(m1);

        ActingJDBC a11 = new ActingJDBC(m1, actors.get(0), true);
        actors.get(0).getActings().add(a11);
        a11.getRoles().add(new ActingRoleJDBC(a11, 1l, "Betty Elms"));
        a11.getRoles().add(new ActingRoleJDBC(a11, 2l, "Diane Selwyn"));

        ActingJDBC a12 = new ActingJDBC(m1, actors.get(1), true);
        actors.get(1).getActings().add(a12);
        a12.getRoles().add(new ActingRoleJDBC(a12, 1l, "Rita"));
        a12.getRoles().add(new ActingRoleJDBC(a12, 2l, "Camilla Rhodes"));

        ActingJDBC a13 = new ActingJDBC(m1, actors.get(2), true);
        actors.get(2).getActings().add(a13);
        a13.getRoles().add(new ActingRoleJDBC(a13, 1l, "Adam"));

        ActingJDBC a14 = new ActingJDBC(m1, actors.get(3), false);
        actors.get(3).getActings().add(a14);
        a14.getRoles().add(new ActingRoleJDBC(a14, 1l, "Dan"));

        ActingJDBC a15 = new ActingJDBC(m1, actors.get(4), false);
        actors.get(4).getActings().add(a15);
        a15.getRoles().add(new ActingRoleJDBC(a15, 1l, "Irene"));

        m1.getActings().add(a11);
        m1.getActings().add(a12);
        m1.getActings().add(a13);
        m1.getActings().add(a14);
        m1.getActings().add(a15);

        //-------------------------------------
        MovieJDBC m2 = new MovieJDBC(2l, "Inland Empire", "2.jpg", "As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.", LocalDate.of(2006, Month.SEPTEMBER, 6), 68, 70, 180);
        m2.getGenres().add(genres.get(5));
        m2.getGenres().add(genres.get(11));
        m2.getGenres().add(genres.get(12));

        m2.getDirectors().add(directors.get(0));
        directors.get(0).getMedias().add(m2);
        m2.getWriters().add(writers.get(0));
        writers.get(0).getMedias().add(m2);

        ActingJDBC a21 = new ActingJDBC(m2, actors.get(2), true);
        actors.get(2).getActings().add(a21);
        a21.getRoles().add(new ActingRoleJDBC(a21, 1l, "Devon Berk"));
        a21.getRoles().add(new ActingRoleJDBC(a21, 2l, "Billy Side"));

        ActingJDBC a22 = new ActingJDBC(m2, actors.get(5), true);
        actors.get(5).getActings().add(a22);
        a22.getRoles().add(new ActingRoleJDBC(a22, 1l, "Lost Girl"));

        ActingJDBC a23 = new ActingJDBC(m2, actors.get(6), true);
        actors.get(6).getActings().add(a23);
        a23.getRoles().add(new ActingRoleJDBC(a23, 1l, "Phantom"));

        ActingJDBC a24 = new ActingJDBC(m2, actors.get(7), true);
        actors.get(7).getActings().add(a24);
        a24.getRoles().add(new ActingRoleJDBC(a24, 1l, "Visitor #1"));

        ActingJDBC a25 = new ActingJDBC(m2, actors.get(8), false);
        actors.get(8).getActings().add(a25);
        a25.getRoles().add(new ActingRoleJDBC(a25, 1l, "Nikki Grace"));
        a25.getRoles().add(new ActingRoleJDBC(a25, 2l, "Susan Blue"));

        ActingJDBC a26 = new ActingJDBC(m2, actors.get(9), false);
        actors.get(9).getActings().add(a26);
        a26.getRoles().add(new ActingRoleJDBC(a26, 1l, "Freddie Howard"));

        ActingJDBC a27 = new ActingJDBC(m2, actors.get(10), false);
        actors.get(10).getActings().add(a27);
        a27.getRoles().add(new ActingRoleJDBC(a27, 1l, "Piotrek Krol"));

        m2.getActings().add(a21);
        m2.getActings().add(a22);
        m2.getActings().add(a23);
        m2.getActings().add(a24);
        m2.getActings().add(a25);
        m2.getActings().add(a26);
        m2.getActings().add(a27);

        //-------------------------------------
        MovieJDBC m3 = new MovieJDBC(4l, "The Lighthouse", null, "Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.", LocalDate.of(2019, Month.MAY, 19), 74, 80, 109);
        m3.getGenres().add(genres.get(5));
        m3.getGenres().add(genres.get(7));
        m3.getGenres().add(genres.get(12));

        m3.getDirectors().add(directors.get(3));
        directors.get(3).getMedias().add(m3);
        m3.getWriters().add(writers.get(5));
        writers.get(5).getMedias().add(m3);
        m3.getWriters().add(writers.get(6));
        writers.get(6).getMedias().add(m3);

        ActingJDBC a31 = new ActingJDBC(m3, actors.get(18), true);
        actors.get(18).getActings().add(a31);
        a31.getRoles().add(new ActingRoleJDBC(a31, 1l, "Thomas Howard"));

        ActingJDBC a32 = new ActingJDBC(m3, actors.get(19), true);
        actors.get(19).getActings().add(a32);
        a32.getRoles().add(new ActingRoleJDBC(a32, 1l, "Thomas Wake"));

        ActingJDBC a33 = new ActingJDBC(m3, actors.get(20), true);
        actors.get(20).getActings().add(a33);
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
        directors.get(1).getMedias().add(s1);
        s1.getDirectors().add(directors.get(2));
        directors.get(2).getMedias().add(s1);

        s1.getWriters().add(writers.get(1));
        writers.get(1).getMedias().add(s1);
        s1.getWriters().add(writers.get(2));
        writers.get(2).getMedias().add(s1);
        s1.getWriters().add(writers.get(3));
        writers.get(3).getMedias().add(s1);
        s1.getWriters().add(writers.get(4));
        writers.get(4).getMedias().add(s1);

        ActingJDBC a11 = new ActingJDBC(s1, actors.get(11), true);
        actors.get(11).getActings().add(a11);
        a11.getRoles().add(new ActingRoleJDBC(a11, 1l, "Vi"));

        ActingJDBC a12 = new ActingJDBC(s1, actors.get(12), true);
        actors.get(12).getActings().add(a12);
        a12.getRoles().add(new ActingRoleJDBC(a12, 1l, "Jayce"));

        ActingJDBC a13 = new ActingJDBC(s1, actors.get(13), true);
        actors.get(13).getActings().add(a13);
        a13.getRoles().add(new ActingRoleJDBC(a13, 1l, "Silico"));
        a13.getRoles().add(new ActingRoleJDBC(a13, 2l, "Pim"));

        ActingJDBC a14 = new ActingJDBC(s1, actors.get(14), false);
        actors.get(14).getActings().add(a14);
        a14.getRoles().add(new ActingRoleJDBC(a14, 1l, "Jinx"));

        ActingJDBC a15 = new ActingJDBC(s1, actors.get(15), false);
        actors.get(15).getActings().add(a15);
        a15.getRoles().add(new ActingRoleJDBC(a15, 1l, "Caitlyn"));

        ActingJDBC a16 = new ActingJDBC(s1, actors.get(16), false);
        actors.get(16).getActings().add(a16);
        a16.getRoles().add(new ActingRoleJDBC(a16, 1l, "Viktor"));

        ActingJDBC a17 = new ActingJDBC(s1, actors.get(17), false);
        actors.get(17).getActings().add(a17);
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
        TVShowJDBC s2 = new TVShowJDBC(5l, "Lost", null, "The survivors of a plane crash are forced to work together in order to survive on a seemingly deserted tropical island.", LocalDate.of(2004, Month.SEPTEMBER, 22), 83, 75, 6);
        s2.getGenres().add(genres.get(1));
        s2.getGenres().add(genres.get(5));
        s2.getGenres().add(genres.get(12));

        s2.getDirectors().add(directors.get(4));
        directors.get(4).getMedias().add(s2);
        s2.getDirectors().add(directors.get(5));
        directors.get(5).getMedias().add(s2);
        s2.getDirectors().add(directors.get(6));
        directors.get(6).getMedias().add(s2);

        s2.getWriters().add(writers.get(7));
        writers.get(7).getMedias().add(s2);
        s2.getWriters().add(writers.get(8));
        writers.get(8).getMedias().add(s2);
        s2.getWriters().add(writers.get(9));
        writers.get(9).getMedias().add(s2);

        ActingJDBC a21 = new ActingJDBC(s2, actors.get(21), true);
        actors.get(21).getActings().add(a21);
        a21.getRoles().add(new ActingRoleJDBC(a21, 1l, "Hugo 'Hurley' Reyes"));

        ActingJDBC a22 = new ActingJDBC(s2, actors.get(22), true);
        actors.get(22).getActings().add(a22);
        a22.getRoles().add(new ActingRoleJDBC(a22, 1l, "James 'Sawyer' Ford"));

        ActingJDBC a23 = new ActingJDBC(s2, actors.get(23), true);
        actors.get(23).getActings().add(a23);
        a23.getRoles().add(new ActingRoleJDBC(a23, 1l, "Sun-Hwa Kwon"));

        ActingJDBC a24 = new ActingJDBC(s2, actors.get(24), false);
        actors.get(24).getActings().add(a24);
        a24.getRoles().add(new ActingRoleJDBC(a24, 1l, "Kate Austen"));

        ActingJDBC a25 = new ActingJDBC(s2, actors.get(25), false);
        actors.get(25).getActings().add(a25);
        a25.getRoles().add(new ActingRoleJDBC(a25, 1l, "John Locke"));
        a25.getRoles().add(new ActingRoleJDBC(a25, 2l, "Man in Black"));

        ActingJDBC a26 = new ActingJDBC(s2, actors.get(26), false);
        actors.get(26).getActings().add(a26);
        a26.getRoles().add(new ActingRoleJDBC(a26, 1l, "Dr. Jack Shephard"));

        ActingJDBC a27 = new ActingJDBC(s2, actors.get(27), false);
        actors.get(27).getActings().add(a27);
        a27.getRoles().add(new ActingRoleJDBC(a27, 1l, "Jin-Soo Kwon"));

        ActingJDBC a28 = new ActingJDBC(s2, actors.get(28), false);
        actors.get(28).getActings().add(a28);
        a28.getRoles().add(new ActingRoleJDBC(a28, 1l, "Sayid Jarrah"));

        ActingJDBC a29 = new ActingJDBC(s2, actors.get(29), false);
        actors.get(29).getActings().add(a29);
        a29.getRoles().add(new ActingRoleJDBC(a29, 1l, "Claire Littleton"));

        ActingJDBC a210 = new ActingJDBC(s2, actors.get(30), false);
        actors.get(30).getActings().add(a210);
        a210.getRoles().add(new ActingRoleJDBC(a210, 1l, "Charlie Pace"));

        ActingJDBC a211 = new ActingJDBC(s2, actors.get(31), false);
        actors.get(31).getActings().add(a211);
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
        TVShowJDBC s3 = new TVShowJDBC(6l, "South Park", "6.jpg", "Follows the misadventures of four irreverent grade-schoolers in the quiet, dysfunctional town of South Park, Colorado.", LocalDate.of(1997, Month.AUGUST, 13), 87, 89, 26);
        s3.getGenres().add(genres.get(2));
        s3.getGenres().add(genres.get(3));

        s3.getDirectors().add(directors.get(7));
        directors.get(7).getMedias().add(s3);
        s3.getDirectors().add(directors.get(8));
        directors.get(8).getMedias().add(s3);
        s3.getDirectors().add(directors.get(9));
        directors.get(9).getMedias().add(s3);

        s3.getWriters().add(writers.get(10));
        writers.get(10).getMedias().add(s3);
        s3.getWriters().add(writers.get(11));
        writers.get(11).getMedias().add(s3);

        ActingJDBC a31 = new ActingJDBC(s3, actors.get(32), true);
        actors.get(32).getActings().add(a31);
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
        actors.get(33).getActings().add(a32);
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
        actors.get(34).getActings().add(a33);
        a33.getRoles().add(new ActingRoleJDBC(a33, 1l, "Chef"));

        ActingJDBC a34 = new ActingJDBC(s3, actors.get(35), false);
        actors.get(35).getActings().add(a34);
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

    private void initPersons() {
        Set<PersonJDBC> set = new HashSet<>();
        PersonJDBC person = null;
        for (DirectorJDBC d : directors) {
            person = new PersonJDBC(d.getId(), d.getFirstName(), d.getLastName(), d.getGender(), d.getProfilePhoto());
            set.add(person);
        }
        for (WriterJDBC w : writers) {
            person = new PersonJDBC(w.getId(), w.getFirstName(), w.getLastName(), w.getGender(), w.getProfilePhoto());
            set.add(person);
        }
        for (ActorJDBC a : actors) {
            person = new PersonJDBC(a.getId(), a.getFirstName(), a.getLastName(), a.getGender(), a.getProfilePhoto());
            set.add(person);
        }
        this.persons = new ArrayList<>(set.stream().collect(Collectors.toList()));
        this.persons.sort(Comparator.comparingLong(PersonJDBC::getId));
    }

    private void initUsers() {
        users = new ArrayList<UserJDBC>() {
            {
                add(new UserJDBC(1l, "Admin", "Admin",
                        Gender.OTHER,
                        "Admin", null,
                        "admin", "admin@gmail.com",
                        "admin",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(198l,"Serbia","The Republic of Serbia","RS")));
                add(new UserJDBC(2l, "Regular", "Regular",
                        Gender.MALE,
                        "Regular", "Regular.jpg",
                        "regular", "regular@gmail.com",
                        "regular",
                        UserRole.REGULAR,
                        LocalDateTime.of(2024, Month.FEBRUARY, 28, 14, 49, 36),
                        LocalDateTime.of(2024, Month.FEBRUARY, 28, 14, 49, 36),
                        new CountryJDBC(15l,"Austria","The Republic of Austria","AT")));
                add(new UserJDBC(3l, "Critic", "Critic",
                        Gender.FEMALE,
                        "Critic", "Critic.jpg",
                        "critic", "critic@gmail.com",
                        "critic",
                        UserRole.CRITIC,
                        LocalDateTime.of(2023, Month.NOVEMBER, 25, 14, 49, 36),
                        LocalDateTime.of(2023, Month.NOVEMBER, 25, 14, 49, 36),
                        new CountryJDBC(57l,"Cuba","The Republic of Cuba","CU")));
            }
        };
    }

    private void initUserMedias() {
        this.users.get(0).getMedias().add(this.movies.get(0));
        this.users.get(0).getMedias().add(this.shows.get(0));
        this.users.get(0).getMedias().add(this.movies.get(2));

        this.users.get(1).getMedias().add(this.movies.get(0));
        this.users.get(1).getMedias().add(this.movies.get(1));
        this.users.get(1).getMedias().add(this.shows.get(1));
    }

    private void initCritiques() {
        this.critiques = new ArrayList<CritiqueJDBC>() {
            {
                add(new CritiqueJDBC(users.get(0), movies.get(0), "Very nice movie", 95));
                add(new CritiqueJDBC(users.get(0), movies.get(2), "Good and mysterious movie. Left me puzzled at the end", 80));
                add(new CritiqueJDBC(users.get(0), shows.get(1), "Starts off good, but falls of in the end", 75));
                add(new CritiqueJDBC(users.get(2), movies.get(0), "Very nice movie. Kind of unexpected end that left me confused. Overall very nice!", 87));
                add(new CritiqueJDBC(users.get(2), movies.get(1), "Very similar to other movies of David Lynch but i felt like it lasted way longer than it should have", 70));
                add(new CritiqueJDBC(users.get(2), shows.get(2), "Comedy gold and perhapts one of the best satirical shows", 89));
            }
        };
        this.users.get(0).getCritiques().add(this.critiques.get(0));
        this.users.get(0).getCritiques().add(this.critiques.get(1));
        this.users.get(0).getCritiques().add(this.critiques.get(2));

        this.users.get(2).getCritiques().add(this.critiques.get(3));
        this.users.get(2).getCritiques().add(this.critiques.get(4));
        this.users.get(2).getCritiques().add(this.critiques.get(5));

        this.movies.get(0).getCritiques().add(this.critiques.get(0));
        this.movies.get(0).getCritiques().add(this.critiques.get(3));
        this.movies.get(1).getCritiques().add(this.critiques.get(4));
        this.movies.get(2).getCritiques().add(this.critiques.get(1));

        this.shows.get(1).getCritiques().add(this.critiques.get(2));
        this.shows.get(2).getCritiques().add(this.critiques.get(5));
    }

    private void initMedias() {
        medias = Stream.concat(movies.stream(), shows.stream())
                .sorted(Comparator.comparingLong(p -> p.getId()))
                .collect(Collectors.toList());
    }

    private void initPersonsWrappers() {
        HashMap<Long, PersonWrapperJDBC> map = new HashMap<>();
        for (DirectorJDBC director : directors) {
            updatePersonWrapperMap(map, director, DirectorJDBC.class);
        }
        for (WriterJDBC writer : writers) {
            updatePersonWrapperMap(map, writer, WriterJDBC.class);
        }
        for (ActorJDBC actor : actors) {
            updatePersonWrapperMap(map, actor, ActorJDBC.class);
        }
        personWrappers = map.values().stream()
                .sorted(Comparator.comparing(pw -> pw.getPerson().getId()))
                .collect(Collectors.toList());
    }

    private void updatePersonWrapperMap(HashMap<Long, PersonWrapperJDBC> map, PersonJDBC person, Class role) {
        PersonWrapperJDBC pw = map.get(person.getId());
        if (pw == null) {
            pw = new PersonWrapperJDBC();
            pw.setPerson(new PersonJDBC(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getGender(),
                    person.getProfilePhoto()));
            map.put(person.getId(), pw);
        }
        if (role == DirectorJDBC.class) {
            pw.setDirector((DirectorJDBC) person);
        } else if (role == WriterJDBC.class) {
            pw.setWriter((WriterJDBC) person);
        } else if (role == ActorJDBC.class) {
            pw.setActor((ActorJDBC) person);
        }
    }

//====================INIT IMAGES PRIVATE METHODS============================================================================================  
    private void initMediaImages() throws RuntimeException {
        try {
            Files.createDirectories(Paths.get(mediaImagesFolderPath));
            deleteFilesFromFolder(mediaImagesFolderPath);
            List<MediaJDBC> medias = Stream.concat(movies.stream(), shows.stream())
                    .sorted(Comparator.comparingLong(p -> p.getId()))
                    .collect(Collectors.toList());
            Random rand = new Random();
            for (MediaJDBC media : medias) {
                if (media.getCoverImage() != null) {
                    createImage(media.getCoverImage(), mediaImagesFolderPath, rand);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initPersonImages() throws RuntimeException {
        try {
            Files.createDirectories(Paths.get(personImagesFolderPath));
            deleteFilesFromFolder(personImagesFolderPath);
            Set<PersonJDBC> personSet = new HashSet<>();
            personSet.addAll(directors);
            personSet.addAll(writers);
            personSet.addAll(actors);

            List<PersonJDBC> people = new ArrayList<>(personSet);
            people.sort(Comparator.comparingLong(person -> person.getId()));

            Random rand = new Random();
            for (PersonJDBC person : people) {
                if (person.getProfilePhoto() != null) {
                    createImage(person.getProfilePhoto(), personImagesFolderPath, rand);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initUserImages() throws RuntimeException {
        try {
            Files.createDirectories(Paths.get(userImagesFolderPath));
            deleteFilesFromFolder(userImagesFolderPath);

            Random rand = new Random();
            for (UserJDBC user : users) {
                if (user.getProfileImage() != null) {
                    createImage(user.getProfileImage(), userImagesFolderPath, rand);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFilesFromFolder(String folder) throws RuntimeException {
        Path dir = Paths.get(folder);
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error while initializing images. Unable to delete files from folder: " + folder);
        }
    }

    private void createImage(String imageName, String folder, Random rand) throws RuntimeException {
        String[] parts = imageName.split("\\.");
        String extension = parts[1];

        int width = rand.nextInt(500) + 100; // Random width between 100 and 600
        int height = rand.nextInt(500) + 100; // Random height between 100 and 600
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();

        // Generate a random color
        Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

        // Save
        try {
            ImageIO.write(img, extension, new File(folder + imageName));
        } catch (IOException e) {
            throw new RuntimeException("Error while initializing images. Unable to create image with name: " + imageName);
        }
    }

}
