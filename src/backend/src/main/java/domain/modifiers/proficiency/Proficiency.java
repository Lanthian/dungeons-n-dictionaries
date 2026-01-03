// java/domain/modifiers/proficiency/Proficiency.java
package domain.modifiers.proficiency;

import domain.character.CharacterModification;
import domain.core.Entity;
import domain.types.ModificationType;
import domain.types.ProficiencyType;

/**
 * An abstract class to group different kinds of proficiencies together.
 *
 * <p> Subclass constructors must define which {@link ProficiencyType} they
 * realise.
 */
public abstract class Proficiency extends Entity<Proficiency> implements CharacterModification {

    // --- Attributes ---
    // Subclasses must define what type of proficiency they offer
    private final ProficiencyType proficiencyType;

    // Abstract Constructor
    protected Proficiency(ProficiencyType type) { this.proficiencyType = type; }

    // --- Getter ---
    public ProficiencyType getProficiencyType() { return this.proficiencyType; }

    /* ======================================================================
     * ---------------- CharacterModification Implementation ----------------
     * ====================================================================== */

    @Override
    public ModificationType modType() { return ModificationType.PROFICIENCY; }
}
