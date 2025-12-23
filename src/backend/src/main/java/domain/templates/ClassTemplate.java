// java/domain/templates/ClassTemplate.java
package domain.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import domain.builders.AbstractBuilder;
import domain.character.Character;
import domain.character.CharacterModifier;
import domain.core.Detail;
import domain.core.DetailSet;
import domain.core.Entity;
import domain.modifiers.LevelReward;
import domain.utils.StringUtils;

/**
 * The attributes a {@link Character} Class has in D&D.
 * Named `CharacterTemplate` to avoid clashing Java keyword `Class`.
 */
public class ClassTemplate extends Entity<ClassTemplate> implements CharacterModifier {

    // --- Attributes ---
    // Simple attributes
    private final String name;
    private final String description;
    private final String hitPoints;
    private final String hitDice;
    // Foreign associations
    private final DetailSet details;
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
    public static class Builder extends AbstractBuilder<ClassTemplate> {

        // --- Attributes ---
        // Simple attributes
        private String name;
        private String description;
        private String hitPoints;
        private String hitDice;
        // Foreign associations
        private DetailSet details;
        private ClassTemplate parentClass;
        private Map<Integer, LevelReward> levelRewards;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            this.name = name;
            this.description = description;
            // Defaults
            this.details = new DetailSet();
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

        /* --------------------------- Dependants --------------------------- */

        /**
         * Appends all {@link Detail}s from provided parameter to current list.
         */
        public Builder details(List<Detail> details) {
            this.details.addAll(details); return this;
        }

        /**
         * Appends a {@link Detail} to current list.
         */
        public Builder detail(Detail detail) {
            this.details.add(detail); return this;
        }
    }

    // Local Constructor
    private ClassTemplate(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.hitPoints = builder.hitPoints;
        this.hitDice = builder.hitDice;

        this.details = builder.details;
        this.parentClass = builder.parentClass;
        this.levelRewards = builder.levelRewards;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    // --- Getters ---
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public List<Detail> getDetails() { return this.details.getDetails(); }
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

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("ClassTemplate")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("details=" + details)
            .add("hitPoints=" + StringUtils.quote(hitPoints))
            .add("hitDice=" + StringUtils.quote(hitDice))
            .add("parentClass=" + parentClass)
            .add("levelRewards=" + levelRewards)
            .toString();
    }
}
