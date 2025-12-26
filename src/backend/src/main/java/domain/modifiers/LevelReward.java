// java/domain/modifiers/LevelReward.java
package domain.modifiers;

import java.util.List;
import java.util.function.Function;

import domain.character.CharacterModifier;
import domain.core.Entity;
import domain.modifiers.proficiency.Proficiency;
import domain.templates.ClassTemplate;
import domain.utils.StringUtils;

/**
 * A collection of {@link CharacterModifier} updates that a specific level in a
 * {@link ClassTemplate} provides.
 * TODO: Subclass selection needs to be supported / offered as a reward
 * - type of reward should perhaps be defined in this case
 */
public class LevelReward extends Entity<LevelReward> implements CharacterModifier, Comparable<LevelReward> {

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
    public List<? extends Language> getLanguages() {
        return all(CharacterModifier::getLanguages);
    }

    @Override
    public List<? extends Proficiency> getProficiencies() {
        return all(CharacterModifier::getProficiencies);
    }

    @Override
    public List<? extends Feat> getFeats() {
        return all(CharacterModifier::getFeats);
    }

    @Override
    public List<? extends AbilityScoreModifier> getAbilityScoreModifiers() {
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
    private <X> List<? extends X> all(Function<CharacterModifier, List<X>> getter) {
        return rewards.stream().flatMap(c -> getter.apply(c).stream()).toList();
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    // --- Getters ---
    public int getLevel() { return this.level; }
    public List<CharacterModifier> getRewards() { return List.copyOf(this.rewards); }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("LevelReward")
            .add("level=" + level)
            .add("rewards=" + rewards)
            .toString();
    }
}
