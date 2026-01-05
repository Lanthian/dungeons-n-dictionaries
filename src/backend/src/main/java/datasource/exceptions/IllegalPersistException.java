// java/datasource/IllegalPersistException.java
package datasource.exceptions;

/**
 * Thin exception wrapper to translate database persistance failure due to a
 * constraint violation into a detectable and unique type.
 */
public class IllegalPersistException extends RuntimeException {

    // --- Attributes: Exception Details ---
    private final String sqlState;
    private final String constraint;
    private final String detail;

    // Constructor
    public IllegalPersistException(String sqlState, String constraint, String detail, Throwable cause) {
        super(detail, cause);
        this.sqlState = sqlState;
        this.constraint = constraint;
        this.detail = detail;
    }

    // Getters
    public String getSqlState() { return this.sqlState; }
    public String getConstraint() { return this.constraint; }
    public String getDetail() { return this.detail; }
}
