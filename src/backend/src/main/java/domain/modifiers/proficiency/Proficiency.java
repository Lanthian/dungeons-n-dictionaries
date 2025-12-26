// java/domain/modifiers/proficiency/Proficiency.java
package domain.modifiers.proficiency;

import domain.character.CharacterModification;
import domain.core.Entity;
import domain.types.ModificationType;
import domain.types.ProficiencyType;

/**
 * An abstract class to group different kinds of proficiencies together.
 *
 * <p> Requires the implementation of a {@link ProficiencyType} distinguishing
 * "{@link #type()}" method.
 */
public abstract class Proficiency extends Entity<Proficiency> implements CharacterModification {

    /**
     * Subclasses must define what type of proficiency they offer.
     *
     * @return {@link ProficiencyType} enum determining kind of proficiency
     */
    public abstract ProficiencyType type();

    /* ======================================================================
     * ---------------- CharacterModification Implementation ----------------
     * ====================================================================== */

    @Override
    public ModificationType modType() { return ModificationType.PROFICIENCY; }
}
