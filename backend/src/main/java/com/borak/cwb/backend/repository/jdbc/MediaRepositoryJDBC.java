/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.jdbc;

import com.borak.cwb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.cwb.backend.exceptions.DatabaseException;
import com.borak.cwb.backend.repository.api.IMediaRepository;
import com.borak.cwb.backend.repository.sql.SQLMedia;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class MediaRepositoryJDBC implements IMediaRepository<MediaJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public MediaJDBC insert(MediaJDBC entity) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(MediaJDBC entity) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<MediaJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            jdbcTemplate.queryForObject(SQLMedia.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if media with id: " + id + " exists", e);
        }
    }

    @Override
    public List<MediaJDBC> findAll() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<MediaJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long count() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<MediaJDBC> findAllByTitleWithGenresPaginated(int page, int size, String title) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0 || title == null) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0, size must be non-negative and title must be non-null");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<MediaJDBC> medias = jdbcTemplate.query(SQLMedia.FIND_ALL_BY_TITLE_PAGINATED_PS, new Object[]{"%" + title + "%", size, offset}, new int[]{Types.VARCHAR, Types.INTEGER, Types.INTEGER}, SQLMedia.mediaRM);
            for (MediaJDBC media : medias) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMedia.FIND_ALL_GENRES_PS, new Object[]{media.getId()}, new int[]{Types.BIGINT}, SQLMedia.genreRM);
                media.setGenres(genres);
            }
            return medias;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving medias", e);
        }
    }

}
