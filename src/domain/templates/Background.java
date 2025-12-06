// src/domain/templates/Background.java
package domain.templates;

import java.util.ArrayList;
import java.util.List;

import domain.builders.DetailedBuilder;
import domain.character.Character;
import domain.character.CharacterModifier;
import domain.core.Detailed;
import domain.modifiers.Language;
import domain.modifiers.choice.Choice;
import domain.modifiers.choice.ChoiceProvider;
import domain.modifiers.proficiency.Proficiency;
import domain.utils.StringUtils;

/**
 * The attributes a {@link Character} Background has and provides in D&D.
 * TODO: Implement Choice<> mechanism from #2 for proficiencies and languages
 * TODO: Implement background variants (choice?)
 */
public class Background extends Detailed<Background> implements CharacterModifier, ChoiceProvider {

    // --- Attributes ---
    // private final Background parentBackground;
    private final List<Language> languages;
    private final List<Proficiency> proficiencies;
    private final List<Choice> choices;

    /* ======================================================================
     * -------------------------- Builder  Pattern --------------------------
     * ====================================================================== */

    /**
     * Builder pattern implementation for eased {@link Background} construction.
     * Create a new {@code Builder} object, call the relevant construction 
     * methods upon it, then finalise the process with the {@link #build()} 
     * method.
     */
    public static class Builder extends DetailedBuilder<Background> {

        // --- Attributes ---
        private List<Language> languages;
        private List<Proficiency> proficiencies;
        private List<Choice> choices;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            super(name, description);
            this.languages = new ArrayList<>();
            this.proficiencies = new ArrayList<>();
            this.choices = new ArrayList<>();
        }

        // Build method
        @Override
        public Background build() { return new Background(this); }

        /* ---------------------- Foreign Associations ---------------------- */
        
        /** 
         * Replaces current {@link Language}s with provided list parameter.
         */
        public Builder languages(List<Language> languages) {
            this.languages = languages; return this;
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

    // Local Constructor
    private Background(Builder builder) {
        // name, description & details copied from DetailedBuilder
        super(builder.getName(), builder.getDescription());
        this.setDetails(builder.getDetails());
        
        this.languages = builder.languages;
        this.proficiencies = builder.proficiencies;
        this.choices = builder.choices;
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------
     * ====================================================================== */

    @Override
    public List<Language> getLanguages() { return List.copyOf(this.languages); }
    
    @Override
    public List<Proficiency> getProficiencies() { return List.copyOf(this.proficiencies); }

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
        return StringUtils.toStringJoiner("Background")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("details=" + details)
            .add("languages=" + languages)
            .add("proficiences=" + proficiencies)
            .add("choices=" + choices)
            .toString();
    }
}
