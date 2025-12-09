// java/datasource/mappers/Mapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import datasource.Database;
import domain.modifiers.Language;

/**
 * DataMapper interface - mappers implementing this connect domain models to
 * database actions. Type parameter {@code T} denotes the type of domain objects
 * acted on.
 *
 * <p> Supplies CRUD functionality:
 * <ul>
 *   <li> Read: {@link #findById(long, Connection)}
 *   <li> Read: {@link #findAll(Connection)}
 *   <li> Create: {@link #insert(Object, Connection)}
 *   <li> Update: {@link #update(Object, Connection)}
 *   <li> Delete: {@link #delete(Object, Connection)}
 * </ul>
 *
 * <p> An already established database Connection is passed to each mapper
 * database operation to allow batch database actions.
 */
public interface Mapper<T> {

    /* ======================================================================
     * -------------------------- Read  Operations --------------------------
     * ====================================================================== */

    /**
     * Reads and returns an {@link Optional} {@code T} with the specified ID
     * searched. Optional is empty if the entry does not exist in the database.
     *
     * @param id ID of the entity to be searched
     * @param conn An open {@link Database} connection to queue operations on
     * @return Optional wrapped {@code T} depending on presence in database
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    Optional<Language> findById(long id, Connection conn) throws SQLException;

    /**
     * Reads and returns a {@link List} of all {@code T} database entries.
     *
     * @param conn An open {@link Database} connection to queue operations on
     * @return List of all {@code T}s in the database
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    List<Language> findAll(Connection conn) throws SQLException;

    /* ======================================================================
     * ----------------------- Insert, Update, Delete -----------------------
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

    /* ======================================================================
     * -------------------------- Row  Translation --------------------------
     * ====================================================================== */

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
