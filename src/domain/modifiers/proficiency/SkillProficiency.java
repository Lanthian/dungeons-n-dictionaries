// src/domain/modifiers/proficiency/SkillProficiency.java
package domain.modifiers.proficiency;

import domain.character.Character;
import domain.types.Skill;
import domain.utils.StringUtils;

/**
 * A {@link Proficiency} providing a {@link Character} with expertise regarding
 * a {@link Skill}. 
 */
public class SkillProficiency implements Proficiency {
    
    // --- Attributes ---
    private final Skill skill;

    // Constructor
    public SkillProficiency(Skill skill) {
        this.skill = skill;
    }

    // TODO: Move this business logic to character level handling
    /**
     * Utility method to retrieve the increase a skill proficiency provides. 
     * Formula derived from: 
     * {@link https://roll20.net/compendium/dnd5e/Character%20Advancement}.
     * 
     * @param level {@link Character} total level (summed multiclass levels)
     * @return integer increase to skill roll
     */
    public static int proficiencyBonus(int level) {
        return 2 + ((-1 + level) / 4);
    }

    // --- Getter ---
    public Skill getSkill() { return this.skill; }

    // To String
    @Override
    public String toString() {
        return StringUtils.toStringJoiner("SkillProficiency")
            .add("skill=" + skill)
            .toString();
    }
}
