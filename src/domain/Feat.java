// src/domain/Feat.java
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link Described} for a Feat a Character posseses.
 * Can supply a character with AbilityScoreModifiers and Proficiencies.
 */
public class Feat extends Described implements CharacterModifier {

    // --- Attributes ---
    private ArrayList<AbilityScoreModifier> abilityScoreModifiers;
    private ArrayList<Proficiency> proficiencies;

    // Constructor
    public Feat(String name, String description) {
        super(name, description);
        this.abilityScoreModifiers = new ArrayList<>();
        this.proficiencies = new ArrayList<>();
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Proficiency> getProficiencies() { return List.copyOf(this.proficiencies); }

    @Override
    public List<AbilityScoreModifier> getAbilityScoreModifiers() { return List.copyOf(this.abilityScoreModifiers); }
}
