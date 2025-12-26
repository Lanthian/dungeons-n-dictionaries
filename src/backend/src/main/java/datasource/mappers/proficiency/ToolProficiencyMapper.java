// java/datasource/mappers/proficiency/ToolProficiencyMapper.java
package datasource.mappers.proficiency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.mappers.AbstractMapper;
import datasource.mappers.Mapper;
import domain.core.EntityId;
import domain.modifiers.proficiency.ToolProficiency;
import domain.types.ToolType;

/**
 * Class for {@link ToolProficiency} data mapping operations.
 * Implementation of {@link Mapper} interface, extending {@link AbstractMapper}.
 * Requires handling by the {@link ProficiencyMapper} class, as the ID values
 * passed around are primary foreign keys from an aggregate proficiency table.
 */
public class ToolProficiencyMapper extends AbstractMapper<ToolProficiency> {

    /* ------------------------- Relational Mapping ------------------------- */

    @Override
    protected String tableName() { return "tool_proficiency"; }

    @Override
    protected long getId(ToolProficiency obj) { return obj.getId().value(); }

    @Override
    public ToolProficiency mapRow(ResultSet rs) throws SQLException {
        ToolProficiency obj = new ToolProficiency(
            rs.getString("name"),
            rs.getString("description"),
            ToolType.fromString(rs.getString("kind"))
        );
        // Set ID
        obj.setId(new EntityId<>(rs.getLong("id")));
        return obj;
    }

    /* -------------------------- Insert &  Update -------------------------- */

    @Override
    public boolean insert(ToolProficiency obj, Connection conn) throws SQLException {
        String sql = sql("""
            INSERT INTO %TABLE% (id, name, description, kind)
            VALUES (?, ?, ?, ?)
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, getId(obj));
            pstmt.setString(2, obj.getName());
            pstmt.setString(3, obj.getDescription());
            pstmt.setString(4, obj.getType().toString());
            return pstmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean update(ToolProficiency obj, Connection conn) throws SQLException {
        String sql = sql("""
            UPDATE %TABLE%
            SET name = ?, description = ?, kind = ?
            WHERE id = ?
            """);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getDescription());
            pstmt.setString(3, obj.getType().toString());
            pstmt.setLong(4, getId(obj));
            return pstmt.executeUpdate() == 1;
        }
    }
}
