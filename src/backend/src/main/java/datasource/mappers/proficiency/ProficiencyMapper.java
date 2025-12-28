// java/datasource/mappers/proficiency/ProficiencyMapper.java
package datasource.mappers.proficiency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import datasource.Database;
import datasource.mappers.Mapper;
import domain.core.EntityId;
import domain.modifiers.proficiency.Proficiency;
import domain.types.ProficiencyType;

/**
 * Facade for {@link Proficiency} data mapping operations. Implementation of
 * {@link Mapper} interface.
 */
public class ProficiencyMapper implements Mapper<Proficiency> {

    // --- Constants ---
    private final static String TABLE_NAME = "proficiency";

    // --- Attributes ---
    private final Map<ProficiencyType, Mapper<?>> mappers = Map.of(
        ProficiencyType.ARMOUR, new ArmourProficiencyMapper(),
        ProficiencyType.SKILL, new SkillProficiencyMapper(),
        ProficiencyType.TOOL, new ToolProficiencyMapper()
    );

    /* ======================================================================
     * ----------------------- Mapper  Implementation -----------------------
     * ====================================================================== */

    /* -------------------------- Read  Operations -------------------------- */

    @Override
    public Optional<Proficiency> findById(long id, Connection conn) throws SQLException {
        String sql = "SELECT kind FROM " + TABLE_NAME + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!(rs.next())) return Optional.empty();

                // Delegate to correct proficiency mapper
                String table = rs.getString("kind");
                ProficiencyType type = ProficiencyType.fromString(table);
                return mapper(type).findById(id, conn);
            }
        }
    }

    @Override
    public List<Proficiency> findAll(Connection conn) throws SQLException {
        List<Proficiency> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Instantiate objects from all entries in queried ResultSet
            while (rs.next()) {
                long id = rs.getLong("id");
                String typeString = rs.getString("kind");
                ProficiencyType type = ProficiencyType.fromString(typeString);
                mapper(type).findById(id, conn).ifPresent(list::add);
            }
        }
        return list;
    }

    /**
     * Reads and returns a fully instantiated {@link List} of all {@link
     * Proficiency} database entries of a particular {@link ProficiencyType}.
     *
     * @param type particular kind of {@link ProficiencyType} returned
     * @param conn an open {@link Database} connection to queue operations on
     * @return list of all {@link ProficiencyType} proficiencies in the database
     * @throws SQLException
     */
    public List<Proficiency> findAllByType(ProficiencyType type, Connection conn) throws SQLException {
        return mapper(type).findAll(conn);
    }

    /* ----------------------- Insert, Update, Delete ----------------------- */

    @Override
    public boolean insert(Proficiency obj, Connection conn) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + "(kind) VALUES (?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getProficiencyType().toString());

            // Set ID of object from returned value
            try (ResultSet rs = pstmt.executeQuery()) {
                // Throw error if insertion failed (no ID returned)
                if (!rs.next()) throw new SQLException("Table `" + TABLE_NAME + "` insertion failed");
                obj.setId(new EntityId<>(rs.getLong(1)));

                // Insert into proficiency subtype table
                return mapper(obj.getProficiencyType()).insert(obj, conn);
            }
        }
    }

    @Override
    public boolean update(Proficiency obj, Connection conn) throws SQLException {
        return mapper(obj.getProficiencyType()).update(obj, conn);
    }

    @Override
    public boolean delete(Proficiency obj, Connection conn) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, obj.getId().value());
            return pstmt.executeUpdate() == 1;
        }
    }

    /* ======================================================================
     * -------------------------- Utility  Methods --------------------------
     * ====================================================================== */

    @SuppressWarnings("unchecked")
    private <T extends Proficiency> Mapper<T> mapper(ProficiencyType type) {
        Mapper<?> mapper = mappers.get(type);
        if (mapper == null)
            throw new IllegalStateException("ProficiencyType " + type + " does not have an allocated Mapper");
        return (Mapper<T>) mapper;
    }
}
