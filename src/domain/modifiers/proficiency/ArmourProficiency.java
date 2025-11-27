// src/domain/modifiers/proficiency/ArmourProficiency.java
package domain.modifiers.proficiency;

import domain.character.Character;
import domain.types.ArmourType;

/**
 * A {@link Proficiency} providing a {@link Character} with expertise regarding
 * an {@link ArmourType}. 
 */
public class ArmourProficiency implements Proficiency {

    // --- Attributes ---
    private ArmourType type;

    // Constructor
    public ArmourProficiency(ArmourType armourType) {
        this.type = armourType;
    }

    // --- Getter ---
    public ArmourType getType() { return this.type; }
}
