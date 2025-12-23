// java/domain/modifiers/proficiency/ArmourProficiency.java
package domain.modifiers.proficiency;

import domain.character.Character;
import domain.types.ArmourType;
import domain.utils.StringUtils;

/**
 * A {@link Proficiency} providing a {@link Character} with expertise regarding
 * an {@link ArmourType}.
 */
public class ArmourProficiency extends Proficiency {

    // --- Attributes ---
    private ArmourType type;

    // Constructor
    public ArmourProficiency(ArmourType armourType) {
        this.type = armourType;
    }

    // --- Getter ---
    public ArmourType getType() { return this.type; }

    // To String
    @Override
    public String toString() {
        return StringUtils.toStringJoiner("ArmourProficiency")
            .add("type=" + type)
            .toString();
    }
}
