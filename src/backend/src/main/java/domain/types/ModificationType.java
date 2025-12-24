// java/domain/types/ModificationType.java
package domain.types;

/**
 * An enum for the different modification subtypes provided to characters.
 */
public enum ModificationType {

    // --- Enumerations ---
    ASM,
    FEAT,
    LANGUAGE,
    PROFICIENCY;

    /**
     * Converts a string to its matching ModifierType enum.
     *
     * @param value String representation of ModifierType (case-insensitive)
     * @return ModifierType enumeration matching {@code value}
     * @throws IllegalArgumentException if no match exists
     */
    public static ModificationType fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ModifierType value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        try {
            return ModificationType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown ModifierType: " + value);
        }
    }
}
