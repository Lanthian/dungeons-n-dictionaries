// java/datasource/mappers/proficiency/SkillProficiencyMapper.java
package datasource.mappers.proficiency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.mappers.AbstractMapper;
import domain.core.EntityId;
import domain.modifiers.proficiency.SkillProficiency;
import domain.types.Skill;

/**
 * Class for {@link SkillProficiency} data mapping operations.
 * Implementation of {@link Mapper} interface, extending {@link AbstractMapper}.
 * Requires handling by the {@link ProficiencyMapper} class, as the ID values
 * passed around are primary foreign keys from an aggregate proficiency table.
 */
public class SkillProficiencyMapper extends AbstractMapper<SkillProficiency> {

    /* ------------------------- Relational Mapping ------------------------- */

    @Override
    protected String tableName() { return "skill_proficiency"; }

    @Override
    protected long getId(SkillProficiency obj) { return obj.getId().value(); }

    @Override
    public SkillProficiency mapRow(ResultSet rs) throws SQLException {
        SkillProficiency obj = new SkillProficiency(
            Skill.fromString(rs.getString("kind"))
        );
        // Set ID
        obj.setId(new EntityId<>(rs.getLong("id")));
        return obj;
    }

    /* -------------------------- Insert &  Update -------------------------- */

    @Override
    public boolean insert(SkillProficiency obj, Connection conn) throws SQLException {
        String sql = sql("""
            INSERT INTO %TABLE% (id, kind)
            VALUES (?, ?)
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, getId(obj));
            pstmt.setString(2, obj.getSkill().toString());
            return pstmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean update(SkillProficiency obj, Connection conn) throws SQLException {
        String sql = sql("""
            UPDATE %TABLE%
            SET kind = ?
            WHERE id = ?
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getSkill().toString());
            pstmt.setLong(2, getId(obj));
            return pstmt.executeUpdate() == 1;
        }
    }
}
