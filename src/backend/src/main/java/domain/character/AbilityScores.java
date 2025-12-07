// java/domain/character/AbilityScores.java
package domain.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.types.Ability;
import domain.utils.StringUtils;

/**
 * A mapping of Character abilities and their scorings. Rules sourced from point
 * buy variant, found at:
 * https://www.dndbeyond.com/sources/dnd/basic-rules-2014/step-by-step-characters#3DetermineAbilityScores
 */
public class AbilityScores {

    // --- Constants ---
    // Scores
    private static final Integer DEFAULT = 10;
    private static final Integer MIN = 8;
    private static final Integer MAX = 15;
    // Point buy constants
    private static final int MAX_POINTS = 27;
    private static final Map<Integer, Integer> PB_COST = Map.of(
        8, 0,
        9, 1,
        10, 2,
        11, 3,
        12, 4,
        13, 5,
        14, 7,
        15, 9
    );

    // --- Attributes ---
    private final HashMap<Ability, Integer> scores;
    private int pointsLeft;

    // --- Constructors ---
    // Overloaded Default Constructor
    public AbilityScores() {
        this(Collections.nCopies(Ability.values().length, DEFAULT));
    }

    // Constructor
    public AbilityScores(List<Integer> scores) {
        // Validate input length
        if (scores == null || scores.size() != Ability.values().length) {
            throw new IllegalArgumentException(
                "AbilityScores constructor requires " + Ability.values().length + " or no scores"
            );
        }

        // Assign scores to abilities
        this.scores = new HashMap<>();
        Ability[] abilities = Ability.values();
        for (int i = 0; i < abilities.length; i++) {
            int score = scores.get(i);
            // Ensure change is within allowed range
            if (!validScore(score)) {
                throw new IllegalArgumentException(
                    "Invalid ability score allocation - score must fall within [" + MIN + "," + MAX + "] range"
                );
            }
            this.scores.put(abilities[i], score);
        }

        // Calculate points left over after default generation
        this.pointsLeft = pointsLeft();
        if (pointsLeft < 0) {
            throw new IllegalArgumentException("Invalid ability score allocation - too many points spent");
        }
    }

    /**
     * Method to change allocated ability scores according to point buy rules.
     *
     * @param ability {@link Ability} for which score is updated
     * @param change pos or neg integer change to score, within rule guidelines
     * @return true on a legal, executed update, false otherwise
     */
    public boolean updateScore(Ability ability, int change) {
        int oldScore = this.scores.get(ability);
        int newScore = oldScore + change;

        // Ensure change is within allowed range
        if (!validScore(newScore)) { return false; }

        // Ensure cost of change can be paid
        int cost = PB_COST.get(newScore) - PB_COST.get(oldScore);
        int newPointsLeft = this.pointsLeft - cost;
        if (newPointsLeft < 0) { return false; }

        // Perform the update
        this.scores.put(ability, newScore);
        this.pointsLeft = newPointsLeft;
        return true;
    }

    /**
     * Utility method to calculate the difference between the current point buy
     * cost of ability score allocation and the point buy limit.
     *
     * @return points left over after allocation
     */
    private int pointsLeft() {
        int cost = 0;
        for (int score : this.scores.values()) { cost += PB_COST.get(score); }
        return MAX_POINTS - cost;
    }

    /**
     * Utility method to validate if an ability score is within allowed range.
     *
     * @param score value validated
     * @return true if in [MIN, MAX], false otherwise
     */
    private boolean validScore(int score) {
        return (score >= MIN && score <= MAX);
    }

    // --- Getters ---
    public Map<Ability, Integer> getScores() { return Map.copyOf(this.scores); }
    public int getScore(Ability ability) { return this.scores.get(ability); }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("AbilityScores")
            .add("scores=" + scores)
            .add("pointsLeft=" + pointsLeft)
            .toString();
    }
}
