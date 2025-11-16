// src/domain/CharacterModifier.java
package domain;

import java.util.Collections;
import java.util.List;

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
    default List<Language> getLanguages() { return Collections.emptyList(); }

    /**
     * Getter for a CharacterModifier's supplied {@link Proficiency}s.
     * 
     * @return A potentially empty {@code List} if implemented, 
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<Proficiency> getProficiencies() { return Collections.emptyList(); }

    /**
     * Getter for a CharacterModifier's supplied {@link Feat}s.
     * 
     * @return A potentially empty {@code List} if implemented, 
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<Feat> getFeats() { return Collections.emptyList(); }
    
    /**
     * Getter for a CharacterModifier's supplied {@link AbilityScoreModifier}s.
     * 
     * @return A potentially empty {@code List} if implemented, 
     *         {@code Collections.emptyList()} if unimplemented.
     */
    default List<AbilityScoreModifier> getAbilityScoreModifiers() { return Collections.emptyList(); }
}
