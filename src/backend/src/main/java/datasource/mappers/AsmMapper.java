// java/datasource/mappers/AsmMapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.utils.SQLExceptionTranslator;
import domain.core.EntityId;
import domain.modifiers.AbilityScoreModifier;
import domain.types.Ability;

/**
 * Class for {@link AbilityScoreModifier} data mapping operations.
 * Implementation of {@link Mapper} interface.
 */
public class AsmMapper extends AbstractMapper<AbilityScoreModifier> {

    /* ------------------------- Relational Mapping ------------------------- */

    @Override
    protected String tableName() { return "asm"; }

    @Override
    protected long getId(AbilityScoreModifier obj) { return obj.getId().value(); }

    @Override
    public AbilityScoreModifier mapRow(ResultSet rs) throws SQLException {
        AbilityScoreModifier asm = new AbilityScoreModifier(
            Ability.fromString(rs.getString("ability")),
            rs.getInt("value")
        );
        // Set ID
        asm.setId(new EntityId<>(rs.getLong("id")));
        return asm;
    }

    /* -------------------------- Insert &  Update -------------------------- */

    @Override
    public boolean insert(AbilityScoreModifier obj, Connection conn) {
        String sql = sql("""
            INSERT INTO %TABLE% (ability, value)
            VALUES (?, ?)
            RETURNING id
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getAbility().getShorthand());
            pstmt.setInt(2, obj.getValue());

            // Set ID of object from returned value
            try (ResultSet rs = pstmt.executeQuery()) {
                // Throw error if insertion failed (no ID returned)
                if (!rs.next()) throw new SQLException("Table `" + tableName() + "` insertion failed");
                obj.setId(new EntityId<>(rs.getLong(1)));
            }
            return true;

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    @Override
    public boolean update(AbilityScoreModifier obj, Connection conn) {
        String sql = sql("""
            UPDATE %TABLE%
            SET ability = ?, value = ?
            WHERE id = ?
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getAbility().getShorthand());
            pstmt.setInt(2, obj.getValue());
            pstmt.setLong(3, getId(obj));
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }
}
