// java/datasource/mappers/LanguageMapper.java
package datasource.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import domain.core.EntityId;
import domain.modifiers.Language;

/**
 * Class for {@link Language} data mapping operations. Implementation of
 * {@link Mapper} interface.
 */
public class LanguageMapper implements Mapper<Language> {

    /* -------------------------- Read  Operations -------------------------- */

    @Override
    public Optional<Language> findById(long id, Connection conn) throws SQLException {
        String sql = """
            SELECT *
            FROM language
            WHERE id = ?
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    @Override
    public List<Language> findAll(Connection conn) throws SQLException {
        List<Language> list = new ArrayList<>();
        String sql = """
            SELECT *
            FROM language
            """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Instantiate objects from all entries in queried ResultSet
            while (rs.next()) { list.add(mapRow(rs)); }
        }
        return list;
    }

    /* ----------------------- Insert, Update, Delete ----------------------- */

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

    /* -------------------------- Row  Translation -------------------------- */

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
