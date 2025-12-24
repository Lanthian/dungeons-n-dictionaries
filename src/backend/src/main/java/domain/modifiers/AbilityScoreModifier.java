// java/domain/modifiers/AbilityScoreModifier.java
package domain.modifiers;

import domain.character.Character;
import domain.character.CharacterModification;
import domain.core.Entity;
import domain.types.Ability;
import domain.types.ModificationType;
import domain.utils.StringUtils;

/**
 * An Ability score increase or decrease provided to a {@link Character}.
 * Immutable once created.
 */
public class AbilityScoreModifier extends Entity<AbilityScoreModifier> implements CharacterModification {

    // --- Attributes ---
    private final Ability ability;
    private final int value;

    // Constructor
    public AbilityScoreModifier(Ability ability, int value) {
        this.ability = ability;
        this.value = value;
    }

    /* ======================================================================
     * ---------------- CharacterModification Implementation ----------------
     * ====================================================================== */

    @Override
    public ModificationType modType() { return ModificationType.ASM; }

    /* ====================================================================== */

    // --- Getters ---
    public Ability getAbility() { return this.ability; }
    public int getValue() { return this.value; }

    // To String
    @Override
    public String toString() {
        return StringUtils.toStringJoiner("AbilityScoreModifier")
            .add("ability=" + ability)
            .add("value=" + value)
            .toString();
    }
}
