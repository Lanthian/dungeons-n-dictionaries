// src/domain/Skill.java
package domain;

/**
 * An enum for the different character skills available in D&D.
 */
public enum Skill {
    
    // --- Enumerations ---
    ACROBATICS(Ability.DEXTERITY),
    ANIMAL_HANDLING(Ability.WISDOM),
    ARCANA(Ability.INTELLIGENCE),
    ATHLETICS(Ability.STRENGTH),
    DECEPTION(Ability.CHARISMA),
    HISTORY(Ability.INTELLIGENCE),
    INSIGHT(Ability.WISDOM),
    INTIMIDATION(Ability.CHARISMA),
    INVESTIGATION(Ability.INTELLIGENCE),
    MEDICINE(Ability.WISDOM),
    NATURE(Ability.INTELLIGENCE),
    PERCEPTION(Ability.WISDOM),
    PERFORMANCE(Ability.CHARISMA),
    PERSUASION(Ability.CHARISMA),
    RELIGION(Ability.INTELLIGENCE),
    SLEIGHT_OF_HAND(Ability.DEXTERITY),
    STEALTH(Ability.DEXTERITY),
    SURVIVAL(Ability.WISDOM);

    // --- Attributes ---
    private Ability ability; 

    // Skill constructor
    Skill(Ability ability) { this.ability = ability; }

    /**
     * Converts a string to its matching Skill enum.
     * 
     * @param value String representation of a Skill (case-insensitive). 
     * @return Skill enumeration matching {@code value}
     * @throws IllegalArgumentException Throws exception if no match exists.
     */
    public static Skill fromString(String value) throws IllegalArgumentException {
        // Check if input can even be converted
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Skill value cannot be null or blank");
        }
        // Try conversion, throwing an exception if unrecognised
        try {
            return Skill.valueOf(value.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown Skill: " + value);
        }
    }

    // --- Getter ---
    public Ability getAbility() { return this.ability; }
}
