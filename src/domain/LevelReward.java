// src/domain/LevelReward.java
package domain;

import java.util.List;
import java.util.function.Function;

/**
 * A collection of {@link CharacterModifier} updates that a specific level in a 
 * {@link ClassTemplate} provides.
 * TODO: Subclass selection needs to be supported / offered as a reward
 */
public class LevelReward implements CharacterModifier, Comparable<LevelReward> {
    
    // --- Constants ---
    private static final int DEFAULT_LEVEL = 1;

    // --- Attributes ---
    private final int level;
    private final List<CharacterModifier> rewards;

    // Constructor
    public LevelReward(int level, List<CharacterModifier> rewards) {
        this.level = level;
        this.rewards = List.copyOf(rewards);
    }

    // Overloaded Constructor (defaults level 1)
    public LevelReward(List<CharacterModifier> rewards) { 
        this(DEFAULT_LEVEL, rewards);
    }

    /* ======================================================================
     * -------------------------- Comparison Logic -------------------------- 
     * ====================================================================== */

    // Overriden Ordering
    @Override
    public final int compareTo(LevelReward o) {
        // Compare using 'before' extended logic
        int res = compareBefore(o);
        if (res != 0) return res;

        // Compare by level
        res = ((Integer) this.level).compareTo(o.level);
        if (res != 0) return res;

        // Finally, compare using 'after' extended logic if necessary
        return compareAfter(o);
    }

    /**
     * Extendable comparison injected before default 
     * {@link #compareTo(LevelReward)} method.
     * 
     * @param o {@link LevelReward} extending object being compared against
     * @return a negative integer, zero or positive integer if {@code this} is 
     *         less than, equal to, or greater than {@code o}
     */
    protected int compareBefore(LevelReward o) { return 0; }

    /**
     * Extendable comparison injected after default 
     * {@link #compareTo(LevelReward)} method.
     * 
     * @param o {@link LevelReward} extending object being compared against
     * @return a negative integer, zero or positive integer if {@code this} is 
     *         less than, equal to, or greater than {@code o}
     */
    protected int compareAfter(LevelReward o) { return 0; }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

     @Override
    public List<Language> getLanguages() { 
        return all(CharacterModifier::getLanguages); 
    }
    
    @Override
    public List<Proficiency> getProficiencies() { 
        return all(CharacterModifier::getProficiencies); 
    }

    @Override
    public List<Feat> getFeats() { 
        return all(CharacterModifier::getFeats); 
    }

    @Override
    public List<AbilityScoreModifier> getAbilityScoreModifiers() { 
        return all(CharacterModifier::getAbilityScoreModifiers);
    }

    /**
     * Utility method to filter type matching {@link CharacterModifier} 
     * {@code reward} items into one returned list.
     * 
     * @param <X> The type of the filtered list items
     * @param getter The relevant {@link CharacterModifier} getter
     * @return The list of type matching values in rewards
     */
    private <X> List<X> all(Function<CharacterModifier, List<X>> getter) {
        return rewards.stream().flatMap(c -> getter.apply(c).stream()).toList();
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public int getLevel() { return this.level; }
    public List<CharacterModifier> getRewards() { return this.rewards; }
}
