// src/datasource/mappers/Mapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.Database;

/**
 * Mapper interface - mappers implementing this connect domain models to 
 * database actions. Type parameter {@code T} denotes the type of domain objects
 * acted on. 
 * 
 * <p> An already established database Connection is passed to each mapper 
 * database operation (insert/update/delete) to allow batch database actions.
 */
public interface Mapper<T> {

    /* ======================================================================
     * ----------------------- CUD Mapper Operations  -----------------------
     * ====================================================================== */

    /**
     * Inserts a {@code T} entry into the database.
     * 
     * @param obj Domain object of type {@code T} to insert into the database
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    boolean insert(T obj, Connection conn) throws SQLException;

    /**
     * Updates a {@code T} entry in the database.
     * 
     * @param obj Domain object of type {@code T} to be updated
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    boolean update(T obj, Connection conn) throws SQLException;

    /**
     * Deletes a {@code T} entry from the database.
     * 
     * @param obj Domain object of type {@code T} to be deleted
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    boolean delete(T obj, Connection conn) throws SQLException;

    /* -------------------------- Row  Translation -------------------------- */

    /**
     * Maps an SQL statement returned {@link ResultSet} back to a domain object 
     * of type {@code T}.
     * 
     * @param rs ResultSet row returned by executed statement
     * @return Domain entity of type {@code T} if successful in translation
     * @throws SQLException if ResultSet does not correctly translate to domain
     */
    T mapRow(ResultSet rs) throws SQLException;
}
