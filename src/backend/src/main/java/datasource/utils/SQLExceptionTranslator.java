// java/datasource/mappers/AbstractMapper.java
package datasource.utils;

import java.sql.SQLException;
import java.util.Set;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import datasource.exceptions.IllegalPersistException;
import datasource.exceptions.PersistenceException;

/**
 * Utility class to translate {@link PSQLException}s and {@link SQLException}s
 * into {@link IllegalPersistException}s and {@link PersistenceException}s.
 */
public class SQLExceptionTranslator {

    // --- Constants ---
    // PostgreSQL desired error codes:
    //  23505 UNIQUE violation
    //  23503 FOREIGN KEY violation
    //  23502 NOT NULL violation
    //  23514 CHECK constraint violation
    //  23001 RESTRICT violation
    private static final Set<String> ILLEGAL_PERSIST_STATES = Set.of(
        "23505", "23503", "23502", "23514", "23001"
    );

    /**
     * @param e {@link SQLException} thrown/captured
     * @return {@link IllegalPersistException} or {@link PersistenceException}
     *         depending on the data available in the exception captured
     */
    public static RuntimeException translate(SQLException e) {
        // Verify Exception is of PSQL origin before checking cause
        if (e instanceof PSQLException psql) {
            ServerErrorMessage sem = psql.getServerErrorMessage();
            String sqlState = psql.getSQLState();

            // Check integrity constraint violation
            if (sqlState != null && ILLEGAL_PERSIST_STATES.contains(sqlState)) {
                return new IllegalPersistException(
                    sqlState,
                    sem != null ? sem.getConstraint() : null,
                    // Use default exception error message if SEM not sent
                    sem != null ? sem.getDetail() : psql.getMessage(),
                    psql
                );
            }
        }
        System.err.println("Database error: " + e.getMessage());
        return new PersistenceException("Database error", e);
    }
}
