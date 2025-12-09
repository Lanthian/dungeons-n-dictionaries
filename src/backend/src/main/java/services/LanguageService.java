// java/services/LanguageService.java
package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import datasource.Database;
import datasource.UnitOfWork;
import datasource.mappers.MapperRegistry;
import domain.modifiers.Language;

/**
 * Service layer component for {@link Language}, containing all business level
 * logic for Controller requests and facilitating access to the datasource
 * layer.
 */
public class LanguageService {

    /* --------------------- Controller  Communication ---------------------- */

    /**
     * ENUM identifying {@link LanguageService} operation success state.
     * Used to pass data back to the Controller calling service methods.
     */
    public enum OperationResult {
        // --- Enumerations ---
        SUCCESS(true),
        ILLEGAL_ENTITY(false),
        DB_FAILURE(false);

        // --- Attributes ---
        private final boolean success;

        // Constructor
        OperationResult(boolean success) { this.success = success; }

        // --- Getter ---
        public boolean isSuccess() { return this.success; }
    }

    /* ------------------------ Business  Operations ------------------------ */

    /**
     * Return all {@link Language}s persisted in the database.
     *
     * @return {@link List} of Languages stored in the database
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static List<Language> getAll() throws SQLException {
        // TODO: Delegate this operation to a repository
        Connection conn = Database.getConnection();
        return MapperRegistry.getMapper(Language.class).findAll(conn);
    }

    /**
     * Commit a newly generated {@link Language} to the database.
     * Will not commit languages that already have a set ID - use {@link
     * #updateLanguage(Language)} instead.
     *
     * @param language Language to insert
     * @return {@link OperationResult} defining the success state of this action
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static OperationResult createLanguage(Language language) throws SQLException {
        // Disallow languages with set IDs
        if (language.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(language);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }

    /**
     * Commit changes to an existing {@link Language} to the database.
     * Will not commit languages that lack a set ID - use {@link
     * #createLanguage(Language)} instead.
     *
     * @param language Language to update
     * @return {@link OperationResult} defining the success state of this action
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static OperationResult updateLanguage(Language language) throws SQLException {
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
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static OperationResult deleteLanguage(Language language) throws SQLException {
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
