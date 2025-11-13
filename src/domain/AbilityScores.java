// src/domain/AbilityScores.java
package domain;

import java.util.HashMap;
import java.util.Map;

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
    private HashMap<Ability, Integer> scores;
    private int pointsLeft;
    
    // Constructor
    public AbilityScores() {
        // Default ability score generation
        this.scores = new HashMap<>();
        for (Ability ability : Ability.values()) {
            this.scores.put(ability, DEFAULT);
        }
        // Calculate points left over after default generation
        this.pointsLeft = pointsLeft();
        assert (pointsLeft >= 0) : "Invalid default ability score allocation";
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
        if (newScore < MIN || newScore > MAX) { return false; }

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
}
