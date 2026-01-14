// java/services/AsmService.java
package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import datasource.Database;
import datasource.UnitOfWork;
import datasource.mappers.CharacterModifierMapper;
import datasource.mappers.MapperRegistry;
import datasource.utils.SQLExceptionTranslator;
import domain.modifiers.AbilityScoreModifier;
import domain.types.ModificationType;

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
     */
    public static AbilityScoreModifier getById(long id) {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            Optional<AbilityScoreModifier> found =
                MapperRegistry.getMapper(AbilityScoreModifier.class).findById(id, conn);
            return found.orElse(null);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Return all {@link AbilityScoreModifier}s persisted in the database.
     *
     * @return {@link List} of AbilityScoreModifiers stored in the database
     */
    public static List<AbilityScoreModifier> getAll() {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            return MapperRegistry.getMapper(AbilityScoreModifier.class).findAll(conn);
        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Commit a newly generated {@link AbilityScoreModifier} to the database.
     * Will not commit ASMs that already have a set ID - use {@link
     * #updateAsm(AbilityScoreModifier)} instead.
     *
     * @param asm AbilityScoreModifier to insert
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult createAsm(AbilityScoreModifier asm) {
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
     */
    public static OperationResult updateAsm(AbilityScoreModifier asm) {
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
     */
    public static OperationResult deleteAsm(AbilityScoreModifier asm) {
        // Disallow entities without set IDs
        if (!asm.hasId()) return OperationResult.ILLEGAL_ENTITY;

        // TODO: Lightweight object is fine here, only ID is needed to delete
        UnitOfWork.newCurrent();

        // Deleted supplied instances of CharacterModification first
        CharacterModifierMapper supplyMapper = new CharacterModifierMapper();
        UnitOfWork.getCurrent().registerWork(conn ->
            supplyMapper.deleteAllForSupply(ModificationType.ASM, asm, conn)
        );

        UnitOfWork.getCurrent().registerDeleted(asm);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }
}
