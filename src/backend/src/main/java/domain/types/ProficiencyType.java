// java/domain/types/ProficiencyType.java
package domain.types;

import domain.modifiers.proficiency.ArmourProficiency;
import domain.modifiers.proficiency.Proficiency;
import domain.modifiers.proficiency.SkillProficiency;
import domain.modifiers.proficiency.ToolProficiency;

/**
 * An enum for the different proficiency subtypes available in D&D.
 */
public enum ProficiencyType {

    // --- Enumerations ---
    ARMOUR(ArmourProficiency.class),
    SKILL(SkillProficiency.class),
    TOOL(ToolProficiency.class);

    // --- Attributes ---
    private Class<? extends Proficiency> proficiencyClass;

    // ProficiencyType constructor
    ProficiencyType(Class<? extends Proficiency> c) { this.proficiencyClass = c; }

    /**
     * Converts a string to its matching ProficiencyType enum.
     *
     * @param value String representation of ProficiencyType (case-insensitive)
     * @return ProficiencyType enumeration matching {@code value}
     * @throws IllegalArgumentException if no match exists
     */
    public static ProficiencyType fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ProficiencyType value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        try {
            return ProficiencyType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown ProficiencyType: " + value);
        }
    }

    // --- Getter ---
    public Class<? extends Proficiency> getProficiencyClass() { return this.proficiencyClass; }
}
