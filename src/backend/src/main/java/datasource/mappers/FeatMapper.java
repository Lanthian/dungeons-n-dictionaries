// java/datasource/mappers/FeatMapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import datasource.utils.SQLExceptionTranslator;
import domain.core.EntityId;
import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.proficiency.Proficiency;

/**
 * Class for {@link Feat} data mapping operations. Implementation of
 * {@link Mapper} interface, extending {@link AbstractMapper}.
 *
 * <p> The {@link #mapRow(ResultSet)} and {@link #findAll(Connection)} methods
 * return shallow instantiations of Feat objects, whereas {@link
 * #findById(long, Connection)} returns fully built out aggregate objects.
 *
 * TODO: Add choices
 */
public class FeatMapper extends AbstractMapper<Feat> {

    // --- Attributes ---
    private final CharacterModifierMapper supplyMapper;

    // Constructor (submapper allocation)
    public FeatMapper(CharacterModifierMapper supplyMapper) {
        this.supplyMapper = supplyMapper;
    }

    /* ------------------------- Relational Mapping ------------------------- */

    @Override
    protected String tableName() { return "feat"; }

    @Override
    protected long getId(Feat obj) { return obj.getId().value(); }

    @Override
    protected Feat mapRow(ResultSet rs) throws SQLException {
        /* Return shallow Feat without sub Character modifications.
            Remember to load in these mods separately afterwards. */
        Feat feat = new Feat(
            rs.getString("name"),
            rs.getString("description")
        );
        // Set ID
        feat.setId(new EntityId<>(rs.getLong("id")));
        return feat;
    }

    /* -------------------------- Insert &  Update -------------------------- */

    @Override
    public boolean insert(Feat obj, Connection conn) {
        String sql = sql("""
            INSERT INTO %TABLE% (name, description)
            VALUES (?, ?)
            RETURNING id
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getDescription());

            // Set ID of object from returned value
            try (ResultSet rs = pstmt.executeQuery()) {
                // Throw error if insertion fails (no ID returned)
                if (!rs.next()) throw new SQLException("Table `" + tableName() + "` insertion failed");
                long id = rs.getLong(1);
                obj.setId(new EntityId<>(id));

                // Delegate modifier persistence
                supplyMapper.replaceAllForSource(tableName(), id, obj, conn);
                return true;
            }

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    @Override
    public boolean update(Feat obj, Connection conn) {
        String sql = sql("""
            UPDATE %TABLE%
            SET name = ?, description = ?
            WHERE id = ?
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long id = getId(obj);
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getDescription());
            pstmt.setLong(3, id);

            // Throw error if update fails
            if (pstmt.executeUpdate() == 0)
                throw new SQLException("Table `" + tableName() + "` update for ID " + id + " failed");

            // Delegate modifier persistence
            supplyMapper.replaceAllForSource(tableName(), id, obj, conn);
            return true;

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /* ======================================================================
     * ----------------------- Overridden  Operations -----------------------
     * ====================================================================== */

    @Override
    public Optional<Feat> findById(long id, Connection conn) {
        Optional<Feat> shallowOpt = super.findById(id, conn);
        if (shallowOpt.isEmpty()) return Optional.empty();

        // mapRow method only returns shallow, non-aggregated objects
        Feat shallow = shallowOpt.get();

        // Prepare to assemble full object - search for supplied modifications
        Mapper<AbilityScoreModifier> asmMapper = MapperRegistry.getMapper(AbilityScoreModifier.class);
        List<AbilityScoreModifier> asms = new ArrayList<>();
        for (long i : supplyMapper.findAsmIds(tableName(), id, conn)) {
            asmMapper.findById(i, conn).ifPresent(asms::add);
        }

        Mapper<Proficiency> profMapper = MapperRegistry.getMapper(Proficiency.class);
        List<Proficiency> proficiences = new ArrayList<>();
        for (long i : supplyMapper.findProficiencyIds(tableName(), id, conn)) {
            profMapper.findById(i, conn).ifPresent(proficiences::add);
        }

        // Assemble full object
        Feat full = new Feat.Builder(shallow.getName(), shallow.getDescription())
            .abilityScoreModifiers(asms)
            .proficiencies(proficiences)
            // .choices(TODO)
            .build();
        full.setId(shallow.getId());

        return Optional.of(full);
    }

    @Override
    public boolean delete(Feat obj, Connection conn) {
        // Delete supplied CharacterModifications if self delete is successful
        return super.delete(obj, conn) &&
            supplyMapper.deleteAllForSource(tableName(), getId(obj), obj, conn);
    }
}
