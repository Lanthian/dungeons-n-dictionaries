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
public class LanguageMapper implements Mapper<Language> {

    /* ======================================================================
     * --------------------- CUD Mapper Implementation  ---------------------
     * ====================================================================== */

    @Override
    public boolean insert(Language obj, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO language (name, description, script, exotic)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getDescription());
            pstmt.setString(3, obj.getScript());
            pstmt.setBoolean(4, obj.isExotic());
            return pstmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean update(Language obj, Connection conn) throws SQLException {
        String sql = """
            UPDATE language
            SET name = ?, description = ?, script = ?, exotic = ?
            WHERE id = ?
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getDescription());
            pstmt.setString(3, obj.getScript());
            pstmt.setBoolean(4, obj.isExotic());
            pstmt.setLong(5, obj.getId().value());
            return pstmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Language obj, Connection conn) throws SQLException {
        String sql = """
            DELETE FROM language
            WHERE id = ?
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, obj.getId().value());
            return pstmt.executeUpdate() == 1;
        }
    }

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
}
