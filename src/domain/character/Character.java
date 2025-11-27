// src/domain/Character.java
package domain.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import domain.builders.AbstractBuilder;
import domain.modifiers.choice.Choice;
import domain.templates.Background;
import domain.templates.ClassTemplate;
import domain.templates.Race;
import domain.types.Ability;
import domain.types.Alignment;

/**
 * The instantiation and accumulation of all D&D character required information.
 * Constructed by utilising internal {@link Character.Builder} class.
 */
public class Character {

    // --- Attributes ---
    // Simple attributes
    private String name;
    private int level;
    private int experience;
    private String playerName;
    private Alignment alignment;
    // Visual descriptors
    private CharacterPhysique physique;
    // Additional details
    private CharacterProfile profile;
    // Foreign associations
    private CharacterSelection<Race> raceSelection;
    private CharacterSelection<Background> backgroundSelection;
    private List<CharacterSelection<ClassTemplate>> classSelections;
    private AbilityScores abilityScores;
    
    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for easy {@link Character} construction.
     * Create a new {@code Builder} object, call the relevant construction 
     * methods upon it, then finalise the process with the {@link #build()} 
     * method.
     */
    public static class Builder extends AbstractBuilder<Character> {

        // --- Constants ---
        private static final int STARTING_LEVEL = 1;
        private static final int STARTING_XP = 0;

        // --- Attributes ---
        // Simple attributes
        private String name;
        private int level;
        private int experience;
        private String playerName;
        private Alignment alignment;
        // Visual descriptors & Additional details
        private CharacterPhysique physique;
        private CharacterProfile profile;
        // Foreign associations
        private CharacterSelection<Race> raceSelection;
        private CharacterSelection<Background> backgroundSelection;
        private List<CharacterSelection<ClassTemplate>> classSelections;
        private AbilityScores abilityScores;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name) {
            this.name = name;

            // Defaults
            this.level = STARTING_LEVEL;
            this.experience = STARTING_XP;
            this.playerName = UNDEFINED;
            this.alignment = null;

            this.physique = null;
            this.profile = null;

            this.raceSelection = null;
            this.backgroundSelection = null;
            this.classSelections = new ArrayList<>();
            this.abilityScores = new AbilityScores();
        }

        // Build method
        public Character build() { return new Character(this); }

        /* ----------------------- Simple  Attributes ----------------------- */

        public Builder level(int level) { this.level = level; return this; }
        public Builder experience(int experience) { this.experience = experience; return this; }
        public Builder playerName(String playerName) { this.playerName = playerName; return this; }
        public Builder alignment(Alignment alignment) { this.alignment = alignment; return this; }
        public Builder alignment(String alignment) { 
            this.alignment = Alignment.fromString(alignment); return this; 
        }

        // Visual descriptors
        public Builder physique(CharacterPhysique physique) { this.physique = physique; return this; }
        public Builder physique(Consumer<CharacterPhysique.Builder> consumer) {
            this.physique = buildWith(CharacterPhysique.Builder::new, consumer);
            return this;
        }

        // Additional details
        public Builder profile(CharacterProfile profile) { this.profile = profile; return this; }
        public Builder profile(Consumer<CharacterProfile.Builder> consumer) {
            this.profile = buildWith(CharacterProfile.Builder::new, consumer);
            return this;
        }

        /* ---------------------- Foreign Associations ---------------------- */

        /** 
         * Replaces current {@link Race} {@link CharacterSelection} 
         * with provided parameter.
         */
        public Builder raceSelection(CharacterSelection<Race> raceSelection) {
            this.raceSelection = raceSelection; return this;
        }

        /** 
         * Replaces current {@link Background} {@link CharacterSelection} 
         * with provided parameter.
         */
        public Builder backgroundSelection(CharacterSelection<Background> backgroundSelection) {
            this.backgroundSelection = backgroundSelection; return this;
        }

        /** 
         * Appends all {@link ClassTemplate} {@link CharacterSelection}s from 
         * provided {@link List} parameter to current list.
         */
        public Builder classSelections(List<CharacterSelection<ClassTemplate>> classSelections) {
            this.classSelections.addAll(classSelections); return this;
        }

        /** 
         * Appends a provided {@link ClassTemplate} {@link CharacterSelection} 
         * to current classes list.
         */
        public Builder classSelection(CharacterSelection<ClassTemplate> classSelection) {
            this.classSelections.add(classSelection); return this;
        }

        /* --------------------------- Dependants --------------------------- */

        public Builder abilityScores(AbilityScores scores) { 
            this.abilityScores = scores; return this; 
        }

        // Overloaded Method (List<Integer> input)
        public Builder abilityScores(List<Integer> scores) { 
            this.abilityScores = new AbilityScores(scores); return this; 
        }
    }

    // Local Constructor
    private Character(Builder builder) {
        this.name = builder.name;
        // TODO: Add verification that level == sum of classes levels
        this.level = builder.level;
        // TODO: Add verification that XP matches level threshold
        this.experience = builder.experience;
        this.playerName = builder.playerName;
        this.alignment = builder.alignment;

        this.physique = builder.physique;
        this.profile = builder.profile;

        // TODO: Add verification that these are selected before building
        this.raceSelection = builder.raceSelection;
        this.backgroundSelection = builder.backgroundSelection;
        this.classSelections = builder.classSelections;

        this.abilityScores = builder.abilityScores;
    }

    /* ======================================================================
     * ----------------------------- Mutations  ----------------------------- 
     * ====================================================================== */

    public List<Choice> levelUp() {
        throw new Error("Unimplemented");
    }

    public void addExperience(int experience) { this.experience += experience; }

    public List<Choice> multiclass(CharacterSelection<ClassTemplate> multiclass) {
        throw new Error("Unimplemented");
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */
    
    /* ------------------------------ Getters  ------------------------------ */

    // Simple attributes
    public String getName() { return this.name; }
    public int getLevel() { return this.level; }
    public int getExperience() { return this.experience; }
    public String getPlayerName() { return this.playerName; }
    public Alignment getAlignment() { return this.alignment; }

    // Visual descriptors & Additional details
    public CharacterPhysique getPhysique() { return this.physique; }
    public CharacterProfile getProfile() { return this.profile; }

    public Map<Ability, Integer> getAbilities(Ability ability) {
        Map<Ability, Integer> flat = Map.copyOf(this.abilityScores.getScores());
        // TODO: Add ASMs found throughout attached CharacterModifiers
        return flat;
    }

    public int getAbility(Ability ability) {
        int flat = this.abilityScores.getScore(ability);
        // TODO: Add ASMs found throughout attached CharacterModifiers
        return flat;
    }

    /* ------------------------------ Setters  ------------------------------ */

    // Simple attributes
    public void setName(String name) { this.name = name; }
    public void setLevel(int level) { this.level = level; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setAlignment(Alignment alignment) { this.alignment = alignment; }
    public void setAlignment(String alignment) { this.alignment = Alignment.fromString(alignment); }

    // Visual descriptors & Additional details
    // TODO: WARNING: Overwrite existing info
    public void setPhysique(CharacterPhysique physique) { this.physique = physique; }
    public void setProfile(CharacterProfile profile) { this.profile = profile; }
}
 