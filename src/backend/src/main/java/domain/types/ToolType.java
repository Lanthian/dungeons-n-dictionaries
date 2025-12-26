// java/domain/types/ToolType.java
package domain.types;

/**
 * An enum for the different tool subtypes available in D&D.
 */
public enum ToolType {

    // --- Enumerations ---
    ARTISAN_TOOLS,
    GAMING_SET,
    MUSICAL_INSTRUMENT,
    MISCELLANEOUS;

    /**
     * Converts a string to its matching ToolType enum.
     *
     * @param value String representation of an ToolType (case-insensitive).
     * @return ToolType enumeration matching {@code value}
     * @throws IllegalArgumentException if no match exists
     */
    public static ToolType fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ToolType value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        try {
            return ToolType.valueOf(value.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown ToolType: " + value);
        }
    }
}
