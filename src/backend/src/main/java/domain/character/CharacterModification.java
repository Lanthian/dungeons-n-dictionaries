// java/domain/character/CharacterModification.java
package domain.character;

import domain.types.ModificationType;

/**
 * A contract components implement in order to be declared information that can
 * be supplied by {@link CharacterModifier}s.
 *
 * <p> Requires the implementation of a {@link ModificationType} distinguishing
 * "{@link #mod()}" method.
 */
public interface CharacterModification {

    /**
     * Implementing classes must define what type of modification they are.
     *
     * @return {@link ModificationType} enum determining kind of modification
     */
    ModificationType modType();
}
