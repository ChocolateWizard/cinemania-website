/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.api.IPersonWrapperRepository;
import com.borak.kinweb.backend.repository.sql.SQLPersonWrapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class PersonWrapperRepositoryJDBC implements IPersonWrapperRepository<PersonWrapperJDBC, Long> {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public List<PersonWrapperJDBC> findAllWithRelationsPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<PersonWrapperJDBC> wrappers = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_PAGINATED_PS, new Object[]{size, offset}, new int[]{Types.INTEGER, Types.INTEGER}, SQLPersonWrapper.personWrapperRM);
            for (PersonWrapperJDBC wrapper : wrappers) {
                if (wrapper.getDirector() != null) {
                    List<MediaJDBC> medias = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_MEDIA_ID_BY_DIRECTOR_PS, new Object[]{wrapper.getDirector().getId()}, new int[]{Types.BIGINT}, SQLPersonWrapper.mediaRM);
                    wrapper.getDirector().setMedias(medias);
                }
                if (wrapper.getWriter() != null) {
                    List<MediaJDBC> medias = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_MEDIA_ID_BY_WRITER_PS, new Object[]{wrapper.getWriter().getId()}, new int[]{Types.BIGINT}, SQLPersonWrapper.mediaRM);
                    wrapper.getWriter().setMedias(medias);
                }
                if (wrapper.getActor() != null) {
                    List<ActingJDBC> actings = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_ACTING_BY_ACTOR_PS, new Object[]{wrapper.getActor().getId()}, new int[]{Types.BIGINT}, SQLPersonWrapper.actingRM);
                    for (ActingJDBC acting : actings) {
                        acting.setActor(wrapper.getActor());
                        List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_ACTING_ROLES_PS, new Object[]{acting.getMedia().getId(), wrapper.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLPersonWrapper.actingRoleRM);
                        for (ActingRoleJDBC role : roles) {
                            role.setActing(acting);
                        }
                        acting.setRoles(roles);
                    }
                    wrapper.getActor().setActings(actings);
                }
            }
            return wrappers;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving persons", e);
        }
        
    }
    
    @Override
    public PersonWrapperJDBC insert(PersonWrapperJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Invalid parameter: entity must be non-null");
            }
            performInsert(entity);
            return entity;
        } catch (NullPointerException | DataAccessException e) {
            throw new DatabaseException("Error while inserting person", e);
        }
    }
    
    @Override
    public void update(PersonWrapperJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Invalid parameter: entity must be non-null");
            }
            performUpdate(entity);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error while updating person with id: " + entity.getPerson().getId() + ". " + e.getMessage(), e);
        } catch (NullPointerException | DataAccessException e) {
            throw new DatabaseException("Error while updating person with id: " + entity.getPerson().getId(), e);
        }
    }
    
    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<PersonWrapperJDBC> findAll() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<PersonWrapperJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<PersonWrapperJDBC> wrappers = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_PAGINATED_PS, new Object[]{size, offset}, new int[]{Types.INTEGER, Types.INTEGER}, SQLPersonWrapper.personWrapperRM);
            return wrappers;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving persons", e);
        }
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
    public Optional<PersonWrapperJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            PersonWrapperJDBC wrapper = jdbcTemplate.queryForObject(SQLPersonWrapper.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLPersonWrapper.personWrapperRM);
            return Optional.of(wrapper);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searching for person with id: " + id, e);
        }
    }
    
    @Override
    public Optional<PersonWrapperJDBC> findByIdWithRelations(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            PersonWrapperJDBC wrapper = jdbcTemplate.queryForObject(SQLPersonWrapper.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLPersonWrapper.personWrapperRM);
            if (wrapper.getDirector() != null) {
                List<MediaJDBC> medias = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_MEDIA_ID_BY_DIRECTOR_PS, new Object[]{wrapper.getDirector().getId()}, new int[]{Types.BIGINT}, SQLPersonWrapper.mediaRM);
                wrapper.getDirector().setMedias(medias);
            }
            if (wrapper.getWriter() != null) {
                List<MediaJDBC> medias = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_MEDIA_ID_BY_WRITER_PS, new Object[]{wrapper.getWriter().getId()}, new int[]{Types.BIGINT}, SQLPersonWrapper.mediaRM);
                wrapper.getWriter().setMedias(medias);
            }
            if (wrapper.getActor() != null) {
                List<ActingJDBC> actings = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_ACTING_BY_ACTOR_PS, new Object[]{wrapper.getActor().getId()}, new int[]{Types.BIGINT}, SQLPersonWrapper.actingRM);
                for (ActingJDBC acting : actings) {
                    acting.setActor(wrapper.getActor());
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLPersonWrapper.FIND_ALL_ACTING_ROLES_PS, new Object[]{acting.getMedia().getId(), wrapper.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLPersonWrapper.actingRoleRM);
                    for (ActingRoleJDBC role : roles) {
                        role.setActing(acting);
                    }
                    acting.setRoles(roles);
                }
                wrapper.getActor().setActings(actings);
            }
            return Optional.of(wrapper);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searching for person with id: " + id, e);
        }
        
    }
