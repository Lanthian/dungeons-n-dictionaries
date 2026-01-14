// java/services/FeatService.java
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
import domain.modifiers.Feat;
import domain.types.ModificationType;

/**
 * Service layer component for {@link Feat}, containing all
 * business level logic for Controller requests and facilitating access to the
 * datasource layer.
 */
public class FeatService extends AbstractService {

    /* ------------------------ Business  Operations ------------------------ */

    /**
     * Returns a {@link Feat} persisted in the database with the passed ID.
     * Feat is returned with all supplied CharacterModifications included.
     *
     * @param id uniquely identifying PK of feat in the database
     * @return {@link Feat} object if found, {@code null} if otherwise
     */
    public static Feat getById(long id) {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            Optional<Feat> found =
                MapperRegistry.getMapper(Feat.class).findById(id, conn);
            return found.orElse(null);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Return a list of all shallowly instantiated {@link Feat}s persisted in
     * the database. Does not include any supplied CharacterModifications.
     *
     * @return {@link List} of Feats stored in the database
     */
    public static List<Feat> getAll() {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            return MapperRegistry.getMapper(Feat.class).findAll(conn);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Commit a newly generated {@link Feat} to the database.
     * Will not commit feats that already have a set ID - use {@link
     * #updateFeat(Feat)} instead.
     *
     * @param feat Feat to insert
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult createFeat(Feat feat) {
        // Disallow entities with set IDs
        if (feat.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(feat);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.CREATED
            : OperationResult.DB_FAILURE;
    }

    /**
     * Commit changes to an existing {@link Feat} to the
     * database. Will not commit feats that lack a set ID - use {@link
     * #createFeat(Feat)} instead.
     *
     * @param feat Feat to update
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult updateFeat(Feat feat) {
        // Disallow entities without set IDs - use createFeat() instead
        if (!feat.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(feat);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }

    /**
     * Deletes a {@link Feat} from the database.
     *
     * @param feat Feat to delete
     * @return true if delete operation was successful, false otherwise
     */
    public static OperationResult deleteFeat(Feat feat) {
        // Disallow entities without set IDs
        if (!feat.hasId()) return OperationResult.ILLEGAL_ENTITY;

        // TODO: Lightweight object is fine here, only ID is needed to delete
        UnitOfWork.newCurrent();

        // Deleted supplied instances of CharacterModification first
        CharacterModifierMapper supplyMapper = new CharacterModifierMapper();
        UnitOfWork.getCurrent().registerWork(conn ->
            supplyMapper.deleteAllForSupply(ModificationType.FEAT, feat, conn)
        );

        UnitOfWork.getCurrent().registerDeleted(feat);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }
}
