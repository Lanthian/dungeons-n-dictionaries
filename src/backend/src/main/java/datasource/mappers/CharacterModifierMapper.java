// java/datasource/mappers/CharacterModifierMapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.Database;
import domain.character.CharacterModifier;
import domain.core.Entity;
import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.Language;
import domain.modifiers.proficiency.Proficiency;

/**
 * Utility Class for {@link CharacterModifier} data mapping operations.
 * Handles the {@code modifier_source} table and all {@code supply_*} tables.
 */
public class CharacterModifierMapper {

    // --- Constants ---
    private final static Map<Class<?>, String> SUPPLY_TABLES = new HashMap<>();
    static {
        // Register all supply tables here
        SUPPLY_TABLES.put(AbilityScoreModifier.class, "supply_asm");
        SUPPLY_TABLES.put(Feat.class, "supply_feat");
        SUPPLY_TABLES.put(Language.class, "supply_language");
        SUPPLY_TABLES.put(Proficiency.class, "supply_proficiency");
    }

    /* ======================================================================
     * -------------------------- Exposed  Methods --------------------------
     * ====================================================================== */

    /**
     * Refresh all supplied modifications for a particular {@link
     * CharacterModifier} source. Does not check if changes have occured, simply
     * fully clears and reinserts modifications.
     *
     * @param kind the table name of the modifier source
     * @param refId the PK of the modifier in its own table
     * @param cm the source for which this replaceAll is called
     * @param conn An open {@link Database} connection to queue transactions on
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    public void replaceAllForSource(
        String kind, long refId, CharacterModifier cm, Connection conn
    ) throws SQLException {
        // Optionally create modifier_source instance / return PK ID
        long sourceId = resolveSourceId(kind, refId, conn);

        // Clear supplies
        deleteAllSupplies(sourceId, conn);

        // Reinsert supplies
        insertSupplies(sourceId, cm.getAbilityScoreModifiers(), conn);
        insertSupplies(sourceId, cm.getFeats(), conn);
        insertSupplies(sourceId, cm.getLanguages(), conn);
        insertSupplies(sourceId, cm.getProficiencies(), conn);
    }

    /* -------------------- Thin Exposed  Supply Finders -------------------- */

    public List<Long> findAsmIds(String kind, long id, Connection conn) throws SQLException {
        String tableName = SUPPLY_TABLES.get(AbilityScoreModifier.class);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    public List<Long> findFeatIds(String kind, long id, Connection conn) throws SQLException {
        String tableName = SUPPLY_TABLES.get(Feat.class);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    public List<Long> findLanguageIds(String kind, long id, Connection conn) throws SQLException {
        String tableName = SUPPLY_TABLES.get(Language.class);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    public List<Long> findProficiencyIds(String kind, long id, Connection conn) throws SQLException {
        String tableName = SUPPLY_TABLES.get(Proficiency.class);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    /* ======================================================================
     * -------------------------- Private  Methods --------------------------
     * ====================================================================== */

    /**
     * Tries to insert a new {@code modifier_source} table entry, touching
     * (performing a no-op update) on preexisting row conflict, and returns this
     * entries PK - used as {@code source_id} in supply tables.
     *
     * @param kind the table name of the modifier source
     * @param refId the PK of the modifier in its own table
     * @param conn An open {@link Database} connection to queue transactions on
     * @return PK of {@code modifier_source} table entry
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    private long resolveSourceId(String kind, long refId, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO modifier_source (kind, ref_id)
            VALUES (?, ?)
            ON CONFLICT (kind, ref_id)
            DO UPDATE SET kind = EXCLUDED.kind
            RETURNING id
            """;
         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kind);
            pstmt.setLong(2, refId);

            // Execute and return
            try (ResultSet rs = pstmt.executeQuery()) {
                // Throw error if operation fails (no ID returned)
                if (!rs.next()) throw new SQLException("Table `modifier_source` ID retrieval failed");
                return rs.getLong("id");
            }
        }
    }

    /* --------------------------- Find  Supplies --------------------------- */

    /**
     * Retrieves a list of IDs of all modifications supplied by a particular
     * type of supply table for a particular modifier source.
     *
     * @param kind the table name of the modifier source
     * @param refId the PK of the modifier in its own table
     * @param supplyTable the name of the supply table searched
     * @param conn An open {@link Database} connection to queue transactions on
     * @return a list of original modification table specific PKs
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    private List<Long> findSuppliedIds(
        String kind, long refId, String supplyTable, Connection conn
    ) throws SQLException {
        // Optionally create modifier_source instance / return PK ID
        long sourceId = resolveSourceId(kind, refId, conn);

        List<Long> list = new ArrayList<>();
        String sql = "SELECT supply_id FROM " + supplyTable + " WHERE source_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sourceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { list.add(rs.getLong("supply_id")); }
            }
        }
        return list;
    }

    /* -------------------------- Insert &  Delete -------------------------- */

    /**
     * Batch insert a list of modifications for a specified modifier source.
     * Typically done after wiping previously supplied modificat6ions, for a
     * clean replace.
     *
     * @param <T> The class type of the modifications supplied
     * @param sourceId {@code modifier_source} table PFK
     * @param list A {@link List} of modifications to save all at once
     * @param conn An open {@link Database} connection to queue transactions on
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    private void insertSupplies(
        long sourceId, List<? extends Entity<?>> list, Connection conn
    ) throws SQLException {
        // Exit early on an empty list
        if (list == null || list.isEmpty()) return;

        // Check type of objects being inserted (check first instance's type)
        Class<?> type = list.get(0).getClass();
        String tableName = SUPPLY_TABLES.get(type);
        if (tableName == null) throw new IllegalStateException("No supply_table is mapped for type " + type);

        String sql = "INSERT INTO " + tableName + "(source_id, supply_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Stack up batch commands
            for (Entity<?> t : list) {
                long supplyId = t.getId().value();
                pstmt.setLong(1, sourceId);
                pstmt.setLong(2, supplyId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    /**
     * Wipe supply tables for a specified modifier source. Typically done before
     * re-inserting new supplied modifications, for a clean replace.
     *
     * @param sourceId {@code modifier_source} table PFK
     * @param conn An open {@link Database} connection to queue transactions on
     * @throws SQLException if an unexpected database SQL exception occurs
     */
    private void deleteAllSupplies(long sourceId, Connection conn) throws SQLException {
        for (String tableName : SUPPLY_TABLES.values()) {
            String sql = "DELETE FROM " + tableName + " WHERE source_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, sourceId);
                pstmt.executeUpdate();
            }
        }
    }
}
