// src/domain/templates/Race.java
package domain.templates;

import java.util.ArrayList;
import java.util.List;

import domain.builders.DetailedBuilder;
import domain.character.Character;
import domain.character.CharacterModifier;
import domain.core.Detailed;
import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.Language;
import domain.modifiers.choice.Choice;
import domain.modifiers.choice.ChoiceProvider;
import domain.modifiers.proficiency.Proficiency;
import domain.utils.StringUtils;

/**
 * The attributes a {@link Character} Race has and provides in D&D.
 */
public class Race extends Detailed<Race> implements CharacterModifier, ChoiceProvider {
    
    // --- Attributes ---
    // Simple attributes
    private final String age;
    private final String alignment;
    private final String size;
    private final int speed;
    // Foreign associations
    // TODO: Only one of the below attributes regarding subrace is necessary
    private final Race parentRace;
    private final List<Race> subraces;
    private final List<Language> languages;
    private final List<Proficiency> proficiencies;
    private final List<Feat> feats;
    private final List<AbilityScoreModifier> abilityScoreModifiers;
    private final List<Choice> choices;
    
    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for eased {@link Race} construction.
     * Create a new {@code Builder} object, call the relevant
     * construction methods upon it, then finalise the process with the 
     * {@link #build()} method.
     */
    public static class Builder extends DetailedBuilder<Race> {

        // --- Constants ---
        private final static int DEFAULT_SPEED = 30;

        // --- Attributes ---
        // Simple attributes
        private String age;
        private String alignment;
        private String size;
        private int speed;
        // Foreign associations
        private Race parentRace;
        private List<Race> subraces;
        private List<Language> languages;
        private List<Proficiency> proficiencies;
        private List<Feat> feats;
        private List<AbilityScoreModifier> abilityScoreModifiers;
        private List<Choice> choices;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            super(name, description);
            
            // Defaults
            this.age = UNDEFINED;
            this.alignment = UNDEFINED;
            this.size = UNDEFINED;
            this.speed = DEFAULT_SPEED;
            this.parentRace = null;
            this.subraces = new ArrayList<>();
            this.languages = new ArrayList<>();
            this.proficiencies = new ArrayList<>();
            this.feats = new ArrayList<>();
            this.abilityScoreModifiers = new ArrayList<>();
            this.choices = new ArrayList<>();
        }

        // Build method
        @Override
        public Race build() { return new Race(this); }

        /* ----------------------- Simple  Attributes ----------------------- */

        public Builder age(String age) { this.age = age; return this; }
        public Builder alignment(String alignment) { this.alignment = alignment; return this; }
        public Builder size(String size) { this.size = size; return this; }

        public Builder speed(int speed) { 
            if (speed < 0) throw new IllegalArgumentException("Speed cannot be negative");
            this.speed = speed; return this;
        }

        /* ---------------------- Foreign Associations ---------------------- */

        /** 
         * Replaces current {@link Race} parentRace with provided parameter.
         */
        public Builder parentRace(Race parentRace) {
            this.parentRace = parentRace; return this;
        }
        
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

        /** 
         * Replaces current {@link Feat}s with provided list parameter.
         */
        public Builder feats(List<Feat> feats) {
            this.feats = feats; return this;
        }

        /** 
         * Replaces current {@link AbilityScoreModifier}s with provided list 
         * parameter.
         */
        public Builder abilityScoreModifiers(List<AbilityScoreModifier> abilityScoreModifiers) {
            this.abilityScoreModifiers = abilityScoreModifiers; return this;
        }

        /* --------------------------- Dependants --------------------------- */

        /** 
         * Appends all {@link Race} subraces from provided parameter to list.
         */
        public Builder subraces(List<Race> subraces) {
            this.subraces.addAll(subraces); return this;
        }

        /** 
         * Appends a {@link Race} subrace to current list.
         */
        public Builder subrace(Race subrace) {
            this.subraces.add(subrace); return this;
        }

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
    private Race(Builder builder) {
        // name, description & details copied from DetailedBuilder
        super(builder.getName(), builder.getDescription());
        this.setDetails(builder.getDetails());
        
        this.age = builder.age;
        this.alignment = builder.alignment;
        this.size = builder.size;
        this.speed = builder.speed;

        this.parentRace = builder.parentRace;
        this.subraces = builder.subraces;
        this.languages = builder.languages;
        this.proficiencies = builder.proficiencies;
        this.feats = builder.feats;
        this.abilityScoreModifiers = builder.abilityScoreModifiers;
        this.choices = builder.choices;
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Language> getLanguages() { return List.copyOf(this.languages); }
    
    @Override
    public List<Proficiency> getProficiencies() { return List.copyOf(this.proficiencies); }

    @Override
    public List<Feat> getFeats() { return List.copyOf(this.feats); }

    @Override
    public List<AbilityScoreModifier> getAbilityScoreModifiers() { return List.copyOf(this.abilityScoreModifiers); }

    /* ======================================================================
     * ------------------- ChoiceProvider  Implementation ------------------- 
     * ====================================================================== */

    @Override
    public List<Choice> getChoices() { return List.copyOf(this.choices); }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getAge() { return this.age; }
    public String getAlignment() { return this.alignment; }
    public String getSize() { return this.size; }
    public int getSpeed() { return this.speed; }
    public Race getParentRace() { return this.parentRace; }
    public List<Race> getSubraces() { return this.subraces; }

    // --- Setters ---
    // public void setAge(String val) { this.age = val; }
    // public void setAlignment(String val) { this.alignment = val; }
    // public void setSize(String val) { this.size = val; }
    // public void setSpeed(int val) { this.speed = val; }
    // public void setParentRace(Race val) { this.parentRace = val; }

    /* ======================================================================
     * --------------------------- Object Methods --------------------------- 
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("Race")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("details=" + details)
            .add("age=" + StringUtils.quote(age))
            .add("alignment=" + StringUtils.quote(alignment))
            .add("size=" + StringUtils.quote(size))
            .add("speed=" + speed)
            // TODO: Only one of the below (parentRace / subraces) should be kept
            .add("parentRace=" + parentRace)
            .add("subraces=" + subraces)
            .add("proficiencies=" + proficiencies)
            .add("languages=" + languages)
            .add("feats=" + feats)
            .add("abilityScoreModifiers=" + abilityScoreModifiers)
            .add("choices=" + choices)
            .toString();
    }
}
