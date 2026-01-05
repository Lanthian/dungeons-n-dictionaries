// java/datasource/mappers/proficiency/ArmourProficiencyMapper.java
package datasource.mappers.proficiency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.mappers.AbstractMapper;
import datasource.utils.SQLExceptionTranslator;
import domain.core.EntityId;
import domain.modifiers.proficiency.ArmourProficiency;
import domain.types.ArmourType;

/**
 * Class for {@link ArmourProficiency} data mapping operations.
 * Implementation of {@link Mapper} interface, extending {@link AbstractMapper}.
 * Requires handling by the {@link ProficiencyMapper} class, as the ID values
 * passed around are primary foreign keys from an aggregate proficiency table.
 */
public class ArmourProficiencyMapper extends AbstractMapper<ArmourProficiency> {

    /* ------------------------- Relational Mapping ------------------------- */

    @Override
    protected String tableName() { return "armour_proficiency"; }

    @Override
    protected long getId(ArmourProficiency obj) { return obj.getId().value(); }

    @Override
    public ArmourProficiency mapRow(ResultSet rs) throws SQLException {
        ArmourProficiency obj = new ArmourProficiency(
            ArmourType.fromString(rs.getString("kind"))
        );
        // Set ID
        obj.setId(new EntityId<>(rs.getLong("id")));
        return obj;
    }

    /* -------------------------- Insert &  Update -------------------------- */

    @Override
    public boolean insert(ArmourProficiency obj, Connection conn) {
        String sql = sql("""
            INSERT INTO %TABLE% (id, kind)
            VALUES (?, ?)
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, getId(obj));
            pstmt.setString(2, obj.getType().toString());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    @Override
    public boolean update(ArmourProficiency obj, Connection conn) {
        String sql = sql("""
            UPDATE %TABLE%
            SET kind = ?
            WHERE id = ?
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getType().toString());
            pstmt.setLong(2, getId(obj));
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }
}
