// java/services/ProficiencyService.java
package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import datasource.Database;
import datasource.UnitOfWork;
import datasource.mappers.CharacterModifierMapper;
import datasource.mappers.MapperRegistry;
import datasource.mappers.proficiency.ProficiencyMapper;
import datasource.utils.SQLExceptionTranslator;
import domain.modifiers.proficiency.ArmourProficiency;
import domain.modifiers.proficiency.Proficiency;
import domain.modifiers.proficiency.SkillProficiency;
import domain.modifiers.proficiency.ToolProficiency;
import domain.types.ModificationType;
import domain.types.ProficiencyType;

/**
 * Service layer component for {@link Proficiency} entities, containing all
 * business level logic for Controller requests and facilitating access to the
 * datasource layer.
 */
public class ProficiencyService extends AbstractService {

    /* ------------------------ Business  Operations ------------------------ */

    /**
     * Returns a {@link Proficiency} persisted in the database with the passed
     * ID.
     *
     * @param id uniquely identifying PK of proficiency in the database
     * @return {@link Proficiency} object if found, {@code null} if otherwise
     */
    public static Proficiency getById(long id) {
        // TODO: Delegate this operation to a repository
        try (Connection conn = Database.getConnection()) {
            Optional<Proficiency> found = MapperRegistry.getMapper(Proficiency.class).findById(id, conn);
            return found.orElse(null);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /* ........................... getAllByType() ........................... */

    /**
     * Returns a list of all proficiencies of the specified {@link
     * ProficiencyType}.
     *
     * @param type {@link ProficiencyType} correlating to the specified class
     * @return a deeply initiated {@link List} of proficiencies of queried type
     */
    public static List<? extends Proficiency> getAllByType(ProficiencyType type) {
        try (Connection conn = Database.getConnection()) {
            ProficiencyMapper mapper = (ProficiencyMapper) MapperRegistry.getMapper(Proficiency.class);
            return mapper.findAllByType(type, conn);

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Private utility method to reduce duplicate code between `getAll*()` type
     * methods. Returns all proficiencies of the specified {@link
     * ProficiencyType} in a list, casted to the specified class.
     *
     * <p> WARNING: Take care to make sure {@code type} and {@code classOfT} are
     * in fact related.
     *
     * @param <T> type of the proficiency casted to
     * @param type {@link ProficiencyType} correlating to the specified class
     * @param classOfT {@code .class} of the proficiency queried
     * @return a deeply initiated {@link List} of proficiencies of queried type
     */
    private static <T extends Proficiency> List<T> getAllByType(
        ProficiencyType type, Class<T> classOfT
    ) {
        try (Connection conn = Database.getConnection()) {
            ProficiencyMapper mapper = (ProficiencyMapper) MapperRegistry.getMapper(Proficiency.class);
            return mapper.findAllByType(type, conn)
                .stream()
                .map(classOfT::cast)
                .toList();

        } catch (SQLException e) {
            // Catch unexpected SQLException thrown by Connection on .close()
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /**
     * Return all {@link ArmourProficiency}s persisted in the database.
     *
     * @return {@link List} of proficiencies stored in the database
     */
    public static List<ArmourProficiency> getAllArmour() {
        return getAllByType(ProficiencyType.ARMOUR, ArmourProficiency.class);
    }

    /**
     * Return all {@link SkillProficiency}s persisted in the database.
     *
     * @return {@link List} of proficiencies stored in the database
     */
    public static List<SkillProficiency> getAllSkills() {
        return getAllByType(ProficiencyType.SKILL, SkillProficiency.class);
    }

    /**
     * Return all {@link ToolProficiency}s persisted in the database.
     *
     * @return {@link List} of proficiencies stored in the database
     */
    public static List<ToolProficiency> getAllTools() {
        return getAllByType(ProficiencyType.TOOL, ToolProficiency.class);
    }

    /* ...................................................................... */

    /**
     * Commit a newly generated {@link Proficiency} to the database.
     * Will not commit proficiencies that already have a set ID - use {@link
     * #updateProficiency(Proficiency)} instead.
     *
     * @param proficiency Proficiency to insert
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult createProficiency(Proficiency proficiency) {
        // Disallow entities with set IDs
        if (proficiency.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(proficiency);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.CREATED
            : OperationResult.DB_FAILURE;
    }

    /**
     * Commit changes to an existing {@link Proficiency} to the database.
     * Will not commit proficiencies that lack a set ID - use {@link
     * #createProficiency(Proficiency)} instead.
     *
     * @param proficiency Proficiency to update
     * @return {@link OperationResult} defining the success state of this action
     */
    public static OperationResult updateProficiency(Proficiency proficiency) {
        // Disallow entities without set IDs - use createProficiency() instead
        if (!proficiency.hasId()) return OperationResult.ILLEGAL_ENTITY;

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(proficiency);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }

    /**
     * Deletes a {@link Proficiency} from the database.
     *
     * @param proficiency Proficiency to delete
     * @return true if delete operation was successful, false otherwise
     */
    public static OperationResult deleteProficiency(Proficiency proficiency) {
        // Disallow proficiencies without set IDs
        if (!proficiency.hasId()) return OperationResult.ILLEGAL_ENTITY;

        // TODO: Lightweight object is fine here, only ID is needed to delete
        UnitOfWork.newCurrent();

        // Deleted supplied instances of CharacterModification first
        CharacterModifierMapper supplyMapper = new CharacterModifierMapper();
        UnitOfWork.getCurrent().registerWork(conn ->
            supplyMapper.deleteAllForSupply(ModificationType.PROFICIENCY, proficiency, conn)
        );

        UnitOfWork.getCurrent().registerDeleted(proficiency);
        return UnitOfWork.getCurrent().commit()
            ? OperationResult.SUCCESS
            : OperationResult.DB_FAILURE;
    }
}
