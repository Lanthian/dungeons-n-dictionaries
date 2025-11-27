// src/domain/templates/ClassTemplate.java
package domain.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import domain.builders.DetailedBuilder;
import domain.character.Character;
import domain.character.CharacterModifier;
import domain.core.Detailed;
import domain.modifiers.LevelReward;

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
    private final ClassTemplate parentClass;
    private final Map<Integer, LevelReward> levelRewards;
    
    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for {@link ClassTemplate} construction.
     * Create a new {@code Builder} object, call the relevant construction 
     * methods upon it, then finalise the process with the {@link #build()} 
     * method.
     */
    public static class Builder extends DetailedBuilder<ClassTemplate> {

        // --- Attributes ---
        // Simple attributes
        private String hitPoints;
        private String hitDice;
        // Foreign associations
        private ClassTemplate parentClass;
        private Map<Integer, LevelReward> levelRewards;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            super(name, description);
            
            // Defaults
            this.hitPoints = UNDEFINED;
            this.hitDice = UNDEFINED;
            this.parentClass = null;
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
        public Builder parentClass(ClassTemplate parentClass) {
            this.parentClass = parentClass; return this;
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
    }

    // Local Constructor
    private ClassTemplate(Builder builder) {
        // name, description & details copied from DetailedBuilder
        super(builder.getName(), builder.getDescription());
        this.setDetails(builder.getDetails());
        
        this.hitPoints = builder.hitPoints;
        this.hitDice = builder.hitDice;

        this.parentClass = builder.parentClass;
        this.levelRewards = builder.levelRewards;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getHitPoints() { return this.hitPoints; }
    public String getHitDice() { return this.hitDice; }
    public ClassTemplate getParentClass() { return this.parentClass; }

    /**
     * Returns all {@link LevelReward}s including those supplied by parent 
     * class. Sorted from lowest to highest level.
     */
    public List<LevelReward> getAllLevelRewards() {
        return recursiveFilterRewards(reward -> true);
    }

    /**
     * Returns all {@link LevelReward}s lower than provided paramater, including
     * those supplied by parent class. Sorted from lowest to highest level.
     * 
     * @param level (Inclusive) level cap for level rewards returned
     * @return Sorted list of level rewards below level param
     */
    public List<LevelReward> getLesserLevelRewards(int level) {
        return recursiveFilterRewards(reward -> reward.getLevel() <= level);
    }

    /**
     * Returns a list of {@link LevelReward}s supplied by this class and parent 
     * class pathway at a particular queried level.
     * 
     * @param level Level at which level rewards are queried
     * @return Sorted list of level rewards at level param
     */
    public List<LevelReward> getLevelReward(int level) {
        return recursiveFilterRewards(reward -> reward.getLevel() == level);
    }

    /* -------------------------- Utility  Methods -------------------------- */

    /**
     * Utility method to recursively retrieve a {@link Stream} of 
     * {@link LevelReward}s supplied by this class and parent class pathway.
     * 
     * @param filter A boolean-valued filter function evaluated on level rewards
     * @return Stream of level rewards satisfying {@code filter}
     */
    private Stream<LevelReward> recursiveFilterRewardsStream(Predicate<LevelReward> filter) {
        return Stream.concat(
            // Current class rewards filtered
            levelRewards.values().stream()
                .filter(filter),

            // Filtered parent class rewards if parent present
            this.parentClass != null
                ? this.parentClass.recursiveFilterRewardsStream(filter)
                : Stream.empty()
        );
    }

    /**
     * Utility method to shorthand conversion from the following methods 
     * {@link Stream} output to a sorted {@link List}: 
     * {@link #recursiveFilterRewardsStream(Predicate)}
     * 
     * <p>Sorts list ascending on total {@link LevelReward} levels.
     * 
     * @param filter A boolean-valued filter function evaluated on level rewards
     * @return Sorted list of level rewards satisfying {@code filter}
     */
    private List<LevelReward> recursiveFilterRewards(Predicate<LevelReward> filter) {
        return recursiveFilterRewardsStream(filter).sorted().toList();
    }
}
