/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.repository.util.FileRepository;
import com.borak.kinweb.backend.seeder.domain.db.ActingDB;
import com.borak.kinweb.backend.seeder.domain.db.ActingRoleDB;
import com.borak.kinweb.backend.seeder.domain.db.DirectorDB;
import com.borak.kinweb.backend.seeder.domain.db.GenreDB;
import com.borak.kinweb.backend.seeder.domain.db.MediaDB;
import com.borak.kinweb.backend.seeder.domain.db.MovieDB;
import com.borak.kinweb.backend.seeder.domain.db.PersonWrapperDB;
import com.borak.kinweb.backend.seeder.domain.db.TVShowDB;
import com.borak.kinweb.backend.seeder.domain.db.UserDB;
import com.borak.kinweb.backend.seeder.domain.db.WriterDB;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class Database {

    private final JdbcTemplate jdbc;
    private final ConfigProperties config;
    private final DataRetreiver retreiver;
    private final FileRepository fileRepo;

    private static final String REMOVE_ALL_USERS = """
                                                 DELETE FROM user;
                                                 """;
    private static final String REMOVE_ALL_MEDIAS = """
                                                 DELETE FROM media;
                                                 """;
    private static final String REMOVE_ALL_PERSONS = """
                                                 DELETE FROM person;
                                                 """;
    private static final String REMOVE_ALL_GENRES = """
                                                 DELETE FROM genre;
                                                 """;

    @Autowired
    public Database(JdbcTemplate jdbc, ConfigProperties config, DataRetreiver retreiver, FileRepository fileRepo) {
        this.jdbc = jdbc;
        this.config = config;
        this.retreiver = retreiver;
        this.fileRepo = fileRepo;
    }
//=====================================================================================================    

    void removeAllTableData() {
        jdbc.execute(REMOVE_ALL_USERS);
        jdbc.execute(REMOVE_ALL_MEDIAS);
        jdbc.execute(REMOVE_ALL_PERSONS);
        jdbc.execute(REMOVE_ALL_GENRES);
    }

    void removeAllImages() throws Exception {
        //create folders in which to store images later if they dont exist.
        //if they exist it wont overwrite them
        Files.createDirectories(Paths.get(config.getMediaImagesFolderPath()));
        Files.createDirectories(Paths.get(config.getPersonImagesFolderPath()));
        Files.createDirectories(Paths.get(config.getUserImagesFolderPath()));

        //delete all images if present in folders
        deleteFilesFromFolder(config.getMediaImagesFolderPath());
        deleteFilesFromFolder(config.getPersonImagesFolderPath());
        deleteFilesFromFolder(config.getUserImagesFolderPath());
    }

    void storeAllGenres(List<GenreDB> genres) throws Exception {
        String sql = """
               INSERT INTO genre(id,name) VALUES(?,?);
               """;
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, genres.get(i).getId());
                ps.setString(2, genres.get(i).getName());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        }
        );

    }

    void storeAllPersons(List<PersonWrapperDB> persons) throws Exception {
        String sqlP = """
                INSERT INTO person(id,first_name,last_name,gender) VALUES(?,?,?,?);
                """;
        String sqlD = """
                INSERT INTO director(person_id) VALUES(?);
                """;
        String sqlW = """
                INSERT INTO writer(person_id) VALUES(?);
                """;
        String sqlA = """
                INSERT INTO actor(person_id,is_star) VALUES(?,?);
                """;
        List<PersonWrapperDB> directors = persons.stream().filter((person)
                -> person.getDirector() != null).collect(Collectors.toList());
        List<PersonWrapperDB> writers = persons.stream().filter((person)
                -> person.getWriter() != null).collect(Collectors.toList());
        List<PersonWrapperDB> actors = persons.stream().filter((person)
                -> person.getActor() != null).collect(Collectors.toList());

        jdbc.batchUpdate(sqlP, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, persons.get(i).getPerson().getId());
                ps.setString(2, persons.get(i).getPerson().getFirstName());
                ps.setString(3, persons.get(i).getPerson().getLastName());
                ps.setString(4, String.valueOf(persons.get(i).getPerson().getGender().getSymbol()));
            }

            @Override
            public int getBatchSize() {
                return persons.size();
            }
        }
        );

        jdbc.batchUpdate(sqlD, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, directors.get(i).getPerson().getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        }
        );
        jdbc.batchUpdate(sqlW, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, writers.get(i).getPerson().getId());
            }

            @Override
            public int getBatchSize() {
                return writers.size();
            }
        }
        );
        jdbc.batchUpdate(sqlA, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, actors.get(i).getPerson().getId());
                ps.setBoolean(2, actors.get(i).getActor().isStar());
            }

            @Override
            public int getBatchSize() {
                return actors.size();
            }
        }
        );
    }

    void storeAllMovies(List<MovieDB> movies) throws Exception {
        String sqlMe = """
                INSERT INTO media(id,title,release_date,description,audience_rating) VALUES(?,?,?,?,?);
                """;
        String sqlMo = """
                INSERT INTO movie(media_id,length) VALUES(?,?);
                """;
        String sqlMeGe = """
                INSERT INTO media_genres(media_id,genre_id) VALUES(?,?);
                """;
        String sqlMeDi = """
                INSERT INTO media_directors(media_id,director_id) VALUES(?,?);
                """;
        String sqlMeWr = """
                INSERT INTO media_writers(media_id,writer_id) VALUES(?,?);
                """;
        String sqlMeAct = """
                INSERT INTO acting(media_id,actor_id,is_starring) VALUES(?,?,?);
                """;
        String sqlMeActRo = """
                INSERT INTO acting_role(acting_id,id,name) 
                VALUES((SELECT acting.id 
                      FROM acting 
                      WHERE acting.media_id=? AND acting.actor_id=?),?,?);
                """;
        final int nG = movies.stream().mapToInt(movie -> movie.getGenres().size()).sum();
        final int nD = movies.stream().mapToInt(movie -> movie.getDirectors().size()).sum();
        final int nW = movies.stream().mapToInt(movie -> movie.getWriters().size()).sum();
        final int nA = movies.stream().mapToInt(movie -> movie.getActings().size()).sum();
        final int nR = movies.stream().mapToInt(movie
                -> movie.getActings().stream().mapToInt((acting) -> acting.getRoles().size()).sum()
        ).sum();
        Long[][] dataG = new Long[nG][2];
        Long[][] dataD = new Long[nD][2];
        Long[][] dataW = new Long[nW][2];
        Object[][] dataA = new Object[nA][3];
        Object[][] dataR = new Object[nR][4];
        int iG = 0;
        int iD = 0;
        int iW = 0;
        int iA = 0;
        int iR = 0;
        for (MovieDB movie : movies) {
            for (GenreDB genre : movie.getGenres()) {
                dataG[iG][0] = movie.getId();
                dataG[iG][1] = genre.getId();
                iG++;
            }
            for (DirectorDB director : movie.getDirectors()) {
                dataD[iD][0] = movie.getId();
                dataD[iD][1] = director.getId();
                iD++;
            }
            for (WriterDB writer : movie.getWriters()) {
                dataW[iW][0] = movie.getId();
                dataW[iW][1] = writer.getId();
                iW++;
            }
            for (ActingDB acting : movie.getActings()) {
                dataA[iA][0] = movie.getId();
                dataA[iA][1] = acting.getActor().getId();
                dataA[iA][2] = acting.isStarring();
                iA++;
                for (ActingRoleDB role : acting.getRoles()) {
                    dataR[iR][0] = movie.getId();
                    dataR[iR][1] = acting.getActor().getId();
                    dataR[iR][2] = role.getId();
                    dataR[iR][3] = role.getName();
                    iR++;
                }
            }
        }
        jdbc.batchUpdate(sqlMe, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, movies.get(i).getId());
                ps.setString(2, movies.get(i).getTitle());
                ps.setDate(3, Date.valueOf(movies.get(i).getReleaseDate()));
                ps.setString(4, movies.get(i).getDescription());
                ps.setInt(5, movies.get(i).getAudienceRating());
            }

            @Override
            public int getBatchSize() {
                return movies.size();
            }
        }
        );
        jdbc.batchUpdate(sqlMo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, movies.get(i).getId());
                ps.setLong(2, movies.get(i).getLength());
            }

            @Override
            public int getBatchSize() {
                return movies.size();
            }
        }
        );

        jdbc.batchUpdate(sqlMeGe, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dataG[i][0]);
                ps.setLong(2, dataG[i][1]);
            }

            @Override
            public int getBatchSize() {
                return nG;
            }
        }
        );

        jdbc.batchUpdate(sqlMeDi, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dataD[i][0]);
                ps.setLong(2, dataD[i][1]);
            }

            @Override
            public int getBatchSize() {
                return nD;
            }
        }
        );
        jdbc.batchUpdate(sqlMeWr, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dataW[i][0]);
                ps.setLong(2, dataW[i][1]);
            }

            @Override
            public int getBatchSize() {
                return nW;
            }
        }
        );
        jdbc.batchUpdate(sqlMeAct, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, (Long) dataA[i][0]);
                ps.setLong(2, (Long) dataA[i][1]);
                ps.setBoolean(3, (Boolean) dataA[i][2]);
            }

            @Override
            public int getBatchSize() {
                return nA;
            }
        }
        );
        jdbc.batchUpdate(sqlMeActRo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, (Long) dataR[i][0]);
                ps.setLong(2, (Long) dataR[i][1]);
                ps.setLong(3, (Long) dataR[i][2]);
                ps.setString(4, (String) dataR[i][3]);
            }

            @Override
            public int getBatchSize() {
                return nR;
            }
        }
        );

    }

    void storeAllShows(List<TVShowDB> shows) throws Exception {
        String sqlMe = """
                INSERT INTO media(id,title,release_date,description,audience_rating) VALUES(?,?,?,?,?);
                """;
        String sqlMo = """
                INSERT INTO tv_show(media_id,number_of_seasons) VALUES(?,?);
                """;
        String sqlMeGe = """
                INSERT INTO media_genres(media_id,genre_id) VALUES(?,?);
                """;
        String sqlMeDi = """
                INSERT INTO media_directors(media_id,director_id) VALUES(?,?);
                """;
        String sqlMeWr = """
                INSERT INTO media_writers(media_id,writer_id) VALUES(?,?);
                """;
        String sqlMeAct = """
                INSERT INTO acting(media_id,actor_id,is_starring) VALUES(?,?,?);
                """;
        String sqlMeActRo = """
                INSERT INTO acting_role(acting_id,id,name) 
                VALUES((SELECT acting.id 
                      FROM acting 
                      WHERE acting.media_id=? AND acting.actor_id=?),?,?);
                """;
        final int nG = shows.stream().mapToInt(show -> show.getGenres().size()).sum();
        final int nD = shows.stream().mapToInt(show -> show.getDirectors().size()).sum();
        final int nW = shows.stream().mapToInt(show -> show.getWriters().size()).sum();
        final int nA = shows.stream().mapToInt(show -> show.getActings().size()).sum();
        final int nR = shows.stream().mapToInt(show
                -> show.getActings().stream().mapToInt((acting) -> acting.getRoles().size()).sum()
        ).sum();
        Long[][] dataG = new Long[nG][2];
        Long[][] dataD = new Long[nD][2];
        Long[][] dataW = new Long[nW][2];
        Object[][] dataA = new Object[nA][3];
        Object[][] dataR = new Object[nR][4];
        int iG = 0;
        int iD = 0;
        int iW = 0;
        int iA = 0;
        int iR = 0;
        for (TVShowDB show : shows) {
            for (GenreDB genre : show.getGenres()) {
                dataG[iG][0] = show.getId();
                dataG[iG][1] = genre.getId();
                iG++;
            }
            for (DirectorDB director : show.getDirectors()) {
                dataD[iD][0] = show.getId();
                dataD[iD][1] = director.getId();
                iD++;
            }
            for (WriterDB writer : show.getWriters()) {
                dataW[iW][0] = show.getId();
                dataW[iW][1] = writer.getId();
                iW++;
            }
            for (ActingDB acting : show.getActings()) {
                dataA[iA][0] = show.getId();
                dataA[iA][1] = acting.getActor().getId();
                dataA[iA][2] = acting.isStarring();
                iA++;
                for (ActingRoleDB role : acting.getRoles()) {
                    dataR[iR][0] = show.getId();
                    dataR[iR][1] = acting.getActor().getId();
                    dataR[iR][2] = role.getId();
                    dataR[iR][3] = role.getName();
                    iR++;
                }
            }
        }
        jdbc.batchUpdate(sqlMe, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, shows.get(i).getId());
                ps.setString(2, shows.get(i).getTitle());
                ps.setDate(3, Date.valueOf(shows.get(i).getReleaseDate()));
                ps.setString(4, shows.get(i).getDescription());
                ps.setInt(5, shows.get(i).getAudienceRating());
            }

            @Override
            public int getBatchSize() {
                return shows.size();
            }
        }
        );
        jdbc.batchUpdate(sqlMo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, shows.get(i).getId());
                ps.setLong(2, shows.get(i).getNumberOfSeasons());
            }

            @Override
            public int getBatchSize() {
                return shows.size();
            }
        }
        );

        jdbc.batchUpdate(sqlMeGe, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dataG[i][0]);
                ps.setLong(2, dataG[i][1]);
            }

            @Override
            public int getBatchSize() {
                return nG;
            }
        }
        );

        jdbc.batchUpdate(sqlMeDi, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dataD[i][0]);
                ps.setLong(2, dataD[i][1]);
            }

            @Override
            public int getBatchSize() {
                return nD;
            }
        }
        );
        jdbc.batchUpdate(sqlMeWr, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dataW[i][0]);
                ps.setLong(2, dataW[i][1]);
            }

            @Override
            public int getBatchSize() {
                return nW;
            }
        }
        );
        jdbc.batchUpdate(sqlMeAct, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, (Long) dataA[i][0]);
                ps.setLong(2, (Long) dataA[i][1]);
                ps.setBoolean(3, (Boolean) dataA[i][2]);
            }

            @Override
            public int getBatchSize() {
                return nA;
            }
        }
        );
        jdbc.batchUpdate(sqlMeActRo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, (Long) dataR[i][0]);
                ps.setLong(2, (Long) dataR[i][1]);
                ps.setLong(3, (Long) dataR[i][2]);
                ps.setString(4, (String) dataR[i][3]);
            }

            @Override
            public int getBatchSize() {
                return nR;
            }
        }
        );

    }

    void storeUser(UserDB user) throws Exception {
        String sql = """
               INSERT INTO user(id,
               first_name,last_name,gender,profile_name,profile_image,
               username,email,password,role,created_at,updated_at,country_id       
               ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);
               """;
        jdbc.update(sql, (PreparedStatement ps) -> {
            ps.setLong(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, String.valueOf(user.getGender().getSymbol()));
            ps.setString(5, user.getProfileName());
            ps.setString(6, user.getProfileImage());
            ps.setString(7, user.getUsername());
            ps.setString(8, user.getEmail());
            ps.setString(9, user.getPassword());
            ps.setString(10, user.getRole().toString());
            ps.setTimestamp(11, Timestamp.valueOf(user.getCreatedAt()));
            ps.setTimestamp(12, Timestamp.valueOf(user.getUpdatedAt()));
            ps.setLong(13, user.getCountry().getId());
        });

    }

    void storeMediaImages(List<MediaDB> medias) throws Exception {
        String sql = "UPDATE media SET media.cover_image=? WHERE media.id=?;";
        List<Object[]> data = new LinkedList<>();
        for (MediaDB media : medias) {
            if (media.getCoverImagePath() != null) {
                MyImage coverImage = retreiver.getPosterImageAsMyImage(media.getCoverImagePath());
                coverImage.setName("" + media.getId());
                fileRepo.saveMediaCoverImage(coverImage);
                data.add(new Object[]{coverImage.getFullName(), media.getId()});
            }
        }
        jdbc.batchUpdate(sql, data, new int[]{Types.VARCHAR, Types.BIGINT});
    }

    void storePersonImages(List<PersonWrapperDB> persons) throws Exception {
        String sql = "UPDATE person SET person.profile_photo=? WHERE person.id=?;";
        List<Object[]> data = new LinkedList<>();
        for (PersonWrapperDB pw : persons) {
            if (pw.getPerson().getProfilePhotoPath() != null) {
                MyImage profilePhoto = retreiver.getPosterImageAsMyImage(pw.getPerson().getProfilePhotoPath());
                profilePhoto.setName("" + pw.getPerson().getId());
                fileRepo.savePersonProfilePhoto(profilePhoto);
                data.add(new Object[]{profilePhoto.getFullName(), pw.getPerson().getId()});
            }
        }
        jdbc.batchUpdate(sql, data, new int[]{Types.VARCHAR, Types.BIGINT});
    }

    void storeUserImage(UserDB user) throws Exception {
        Random rand = new Random();
        String[] parts = user.getProfileImage().split("\\.");
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
            ImageIO.write(img, extension, new File(config.getUserImagesFolderPath() + user.getProfileImage()));
        } catch (IOException e) {
            throw new RuntimeException("Error while storing user image");
        }
    }

//=====================================================================================================
    private void deleteFilesFromFolder(String folder) throws RuntimeException {
        Path dir = Paths.get(folder);
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!file.getFileName().toString().equals(".gitignore")) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to delete images from folder: " + folder);
        }
    }

}
