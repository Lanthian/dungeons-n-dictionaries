// src/domain/Race.java
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The attributes a {@link Character} Race has and provides in D&D.
 */
public class Race extends Detailed implements CharacterModifier {
    
    // --- Attributes ---
    // Simple attributes
    private final String age;
    private final String alignment;
    private final String size;
    private final int speed;
    // Foreifinal gn associations
    private final Race parentRace;
    private final List<Language> languages;
    private final List<Proficiency> proficiencies;
    private final List<Feat> feats;
    private final List<AbilityScoreModifier> abilityScoreModifiers;
    
    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for eased {@link Race} construction.
     * Create a new {@code Builder} object, call the relevant
     * construction methods upon it, then finalise the process with the 
     * {@link #build()} method.
     */
    public static class Builder extends Detailed {

        // --- Constants ---
        private final static String UNDEFINED = null;
        private final static int DEFAULT_SPEED = 30;

        // --- Attributes ---
        // Simple attributes
        private String age;
        private String alignment;
        private String size;
        private int speed;
        // Foreign associations
        private Race parentRace;
        private List<Language> languages;
        private List<Proficiency> proficiencies;
        private List<Feat> feats;
        private List<AbilityScoreModifier> abilityScoreModifiers;

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
            this.languages = new ArrayList<>();
            this.proficiencies = new ArrayList<>();
            this.feats = new ArrayList<>();
            this.abilityScoreModifiers = new ArrayList<>();
        }

        // Build method
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
         * Appends all {@link Detail}s from provided parameter to current list.
         */
        public Builder details(List<Detail> details) {
            this.setDetails(details); return this;
        }

        /** 
         * Appends a {@link Detail} to current list.
         */
        public Builder detail(Detail detail) {
            this.addDetail(detail); return this;
        }
    }

    // Local Constructor
    private Race(Builder builder) {
        // name, description & details copied from Detailed builder
        super(builder);
        
        this.age = builder.age;
        this.alignment = builder.alignment;
        this.size = builder.size;
        this.speed = builder.speed;

        this.parentRace = builder.parentRace;
        this.languages = builder.languages;
        this.proficiencies = builder.proficiencies;
        this.feats = builder.feats;
        this.abilityScoreModifiers = builder.abilityScoreModifiers;
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Language> getLanguages() { return this.languages; }
    
    @Override
    public List<Proficiency> getProficiencies() { return this.proficiencies; }

    @Override
    public List<Feat> getFeats() { return this.feats; }

    @Override
    public List<AbilityScoreModifier> getAbilityScoreModifiers() { return this.abilityScoreModifiers; }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getAge() { return this.age; }
    public String getAlignment() { return this.alignment; }
    public String getSize() { return this.size; }
    public int getSpeed() { return this.speed; }
    public Race getParentRace() { return this.parentRace; }

    // --- Setters ---
    // public void setAge(String val) { this.age = val; }
    // public void setAlignment(String val) { this.alignment = val; }
    // public void setSize(String val) { this.size = val; }
    // public void setSpeed(int val) { this.speed = val; }
    // public void setParentRace(Race val) { this.parentRace = val; }
}
