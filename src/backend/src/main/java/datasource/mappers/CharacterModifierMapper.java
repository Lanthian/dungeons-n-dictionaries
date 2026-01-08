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
import java.util.Optional;

import datasource.Database;
import datasource.utils.SQLExceptionTranslator;
import domain.character.CharacterModification;
import domain.character.CharacterModifier;
import domain.core.Entity;
import domain.types.ModificationType;

/**
 * Utility Class for {@link CharacterModifier} data mapping operations.
 * Handles the {@code modifier_source} table and all {@code supply_*} tables.
 */
public class CharacterModifierMapper {

    // --- Constants ---
    private final static Map<ModificationType, String> SUPPLY_TABLES = new HashMap<>();
    static {
        // Register all supply tables here
        SUPPLY_TABLES.put(ModificationType.ASM, "supply_asm");
        SUPPLY_TABLES.put(ModificationType.FEAT, "supply_feat");
        SUPPLY_TABLES.put(ModificationType.LANGUAGE, "supply_language");
        SUPPLY_TABLES.put(ModificationType.PROFICIENCY, "supply_proficiency");
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
     */
    public void replaceAllForSource(
        String kind, long refId, CharacterModifier cm, Connection conn
    ) {
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

    /**
     * Delete all supplied modifications for a particular {@link
     * CharacterModifier} source, plus the source table entry itself.
     *
     * @param kind the table name of the modifier source
     * @param refId the PK of the modifier in its own table
     * @param cm the source for which this replaceAll is called
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful / occurred, false if otherwise
     */
    public boolean deleteAllForSource(
        String kind, long refId, CharacterModifier cm, Connection conn
    ) {
        // Find modifier_source PK ID
        Optional<Long> sourceIdOpt = findSourceId(kind, refId, conn);
        if (sourceIdOpt.isPresent()) {
            long sourceId = sourceIdOpt.get();

            // Clear supplies
            deleteAllSupplies(sourceId, conn);

            // Remove source
            return deleteSource(sourceId, conn);
        }

        // Fail to deleteAllForSource if ID doesn't exist
        return false;
    }

    /* -------------------- Thin Exposed  Supply Finders -------------------- */

    public List<Long> findAsmIds(String kind, long id, Connection conn) {
        String tableName = SUPPLY_TABLES.get(ModificationType.ASM);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    public List<Long> findFeatIds(String kind, long id, Connection conn) {
        String tableName = SUPPLY_TABLES.get(ModificationType.FEAT);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    public List<Long> findLanguageIds(String kind, long id, Connection conn) {
        String tableName = SUPPLY_TABLES.get(ModificationType.LANGUAGE);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    public List<Long> findProficiencyIds(String kind, long id, Connection conn) {
        String tableName = SUPPLY_TABLES.get(ModificationType.PROFICIENCY);
        return findSuppliedIds(kind, id, tableName, conn);
    }

    /* ======================================================================
     * -------------------------- Private  Methods --------------------------
     * ====================================================================== */

    /* -------------------- Create, Find & Delete Source -------------------- */

    /**
     * Tries to insert a new {@code modifier_source} table entry, touching
     * (performing a no-op update) on preexisting row conflict, and returns this
     * entries PK - used as {@code source_id} in supply tables.
     *
     * @param kind the table name of the modifier source
     * @param refId the PK of the modifier in its own table
     * @param conn An open {@link Database} connection to queue transactions on
     * @return PK of {@code modifier_source} table entry
     */
    private long resolveSourceId(String kind, long refId, Connection conn) {
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

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Searches for an existing {@code modifier_source} table entry, returning
     * this entries PK if found - used as {@code source_id} in supply tables.
     *
     * @param kind the table name of the modifier source
     * @param refId the PK of the modifier in its own table
     * @param conn An open {@link Database} connection to queue transactions on
     * @return {@link Optional} wrapped PK of {@code modifier_source} entry
     */
    private Optional<Long> findSourceId(String kind, long refId, Connection conn) {
        String sql = """
            SELECT id
            FROM modifier_source
            WHERE kind = ? AND ref_id = ?
            """;
         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kind);
            pstmt.setLong(2, refId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(rs.getLong("id")) : Optional.empty();
            }
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Delete a {@code modifier_source} table entry from the database.
     *
     * @param sourceId PK ID of the source to be removed
     * @param conn An open {@link Database} connection to queue transactions on
     * @return true if transaction was successful, false if otherwise
     */
    private boolean deleteSource(long sourceId, Connection conn) {
        String sql = "DELETE FROM modifier_source WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sourceId);
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
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
     */
    private List<Long> findSuppliedIds(
        String kind, long refId, String supplyTable, Connection conn
    ) {
        // Optionally create modifier_source instance / return PK ID
        long sourceId = resolveSourceId(kind, refId, conn);

        List<Long> list = new ArrayList<>();
        String sql = "SELECT supply_id FROM " + supplyTable + " WHERE source_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sourceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { list.add(rs.getLong("supply_id")); }
            }

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
        return list;
    }

    /* -------------------------- Insert &  Delete -------------------------- */

    /**
     * Batch insert a list of modifications for a specified modifier source.
     * Typically done after wiping previously supplied modificat6ions, for a
     * clean replace.
     *
     * @param <T> The class type of the {@link CharacterModification}s supplied
     * @param sourceId {@code modifier_source} table PFK
     * @param list A {@link List} of modifications to save all at once
     * @param conn An open {@link Database} connection to queue transactions on
     */
    private <T extends Entity<?> & CharacterModification> void insertSupplies(
        long sourceId, List<T> list, Connection conn
    ) {
        // Exit early on an empty list
        if (list == null || list.isEmpty()) return;

        // Check type of objects being inserted (check first instance's type)
        ModificationType type = list.get(0).modType();
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

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Wipe supply tables for a specified modifier source. Typically done before
     * re-inserting new supplied modifications, for a clean replace.
     *
     * @param sourceId {@code modifier_source} table PFK
     * @param conn An open {@link Database} connection to queue transactions on
     */
    private void deleteAllSupplies(long sourceId, Connection conn) {
        for (String tableName : SUPPLY_TABLES.values()) {
            String sql = "DELETE FROM " + tableName + " WHERE source_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, sourceId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw SQLExceptionTranslator.translate(e);
            }
        }
    }
}
