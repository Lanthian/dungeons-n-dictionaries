// java/services/LanguageServices.java
package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import datasource.Database;
import datasource.mappers.MapperRegistry;
import domain.modifiers.Language;

/**
 * Service layer component for {@link Language}, containing all business level
 * logic for Controller requests and facilitating access to the datasource
 * layer.
 */
public class LanguageService {

    /**
     * Return all {@link Language}s persisted in the database.
     *
     * @return {@link List} of Languages stored in the database
     */
    public static List<Language> getAll() throws SQLException {
        Connection conn = Database.getConnection();
        return MapperRegistry.getMapper(Language.class).findAll(conn);
    }

}
