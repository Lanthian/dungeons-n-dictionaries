// java/datasource/PersistenceException.java
package datasource.exceptions;

/**
 * Thin exception wrapper to translate database persistance failure to a
 * detectable and unique type. Use {@link IllegalPersistException} instead in
 * the case of a database constraint violation.
 */
public class PersistenceException extends RuntimeException {

    /* ---------------------------- Constructors ---------------------------- */

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(String message) {
        super(message);
    }
}
