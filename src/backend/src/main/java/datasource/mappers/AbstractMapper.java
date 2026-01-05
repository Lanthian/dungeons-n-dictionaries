// java/datasource/mappers/AbstractMapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import datasource.utils.SQLExceptionTranslator;

/**
 * Abstract Datamapper - mappers implementing this connect domain model objects
 * to database persistence. Type parameter {@code T} denotes the type of domain
 * objects acted on.
 *
 * <p> Implements {@link Mapper} to abstract away and hide relational mapping
 * code from file accessing the Mapper interface.
 *
 * <p> Supplies the following methods:
 * <ul>
 *   <li> Meta: {@link #tableName()} - define mapper table name
 *   <li> Meta: {@link #getId(Object)} - define how to get mapped object's Id
 *   <li> Meta: {@link #mapRow(ResultSet)} - convert from SQL to domain object
 *   <li> Create: {@link #insertStatement(Object, Connection)}
 *   <li> Update: {@link #updateStatement(Object, Connection)}
 * </ul>
 */
public abstract class AbstractMapper<T> implements Mapper<T> {

    // --- Constants ---
    private final static String TABLE_NAME = "%TABLE%";

    /* ======================================================================
     * -------------------- Subclass Relational Mapping  --------------------
     * ====================================================================== */

    /**
     * Getter: Standardised retrieval of the tablename this mapper maps to
     *
     * @return String table name
     */
    protected abstract String tableName();

    /**
     * Shorthand utility method to define how the ID of an object of type {@code
     * T} is retrieved within this mapper.
     *
     * @param obj object queried
     * @return ID (as a long) of the object {@code obj} checked
     */
    protected abstract long getId(T obj);

    /**
     * Maps an SQL statement returned {@link ResultSet} back to a domain object
     * of type {@code T}.
     *
     * @param rs ResultSet row returned by executed statement
     * @return Domain entity of type {@code T} if successful in translation
     * @throws SQLException if ResultSet does not correctly translate to domain
     */
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    /* ======================================================================
     * ----------------------- Mapper  Implementation -----------------------
     * ====================================================================== */

    /* -------------------------- Read  Operations -------------------------- */

    @Override
    public Optional<T> findById(long id, Connection conn) {
        String sql = "SELECT * FROM " + tableName() + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    @Override
    public List<T> findAll(Connection conn) {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Instantiate objects from all entries in queried ResultSet
            while (rs.next()) { list.add(mapRow(rs)); }
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
        return list;
    }

    /* ------------------------------- Delete ------------------------------- */

    @Override
    public boolean delete(T obj, Connection conn) {
        String sql = "DELETE FROM " + tableName() + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, getId(obj));
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /* ======================================================================
     * -------------------------- Utility  Methods --------------------------
     * ====================================================================== */

    /**
     * GPT recommended change:
     * Shorthand utility method to replace placeholders for this table's name
     * with the actual defined {@link #tableName()}.
     *
     * @param template original String of SQL query
     * @return String of SQL query with {@link #tableName()} subbed in
     */
    protected final String sql(String template) {
        if (!template.contains(TABLE_NAME)) {
            throw new IllegalArgumentException("SQL template missing " + TABLE_NAME);
        }
        return template.replace(TABLE_NAME, tableName());
    }
}
