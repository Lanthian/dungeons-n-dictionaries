// java/services/LanguageService.java
package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import datasource.Database;
import datasource.UnitOfWork;
import datasource.mappers.MapperRegistry;
import datasource.utils.SQLExceptionTranslator;
import domain.modifiers.Language;

/**
 * Service layer component for {@link Language}, containing all business level
 * logic for Controller requests and facilitating access to the datasource
 * layer.
 */
public class LanguageService extends AbstractService {

    /* ------------------------ Business  Operations ------------------------ */

    /**
     * Returns a {@link Language} persisted in the database with the passed ID.
     *
     * @param id uniquely identifying PK of language in the database
     * @return {@link Language} object if found, {@code null} if otherwise
     */
    public static Language getById(long id) {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            Optional<Language> found = MapperRegistry.getMapper(Language.class).findById(id, conn);
            return found.orElse(null);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Return all {@link Language}s persisted in the database.
     *
     * @return {@link List} of Languages stored in the database
     */
    public static List<Language> getAll() {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            return MapperRegistry.getMapper(Language.class).findAll(conn);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Commit a newly generated {@link Language} to the database.
     * Will not commit languages that already have a set ID - use {@link
     * #updateLanguage(Language)} instead.
     *
     * @param language Language to insert
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult createLanguage(Language language) {
        // Disallow languages with set IDs
        if (language.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(language);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.CREATED
            : OperationResult.DB_FAILURE;
    }

    /**
     * Commit changes to an existing {@link Language} to the database.
     * Will not commit languages that lack a set ID - use {@link
     * #createLanguage(Language)} instead.
     *
     * @param language Language to update
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult updateLanguage(Language language) {
        // Disallow languages without set IDs - use createLanguage() instead
        if (!language.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(language);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }

    /**
     * Deletes a {@link Language} from the database.
     *
     * @param language Language to delete
     * @return true if delete operation was successful, false otherwise
     */
    public static OperationResult deleteLanguage(Language language) {
        // Disallow languages without set IDs - use createLanguage() instead
        if (!language.hasId()) return OperationResult.ILLEGAL_ENTITY;

        // TODO: Lightweight object is fine here, only ID is needed to delete
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(language);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }
}
