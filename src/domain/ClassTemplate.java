// src/domain/ClassTemplate.java
package domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The attributes a {@link Character} Class has in D&D. 
 * Named `CharacterTemplate` to avoid clashing Java keyword `Class`.
 */
public class ClassTemplate extends Detailed implements CharacterModifier {
    
    // --- Attributes ---
    // Simple attributes
    private final String hitPoints;
    private final String hitDice;
    // Foreign associations
    private final ClassTemplate parentClassTemplate;
    private final Map<Integer, LevelReward> levelRewards;
    
    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for {@link ClassTemplate} construction.
     * Create a new {@code Builder} object, call the relevant
     * construction methods upon it, then finalise the process with the 
     * {@link #build()} method.
     */
    public static class Builder extends Detailed {

        // --- Constants ---
        private final static String UNDEFINED = null;

        // --- Attributes ---
        // Simple attributes
        private String hitPoints;
        private String hitDice;
        // Foreign associations
        private ClassTemplate parentClassTemplate;
        private Map<Integer, LevelReward> levelRewards;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            super(name, description);
            
            // Defaults
            this.hitPoints = UNDEFINED;
            this.hitDice = UNDEFINED;
            this.parentClassTemplate = null;
            this.levelRewards = new HashMap<>();
        }

        // Build method
        public ClassTemplate build() { return new ClassTemplate(this); }

        /* ----------------------- Simple  Attributes ----------------------- */

        public Builder hitPoints(String hitPoints) { this.hitPoints = hitPoints; return this; }
        public Builder hitDice(String hitDice) { this.hitDice = hitDice; return this; }

        /* ---------------------- Foreign Associations ---------------------- */

        /** 
         * Replaces current {@link Race} parentRace with provided parameter.
         */
        public Builder parentClassTemplate(ClassTemplate parentClassTemplate) {
            this.parentClassTemplate = parentClassTemplate; return this;
        }

        /**
         * Inserts all {@link LevelReward}s from provided parameter into curent
         * map. Overwrites duplicate keys.
         */
        public Builder levelRewards(Map<Integer, LevelReward> levelRewards) {
            this.levelRewards.putAll(levelRewards); return this;
        }
        
        /** 
         * Inserts a {@link LevelReward} into current reward map. Overwrites 
         * duplicate keys.
         */
        public Builder levelReward(LevelReward levelReward) {
            this.levelRewards.put(levelReward.getLevel(), levelReward); return this;
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
    private ClassTemplate(Builder builder) {
        // name, description & details copied from Detailed builder
        super(builder);
        
        this.hitPoints = builder.hitPoints;
        this.hitDice = builder.hitDice;

        this.parentClassTemplate = builder.parentClassTemplate;
        this.levelRewards = builder.levelRewards;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getHitPoints() { return this.hitPoints; }
    public String getHitDice() { return this.hitDice; }
    public ClassTemplate getParentClassTemplate() { return this.parentClassTemplate; }

    /**
     * Returns all {@link LevelReward}s including those supplied by parent 
     * class. Sorted from lowest to highest level.
     */
    public List<LevelReward> getAllLevelRewards() {
        return Stream.concat(
            levelRewards.values().stream(),
            // Parent class rewards if parent present
            this.parentClassTemplate != null
                ? this.parentClassTemplate.getAllLevelRewards().stream()
                : Stream.empty()
        ).sorted().toList();
    }

    /**
     * Returns all {@link LevelReward}s lower than provided paramater, including
     * those supplied by parent class. Sorted from lowest to highest level.
     * 
     * @param level (Inclusive) level cap for level rewards returned
     * @return Sorted list of level rewards below level param
     */
    public List<LevelReward> getLesserLevelRewards(int level) {
        return Stream.concat(
            // Current class rewards filtered by level
            levelRewards.values().stream()
                .filter(reward -> reward.getLevel() <= level),
            // Filtered parent class rewards if parent present
            this.parentClassTemplate != null
                ? this.parentClassTemplate.getLesserLevelRewards(level).stream()
                : Stream.empty()
        ).sorted().toList();
    }

    /**
     * Returns a list of {@link LevelReward}s supplied by this class and parent 
     * class pathway at a particular queried level.
     * 
     * @param level Level at which level rewards are queried
     * @return Sorted list of level rewards at level param
     */
    public List<LevelReward> getLevelReward(int level) {
        return Stream.concat(
            // Level reward supplied by this class at provided level
            Stream.of(levelRewards.get(level)),
            // Parent class reward if parent present
            this.parentClassTemplate != null
                ? this.parentClassTemplate.getLevelReward(level).stream()
                : Stream.empty()
        ).sorted().toList();
    }
}
