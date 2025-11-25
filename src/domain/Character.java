// src/domain/Character.java
package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String eyes;
    private String skin;
    private String hair;
    // Additional details
    private String backstory;
    private String personalityTraits;
    private String ideals;
    private String bonds;
    private String flaws;
    private String allies;
    private String deity;
    private String trinket;
    private String notes;
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
    public static class Builder {

        // --- Constants ---
        private final static String UNDEFINED = null;
        private static final int STARTING_LEVEL = 1;
        private static final int STARTING_XP = 0;

        // --- Attributes ---
        // Simple attributes
        private String name;
        private int level;
        private int experience;
        private String playerName;
        private Alignment alignment;
        // Visual descriptors
        private String gender;
        private String age;
        private String height;
        private String weight;
        private String eyes;
        private String skin;
        private String hair;
        // Additional details
        private String backstory;
        private String personalityTraits;
        private String ideals;
        private String bonds;
        private String flaws;
        private String allies;
        private String deity;
        private String trinket;
        private String notes;
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

            this.gender = UNDEFINED;
            this.age = UNDEFINED;
            this.height = UNDEFINED;
            this.weight = UNDEFINED;
            this.eyes = UNDEFINED;
            this.skin = UNDEFINED;
            this.hair = UNDEFINED;

            this.backstory = UNDEFINED;
            this.personalityTraits = UNDEFINED;
            this.ideals = UNDEFINED;
            this.bonds = UNDEFINED;
            this.flaws = UNDEFINED;
            this.allies = UNDEFINED;
            this.deity = UNDEFINED;
            this.trinket = UNDEFINED;
            this.notes = UNDEFINED;

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
        
        // Overloaded Method (String input)
        public Builder alignment(String alignment) { 
            this.alignment = Alignment.fromString(alignment); return this; 
        }

        // Visual descriptors
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder age(String age) { this.age = age; return this; }
        public Builder height(String height) { this.height = height; return this; }
        public Builder weight(String weight) { this.weight = weight; return this; }
        public Builder eyes(String eyes) { this.eyes = eyes; return this; }
        public Builder skin(String skin) { this.skin = skin; return this; }
        public Builder hair(String hair) { this.hair = hair; return this; }

        // Additional details
        public Builder backstory(String backstory) { this.backstory = backstory; return this; }
        public Builder personalityTraits(String personalityTraits) { this.personalityTraits = personalityTraits; return this; }
        public Builder ideals(String ideals) { this.ideals = ideals; return this; }
        public Builder bonds(String bonds) { this.bonds = bonds; return this; }
        public Builder flaws(String flaws) { this.flaws = flaws; return this; }
        public Builder allies(String allies) { this.allies = allies; return this; }
        public Builder deity(String deity) { this.deity = deity; return this; }
        public Builder trinket(String trinket) { this.trinket = trinket; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

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

        this.gender = builder.gender;
        this.age = builder.age;
        this.height = builder.height;
        this.weight = builder.weight;
        this.eyes = builder.eyes;
        this.skin = builder.skin;
        this.hair = builder.hair;

        this.backstory = builder.backstory;
        this.personalityTraits = builder.personalityTraits;
        this.ideals = builder.ideals;
        this.bonds = builder.bonds;
        this.flaws = builder.flaws;
        this.allies = builder.allies;
        this.deity = builder.deity;
        this.trinket = builder.trinket;
        this.notes = builder.notes;

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

    // Visual descriptors
    public String getGender() { return this.gender; }
    public String getAge() { return this.age; }
    public String getHeight() { return this.height; }
    public String getWeight() { return this.weight; }
    public String getEyes() { return this.eyes; }
    public String getSkin() { return this.skin; }
    public String getHair() { return this.hair; }

    // Additional details
    public String getBackstory() { return this.backstory; }
    public String getPersonalityTraits() { return this.personalityTraits; }
    public String getIdeals() { return this.ideals; }
    public String getBonds() { return this.bonds; }
    public String getFlaws() { return this.flaws; }
    public String getAllies() { return this.allies; }
    public String getDeity() { return this.deity; }
    public String getTrinket() { return this.trinket; }
    public String getNotes() { return this.notes; }

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

    // Visual descriptors
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(String age) { this.age = age; }
    public void setHeight(String height) { this.height = height; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setEyes(String eyes) { this.eyes = eyes; }
    public void setSkin(String skin) { this.skin = skin; }
    public void setHair(String hair) { this.hair = hair; }

    // Additional details
    public void setBackstory(String backstory) { this.backstory = backstory; }
    public void setPersonalityTraits(String personalityTraits) { this.personalityTraits = personalityTraits; }
    public void setIdeals(String ideals) { this.ideals = ideals; }
    public void setBonds(String bonds) { this.bonds = bonds; }
    public void setFlaws(String flaws) { this.flaws = flaws; }
    public void setAllies(String allies) { this.allies = allies; }
    public void setDeity(String deity) { this.deity = deity; }
    public void setTrinket(String trinket) { this.trinket = trinket; }
    public void setNotes(String notes) { this.notes = notes; }
}
 