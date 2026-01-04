// java/services/AsmService.java
package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import datasource.Database;
import datasource.UnitOfWork;
import datasource.mappers.MapperRegistry;
import domain.modifiers.AbilityScoreModifier;

/**
 * Service layer component for {@link AbilityScoreModifier}, containing all
 * business level logic for Controller requests and facilitating access to the
 * datasource layer.
 */
public class AsmService extends AbstractService {

    /* ------------------------ Business  Operations ------------------------ */

    /**
     * Returns a {@link AbilityScoreModifier} persisted in the database with the
     * passed ID.
     *
     * @param id uniquely identifying PK of ASM in the database
     * @return {@link AbilityScoreModifier} if found, {@code null} if otherwise
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static AbilityScoreModifier getById(long id) throws SQLException {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            Optional<AbilityScoreModifier> found =
                MapperRegistry.getMapper(AbilityScoreModifier.class).findById(id, conn);
            return found.orElse(null);
        }
    }

    /**
     * Return all {@link AbilityScoreModifier}s persisted in the database.
     *
     * @return {@link List} of AbilityScoreModifiers stored in the database
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static List<AbilityScoreModifier> getAll() throws SQLException {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            return MapperRegistry.getMapper(AbilityScoreModifier.class).findAll(conn);
        }
    }

    /**
     * Commit a newly generated {@link AbilityScoreModifier} to the database.
     * Will not commit ASMs that already have a set ID - use {@link
     * #updateAsm(AbilityScoreModifier)} instead.
     *
     * @param asm AbilityScoreModifier to insert
     * @return {@link OperationResult} defining the success state of this action
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static OperationResult createAsm(AbilityScoreModifier asm) throws SQLException {
        // Disallow entities with set IDs
        if (asm.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(asm);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.CREATED
            : OperationResult.DB_FAILURE;
    }

    /**
     * Commit changes to an existing {@link AbilityScoreModifier} to the
     * database. Will not commit ASMs that lack a set ID - use {@link
     * #createAsm(AbilityScoreModifier)} instead.
     *
     * @param asm AbilityScoreModifier to update
     * @return {@link OperationResult} defining the success state of this action
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static OperationResult updateAsm(AbilityScoreModifier asm) throws SQLException {
        // Disallow entities without set IDs - use createAsm() instead
        if (!asm.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(asm);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }

    /**
     * Deletes a {@link AbilityScoreModifier} from the database.
     *
     * @param asm AbilityScoreModifier to delete
     * @return true if delete operation was successful, false otherwise
     * @throws SQLException if an unexpected database SQL exception occurs or
     *         if {@link Database#getConnection()} is interupted
     */
    public static OperationResult deleteAsm(AbilityScoreModifier asm) throws SQLException {
        // Disallow entities without set IDs
        if (!asm.hasId()) return OperationResult.ILLEGAL_ENTITY;

        // TODO: Lightweight object is fine here, only ID is needed to delete
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(asm);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }
}
