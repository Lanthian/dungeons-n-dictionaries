// src/domain/modifiers/Feat.java
package domain.modifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import domain.builders.DescribedBuilder;
import domain.character.CharacterModifier;
import domain.core.Described;
import domain.modifiers.choice.Choice;
import domain.modifiers.choice.ChoiceProvider;
import domain.modifiers.proficiency.Proficiency;
import domain.utils.StringUtils;

/**
 * Implementation of {@link Described} for a Feat a Character posseses.
 * Can supply a character with AbilityScoreModifiers and Proficiencies.
 */
public class Feat extends Described<Feat> implements CharacterModifier, ChoiceProvider {

    // --- Attributes ---
    private final List<AbilityScoreModifier> abilityScoreModifiers;
    private final List<Proficiency> proficiencies;
    private final List<Choice> choices;

    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for eased {@link Feat} construction.
     * Create a new {@code Builder} object, call the relevant construction 
     * methods upon it, then finalise the process with the {@link #build()} 
     * method.
     */
    public static class Builder extends DescribedBuilder<Feat> {

        // --- Attributes ---
        private List<AbilityScoreModifier> abilityScoreModifiers;
        private List<Proficiency> proficiencies;
        private List<Choice> choices;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            super(name, description);
            this.abilityScoreModifiers = new ArrayList<>();
            this.proficiencies = new ArrayList<>();
            this.choices = new ArrayList<>();
        }

        // Build method
        @Override
        public Feat build() { return new Feat(this); }

        /* ---------------------- Foreign Associations ---------------------- */
        
        /** 
         * Replaces current {@link AbilityScoreModifier}s with provided list.
         */
        public Builder abilityScoreModifiers(List<AbilityScoreModifier> abilityScoreModifiers) {
            this.abilityScoreModifiers = abilityScoreModifiers; return this;
        }

        /** 
         * Replaces current {@link Proficiency}s with provided list parameter.
         */
        public Builder proficiencies(List<Proficiency> proficiencies) {
            this.proficiencies = proficiencies; return this;
        }

        /* --------------------------- Dependants --------------------------- */

        /** 
         * Appends all {@link Choice}s from provided parameter to current list.
         */
        public Builder choices(List<Choice> choices) {
            this.choices.addAll(choices); return this;
        }

        /** 
         * Appends a {@link Choice} to current list.
         */
        public Builder choice(Choice choice) {
            this.choices.add(choice); return this;
        }
    }

    /* ---------------------------- Constructors ---------------------------- */

    // Local Constructor
    private Feat(Builder builder)  {
        // name & description copied from DescribedBuilder
        super(builder.getName(), builder.getDescription());

        this.abilityScoreModifiers = builder.abilityScoreModifiers; 
        this.proficiencies = builder.proficiencies; 
        this.choices = builder.choices; 
    }

    /**
     * Publicly assessable constructor for simple Feats that do not require
     * {@link Feat.Builder} construction or provide {@link CharacterModifier}s. 
     * Description attribute must not be null or blank.
     * 
     * @param name Name of the Feat
     * @param description Description of the Feat - what it does
     */
    public Feat(String name, String description) {
        // Validate that description is both not null and also not blank
        super(name, Objects.requireNonNull(description, 
            "Constructed Feat must present a non-null description"));
        if (description.isBlank()) {
            throw new IllegalArgumentException("Constructed Feat must present a non-blank description");
        }

        this.abilityScoreModifiers = new ArrayList<>();
        this.proficiencies = new ArrayList<>();
        this.choices = new ArrayList<>();
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Proficiency> getProficiencies() { return List.copyOf(this.proficiencies); }

    @Override
    public List<AbilityScoreModifier> getAbilityScoreModifiers() { return List.copyOf(this.abilityScoreModifiers); }

    /* ======================================================================
     * ------------------- ChoiceProvider  Implementation ------------------- 
     * ====================================================================== */

    @Override
    public List<Choice> getChoices() { return List.copyOf(this.choices); }

    /* ======================================================================
     * --------------------------- Object Methods --------------------------- 
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("Feat")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("abilityScoreModifiers=" + abilityScoreModifiers)
            .add("proficiences=" + proficiencies)
            .add("choices=" + choices)
            .toString();
    }
}