//=====================================================================================================================
//=========================================PRIVATE METHODS=============================================================
//=====================================================================================================================

    private void performUpdate(PersonWrapperJDBC entity) {
        int i = jdbcTemplate.update(SQLPersonWrapper.UPDATE_PERSON_PS, new Object[]{entity.getPerson().getFirstName(), entity.getPerson().getLastName(), String.valueOf(entity.getPerson().getGender().getSymbol()), entity.getPerson().getProfilePhoto(), entity.getPerson().getId()}, new int[]{Types.VARCHAR, Types.VARCHAR, Types.CHAR, Types.VARCHAR, Types.BIGINT});
        if (i <= 0) {
            throw new DatabaseException("No person found with given id");
        }
        performUpdateDirector(entity.getDirector(), entity.getPerson().getId());
        performUpdateWriter(entity.getWriter(), entity.getPerson().getId());
        performUpdateActor(entity.getActor(), entity.getPerson().getId());
    }
    
    private void performUpdateDirector(DirectorJDBC director, Long id) {
        jdbcTemplate.update(SQLPersonWrapper.DELETE_PERSON_DIRECTOR_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertDirector(director, id);
    }
    
    private void performUpdateWriter(WriterJDBC writer, Long id) {
        jdbcTemplate.update(SQLPersonWrapper.DELETE_PERSON_WRITER_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertWriter(writer, id);
    }
    
    private void performUpdateActor(ActorJDBC actor, Long id) {
        jdbcTemplate.update(SQLPersonWrapper.DELETE_PERSON_ACTOR_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertActor(actor, id);
    }
    
    private void performInsert(PersonWrapperJDBC entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQLPersonWrapper.INSERT_PERSON_PS, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getPerson().getFirstName());
            ps.setString(2, entity.getPerson().getLastName());
            ps.setString(3, String.valueOf(entity.getPerson().getGender().getSymbol()));
            if (entity.getPerson().getProfilePhoto() == null) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, entity.getPerson().getProfilePhoto());
            }
            return ps;
        }, keyHolder);
        entity.getPerson().setId(keyHolder.getKey().longValue());
        performInsertDirector(entity.getDirector(), entity.getPerson().getId());
        performInsertWriter(entity.getWriter(), entity.getPerson().getId());
        performInsertActor(entity.getActor(), entity.getPerson().getId());
    }
    
    private void performInsertDirector(DirectorJDBC director, Long id) {
        if (director != null) {
            jdbcTemplate.update(SQLPersonWrapper.INSERT_PERSON_DIRECTOR_PS, new Object[]{id}, new int[]{Types.BIGINT});
            jdbcTemplate.batchUpdate(SQLPersonWrapper.INSERT_DIRECTOR_MEDIA_PS, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, director.getMedias().get(i).getId());
                    ps.setLong(2, id);
                }
                
                @Override
                public int getBatchSize() {
                    return director.getMedias().size();
                }
            }
            );
        }
    }
    
    private void performInsertWriter(WriterJDBC writer, Long id) {
        if (writer != null) {
            jdbcTemplate.update(SQLPersonWrapper.INSERT_PERSON_WRITER_PS, new Object[]{id}, new int[]{Types.BIGINT});
            jdbcTemplate.batchUpdate(SQLPersonWrapper.INSERT_WRITER_MEDIA_PS, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, writer.getMedias().get(i).getId());
                    ps.setLong(2, id);
                }
                
                @Override
                public int getBatchSize() {
                    return writer.getMedias().size();
                }
            }
            );
        }
        
    }
    
    private void performInsertActor(ActorJDBC actor, Long id) {
        if (actor != null) {
            jdbcTemplate.update(SQLPersonWrapper.INSERT_PERSON_ACTOR_PS, new Object[]{id, actor.isStar()}, new int[]{Types.BIGINT, Types.BOOLEAN});
            jdbcTemplate.batchUpdate(SQLPersonWrapper.INSERT_ACTOR_ACTING_PS, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, actor.getActings().get(i).getMedia().getId());
                    ps.setLong(2, id);
                    ps.setBoolean(3, actor.getActings().get(i).isStarring());
                }
                
                @Override
                public int getBatchSize() {
                    return actor.getActings().size();
                }
            }
            );
            final int n = actor.getActings().stream().mapToInt(acting -> acting.getRoles().size()).sum();
            Object[][] data = new Object[n][4];
            int i = 0;
            for (ActingJDBC acting : actor.getActings()) {
                for (ActingRoleJDBC role : acting.getRoles()) {
                    data[i][0] = acting.getMedia().getId();
                    data[i][1] = id;
                    data[i][2] = role.getId();
                    data[i][3] = role.getName();
                    i++;
                }
            }
            jdbcTemplate.batchUpdate(SQLPersonWrapper.INSERT_ACTOR_ACTING_ROLES_PS, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, (Long) data[i][0]);
                    ps.setLong(2, (Long) data[i][1]);
                    ps.setLong(3, (Long) data[i][2]);
                    ps.setString(4, (String) data[i][3]);
                }
                
                @Override
                public int getBatchSize() {
                    return n;
                }
            }
            );
            
        }
    }
    
}
