// java/domain/character/CharacterModifier.java
package domain.character;

import java.util.Collections;
import java.util.List;

import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.Language;
import domain.modifiers.proficiency.Proficiency;

/**
 * A contract {@link Character} components implement in order to be suppliers
 * of information. A class implementing this interface does not need to
 * implement all methods - only those that are relevant.
 */
public interface CharacterModifier {

    /* ======================================================================
     * ------------------------------ Getters  ------------------------------
     * ====================================================================== */

    /**
     * Getter for a CharacterModifier's supplied {@link Language}s.
     *
     * @return A potentially empty {@code List} if implemented,
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<? extends Language> getLanguages() { return Collections.emptyList(); }

    /**
     * Getter for a CharacterModifier's supplied {@link Proficiency}s.
     *
     * @return A potentially empty {@code List} if implemented,
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<? extends Proficiency> getProficiencies() { return Collections.emptyList(); }

    /**
     * Getter for a CharacterModifier's supplied {@link Feat}s.
     *
     * @return A potentially empty {@code List} if implemented,
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<? extends Feat> getFeats() { return Collections.emptyList(); }

    /**
     * Getter for a CharacterModifier's supplied {@link AbilityScoreModifier}s.
     *
     * @return A potentially empty {@code List} if implemented,
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<? extends AbilityScoreModifier> getAbilityScoreModifiers() { return Collections.emptyList(); }
}
