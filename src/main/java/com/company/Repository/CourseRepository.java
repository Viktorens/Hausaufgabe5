package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;

import java.sql.SQLException;
import java.util.List;

/**
 * Course com.company.Repository that extends an in-DataBase-com.company.Repository
 */
public class CourseRepository extends CourseJdbcRepository {
    public CourseRepository() throws SQLException {
        super();
    }

    /**
     * finds the entity with the give id from the repository
     *
     * @param id the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null
     * @throws SQLException  if connection to database could not succeed
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Course findOne(Long id) throws SQLException, NullException {
        return super.findOne(id);
    }

    /**
     * retrieves all entities from the repository
     *
     * @return all entities
     * @throws SQLException if connection to database could not succeed
     */
    @Override
    public List<Course> findAll() throws SQLException {
        return super.findAll();
    }

    /**
     * adds an entity in the repository
     *
     * @param obj entity must be not null
     * @return null if the given entity is saved otherwise returns the entity
     * @throws SQLException  if connection to database could not succeed
     * @throws NullException if input parameter entity obj is NULL
     */
    @Override
    public Course save(Course obj) throws SQLException, NullException {
        return super.save(obj);
    }

    /**
     * updates an entity from the repository
     *
     * @param obj entity must not be null
     * @return null if the entity is updated, otherwise returns the entity
     * @throws SQLException  if connection to database could not succeed
     * @throws NullException if input parameter entity obj is NULL
     */
    @Override
    public Course update(Course obj) throws SQLException, NullException {
        return super.update(obj);
    }

    /**
     * removes the entity with the specified id from the repository
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws SQLException  if connection to database could not succeed
     * @throws NullException if input parameter id is NULL
     */
    @Override
    public Course delete(Long id) throws SQLException, NullException {
        return super.delete(id);
    }
}
