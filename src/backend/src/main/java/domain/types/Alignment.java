// java/domain/types/Alignment.java
package domain.types;

/**
 * An enum for recommended character/creature alignment within the D&D mythos.
 */
public enum Alignment {

    // --- Enumerations ---
    LAWFUL_GOOD,
    NEUTRAL_GOOD,
    CHAOTIC_GOOD,
    LAWFUL_NEUTRAL,
    NEUTRAL,
    CHAOTIC_NEUTRAL,
    LAWFUL_EVIL,
    NEUTRAL_EVIL,
    CHAOTIC_EVIL;

    /**
     * Converts a string to its matching Alignment enum.
     *
     * @param value String representation of an Alignment (case-insensitive)
     * @return Alignment enumeration matching {@code value}
     * @throws IllegalArgumentException Throws exception if no match exists.
     */
    public static Alignment fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Alignment value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        try {
            return Alignment.valueOf(value.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown Alignment: " + value);
        }
    }
}
