// java/domain/modifiers/proficiency/Proficiency.java
package domain.modifiers.proficiency;

import domain.character.Character;
import domain.core.Entity;
import domain.types.ProficiencyType;

/**
 * An interface to group multiple {@link Character} proficiencies together.
 * Does not require any methods to be implemented.
 */
public abstract class Proficiency extends Entity<Proficiency> {

    /**
     * Subclasses must define what type of proficiency they offer.
     *
     * @return {@link ProficiencyType} enum determining kind of proficiency
     */
    public abstract ProficiencyType type();
}
