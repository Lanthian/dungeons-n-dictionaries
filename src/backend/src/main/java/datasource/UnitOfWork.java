// java/datasource/UnitOfWork.java
package datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasource.mappers.Mapper;
import datasource.mappers.MapperRegistry;
import datasource.utils.SQLExceptionTranslator;

/**
 * Unit Of Work design pattern to improve database transaction efficiency.
 * Unit of automatically clears itself after committing to protect against stale
 * state.
 *
 * <ul>
 *   <li> Request a new UnitOfWork via {@link UnitOfWork#newCurrent()},
 *   <li> Retrieve a current UnitOfWork via {@link UnitOfWork#getCurrent()}
 *   <li> Commit registered changes via {@link UnitOfWork#commit()}
 * </ul>
 */
public class UnitOfWork {

    // UnitOfWork thread instance
    @SuppressWarnings("rawtypes")
    private static ThreadLocal current = new ThreadLocal();

    // --- Attributes: Object Lists ---
    private List<Object> newObjects = new ArrayList<>();
    private List<Object> dirtyObjects = new ArrayList<>();
    private List<Work> additionalWork = new ArrayList<>();
    private List<Object> deletedObjects = new ArrayList<>();

    // UnitOfWork initialisation, retrieval and management methods
    public static void newCurrent() { setCurrent(new UnitOfWork()); }
    @SuppressWarnings("unchecked")
    public static void setCurrent(UnitOfWork uow) { current.set(uow); }
    public static UnitOfWork getCurrent() { return (UnitOfWork) current.get(); }
    public static void clearCurrent() { current.remove(); }

    /* ======================================================================
     * ------------------------ Object  Registration ------------------------
     * ====================================================================== */

    /**
     * Register new object.
     *
     * @param obj new object to insert
     */
    public void registerNew(Object obj) {
        if (!newObjects.contains(obj) && !dirtyObjects.contains(obj) && !deletedObjects.contains(obj)) {
            newObjects.add(obj);
        }
    }

    /**
     * Register dirtied object.
     *
     * @param obj dirtied object to update
     */
    public void registerDirty(Object obj) {
        if (!newObjects.contains(obj) && !dirtyObjects.contains(obj) && !deletedObjects.contains(obj)) {
            dirtyObjects.add(obj);
        }
    }

    /**
     * Register deleted object.
     *
     * @param obj object to delete
     */
    public void registerDeleted(Object obj) {
        // No need to register deletion of object if not yet in database
        if (newObjects.remove(obj)) return;

        // Otherwise, remove object from dirty set before adding to deletion set
        dirtyObjects.remove(obj);
        if (!deletedObjects.contains(obj)) deletedObjects.add(obj);
    }

    /**
     * Commit transaction, inserting new, updating dirty, and removing deleted
     * objects in the database. Does so in an atomic manner, rolling back
     * database changes in case of failure
     *
     * @return true if all database commits succeed, false if any failures
     */
    public boolean commit() {
        try (Connection conn = Database.getConnection()) {
            boolean autoCommitDefault = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                /* Repetitive code moved into processList helper function. Each
                 * list of objects in UoW has a distinct mapper action applied
                 * to it.
                */
                boolean failure = false;
                // Stop transaction early if failure detected
                if (!processList(newObjects, (m, o) -> m.insert(o, conn), conn)) failure = true;
                else if (!processList(dirtyObjects, (m, o) -> m.update(o, conn), conn)) failure = true;
                else if (!executeWork(conn)) failure = true;
                else if (!processList(deletedObjects, (m, o) -> m.delete(o, conn), conn)) failure = true;

                // On DB update failure, rollback before returning
                if (failure) { conn.rollback(); return false; }

                conn.commit();
                return true;
            } catch (Exception e) {
                // If any database action fails, rollback full UoW commit
                conn.rollback();
                throw e;
            } finally {
                // Safety reset of autoCommit state
                conn.setAutoCommit(autoCommitDefault);
                // Cleanup to protect against stale/duplicate state
                UnitOfWork.clearCurrent();
            }

        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate(e);
        }
    }

    /* ======================================================================
     * -------------------- Additional Work Registration --------------------
     * ====================================================================== */

    /**
     * A {@link FunctionalInterface} defining the contract additional UnitOfWork
     * transaction 'work' must fulfil to be included in the UoW flow.
     *
     * <p> Take a {@link Connection} object and perform a user defined operation
     * with it.
     */
    @FunctionalInterface
    public interface Work {
        /**
         * @param conn database Connection object
         * @return true if operation was successful, false otherwise
         */
        boolean execute(Connection conn);
    }

    /**
     * Register additional transaction locked work.
     *
     * @param work new work to queue performance of
     */
    public void registerWork(Work work) {
        additionalWork.add(work);
    }

    /* ======================================================================
     * ------------------------ Private Utility Code ------------------------
     * ====================================================================== */

    /**
     * m : Mapper<Object>,
     * o : Object,
     * c : Connection;
     * (m, o) -> m.MapperAction(o, c)
     */
    @FunctionalInterface
    private interface MapperAction {
        boolean apply(Mapper<Object> mapper, Object obj) throws SQLException;
    }

    /**
     * Private helper function to reduce repetitive code. Applys a MapperAction
     * to a List of Object entities, pushing changes to a Connection. Rollbacks
     * changes if action fails.
     *
     * @param objects List of objects over which action will be applied
     * @param action BiFunction taking a Mapper m, Object o, and then applying
     *               specified MapperAction to it.
     * @param conn database {@link Connection} object
     * @return true if action applied succeeds on all objects, false otherwise
     * @throws SQLException
     */
    private boolean processList(List<Object> objects, MapperAction action, Connection conn) throws SQLException {
        for (Object obj : objects) {
            /* Type casts registry returned Mapper to Mapper<Object> to allow
             * .insert()/.update()/.delete() methods. Applicable Mapper must be
             * pre-registered in MapperRegistry.
            */
            @SuppressWarnings("unchecked")
            Mapper<Object> mapper = (Mapper<Object>) MapperRegistry.getMapper(obj.getClass());
            // Apply insert/update/delete action
            if (!action.apply(mapper, obj)) {
                // If action fails, rollback database changes
                conn.rollback();
                return false;
            }
        }
        return true;
    }

    /**
     * Private helper function to enact all additional work registered in order,
     * returning success/failure status of batch operations. Rollbacks changes
     * if action fails.
     *
     * @param conn database {@link Connection} object
     * @return true if all work performed succeeds, false otherwise
     * @throws SQLException
     */
    private boolean executeWork(Connection conn) throws SQLException {
        for (Work work : additionalWork) {
            // Perform additional work
            if (!work.execute(conn)) {
                // If action fails, rollback database changes
                conn.rollback();
                return false;
            }
        }
        return true;
    }
}
