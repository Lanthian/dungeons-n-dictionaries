// java/datasource/mappers/Mapper.java
package datasource.mappers;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import datasource.Database;

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
     */
    Optional<T> findById(long id, Connection conn);

    /**
     * Reads and returns a {@link List} of all {@code T} database entries.
     *
     * @param conn An open {@link Database} connection to queue operations on
     * @return List of all {@code T}s in the database
     */
    List<T> findAll(Connection conn);

    /* ======================================================================
     * ----------------------- Insert, Update, Delete -----------------------
     * ====================================================================== */

    /**
     * Inserts a {@code T} entry into the database.
     *
     * @param obj Domain object of type {@code T} to insert into the database
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     */
    boolean insert(T obj, Connection conn);

    /**
     * Updates a {@code T} entry in the database.
     *
     * @param obj Domain object of type {@code T} to be updated
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     */
    boolean update(T obj, Connection conn);

    /**
     * Deletes a {@code T} entry from the database.
     *
     * @param obj Domain object of type {@code T} to be deleted
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     */
    boolean delete(T obj, Connection conn);
}
