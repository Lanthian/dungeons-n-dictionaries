// src/domain/ArmourType.java
package domain;

/**
 * An enum for the different armour subtypes available in D&D. 
 */
public enum ArmourType {
    
    // --- Enumerations ---
    LIGHT("1 minute", "1 minute"),
    MEDIUM("5 minutes", "1 minute"),
    HEAVY("10 minutes", "5 minutes"),
    SHIELD("1 action", "1 action");

    // --- Attributes ---
    private String don;
    private String doff;

    // ArmourType constructor
    ArmourType(String don, String doff) { 
        this.don = don;
        this.doff = doff;
    }

    /**
     * Converts a string to its matching ArmourType enum.
     * 
     * @param value String representation of an ArmourType (case-insensitive). 
     * @return Skill enumeration matching {@code value}
     * @throws IllegalArgumentException Throws exception if no match exists.
     */
    public static Skill fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ArmourType value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        try {
            return Skill.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown ArmourType: " + value);
        }
    }

    // --- Getters ---
    public String getDon() { return this.don; }
    public String getDoff() { return this.doff; }
}
