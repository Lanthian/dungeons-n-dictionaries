// java/services/AbstractService.java
package services;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Abstract base service layer component - defines shared logic in services.
 *
 * <p> {@link OperationResult} is defined here for wrapping service responses
 * for Controller use.
 */
public class AbstractService {

    /* --------------------- Controller  Communication ---------------------- */

    /**
     * ENUM identifying Service operation success state.
     * Used to pass data back to the Controller calling service methods.
     */
    public enum OperationResult {
        // --- Enumerations ---
        SUCCESS(HttpServletResponse.SC_OK),
        CREATED(HttpServletResponse.SC_CREATED),
        ILLEGAL_ENTITY(HttpServletResponse.SC_BAD_REQUEST),
        DB_FAILURE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // --- Attributes ---
        private final int status;

        // Constructor
        OperationResult(int status) { this.status = status; }

        // --- Getter ---
        public int getStatus() { return this.status; }
    }
}
