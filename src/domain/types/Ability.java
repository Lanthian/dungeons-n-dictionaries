// src/domain/types/Ability.java
package domain.types;

import java.util.HashMap;

/**
 * An enum for the different character abilities available in D&D.
 */
public enum Ability {
    
    // --- Enumerations ---
    STRENGTH("STR"),
    DEXTERITY("DEX"),
    CONSTITUTION("CON"),
    INTELLIGENCE("INT"),
    WISDOM("WIS"),
    CHARISMA("CHA");

    // --- Attributes ---
    private String shorthand; 

    // Ability constructor
    Ability(String shorthand) { this.shorthand = shorthand; }

    // Combined fullname and shorthand lookup for abilities
    private static final HashMap<String, Ability> LOOKUP = new HashMap<>();
    static {
        for (Ability ability : Ability.values()) {
            LOOKUP.put(ability.name(), ability);
            LOOKUP.put(ability.shorthand, ability);
        }
    }

    /**
     * Converts a string to its matching Ability enum.
     * 
     * @param value String representation of an Ability (case-insensitive). 
     *          Accepts both full names and shorthand (e.g. "STR"). 
     * @return Ability enumeration matching {@code value}
     * @throws IllegalArgumentException Throws exception if no match exists.
     */
    public static Ability fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Ability value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        Ability ability = LOOKUP.get(value.trim().toUpperCase());
        if (ability != null) return ability;
        throw new IllegalArgumentException("Unknown Ability: " + value);
    }
}
