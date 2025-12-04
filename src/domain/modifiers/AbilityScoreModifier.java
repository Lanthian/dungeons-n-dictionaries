// src/domain/modifiers/AbilityScoreModifier.java
package domain.modifiers;

import domain.character.Character;
import domain.types.Ability;
import domain.utils.StringUtils;

/**
 * An Ability score increase or decrease provided to a {@link Character}.
 * Immutable once created.
 */
public class AbilityScoreModifier {

    // --- Attributes ---
    private final Ability ability;
    private final int value;

    // Constructor
    public AbilityScoreModifier(Ability ability, int value) {
        this.ability = ability;
        this.value = value;
    }

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
