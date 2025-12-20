// java/datasource/mappers/LanguageMapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.core.EntityId;
import domain.modifiers.Language;

/**
 * Class for {@link Language} data mapping operations. Implementation of
 * {@link Mapper} interface.
 */
public class LanguageMapper extends AbstractMapper<Language> {

    /* ------------------------- Relational Mapping ------------------------- */

    @Override
    protected String tableName() { return "language"; }

    @Override
    protected long getId(Language obj) { return obj.getId().value(); }

    @Override
    public Language mapRow(ResultSet rs) throws SQLException {
        Language lang = new Language(
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("script"),
            rs.getBoolean("exotic")
        );
        // Set ID
        lang.setId(new EntityId<>(rs.getLong("id")));
        return lang;
    }

    /* -------------------------- Insert &  Update -------------------------- */

    @Override
    public PreparedStatement insertStatement(Language obj, Connection conn) throws SQLException {
        String sql = sql("""
            INSERT INTO %TABLE% (name, description, script, exotic)
            VALUES (?, ?, ?, ?)
            """);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, obj.getName());
        pstmt.setString(2, obj.getDescription());
        pstmt.setString(3, obj.getScript());
        pstmt.setBoolean(4, obj.isExotic());
        return pstmt;
    }

    @Override
    public PreparedStatement updateStatement(Language obj, Connection conn) throws SQLException {
        String sql = sql("""
            UPDATE %TABLE%
            SET name = ?, description = ?, script = ?, exotic = ?
            WHERE id = ?
            """);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, obj.getName());
        pstmt.setString(2, obj.getDescription());
        pstmt.setString(3, obj.getScript());
        pstmt.setBoolean(4, obj.isExotic());
        pstmt.setLong(5, getId(obj));
        return pstmt;
    }
}
