// java/datasource/mappers/AsmMapper.java
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
import domain.modifiers.AbilityScoreModifier;
import domain.types.Ability;

/**
 * Class for {@link AbilityScoreModifier} data mapping operations.
 * Implementation of {@link Mapper} interface.
 */
public class AsmMapper implements Mapper<AbilityScoreModifier> {

    /* -------------------------- Read  Operations -------------------------- */

    @Override
    public Optional<AbilityScoreModifier> findById(long id, Connection conn) throws SQLException {
        String sql = """
            SELECT *
            FROM asm
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
    public List<AbilityScoreModifier> findAll(Connection conn) throws SQLException {
        List<AbilityScoreModifier> list = new ArrayList<>();
        String sql = """
            SELECT *
            FROM asm
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
    public boolean insert(AbilityScoreModifier obj, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO asm (ability, value)
            VALUES (?, ?)
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getAbility().getShorthand());
            pstmt.setInt(2, obj.getValue());
            return pstmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean update(AbilityScoreModifier obj, Connection conn) throws SQLException {
        String sql = """
            UPDATE asm
            SET ability = ?, value = ?
            WHERE id = ?
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getAbility().getShorthand());
            pstmt.setInt(2, obj.getValue());
            pstmt.setLong(3, obj.getId().value());
            return pstmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(AbilityScoreModifier obj, Connection conn) throws SQLException {
        String sql = """
            DELETE FROM asm
            WHERE id = ?
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, obj.getId().value());
            return pstmt.executeUpdate() == 1;
        }
    }

    /* -------------------------- Row  Translation -------------------------- */

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

}
