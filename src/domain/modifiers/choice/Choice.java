// src/domain/modifiers/choice/Choice.java
package domain.modifiers.choice;

import java.util.List;

import domain.character.CharacterModifier;
import domain.utils.StringUtils;

/**
 * A collection of {@link ChoiceOption}s with a prescribed {@code count} of the 
 * number of valid choices allowed. Used to mark decision points, primarily in 
 * {@link CharacterModifier}-ing classes.
 */
public class Choice {

    // --- Constants ---
    private static final int DEFAULT_CHOICE_COUNT = 1;
    
    // --- Attributes ---
    private final List<ChoiceOption<?>> options;
    private final int count;

    // Constructor
    public Choice(List<ChoiceOption<?>> options, int count) {
        this.options = List.copyOf(options);
        this.count = count;
    }

    // Overloaded Constructor (defaults 1 available)
    public Choice(List<ChoiceOption<?>> options) {
        this(options, DEFAULT_CHOICE_COUNT);
    }

    // --- Getters ---
    public List<ChoiceOption<?>> getOptions() { return List.copyOf(this.options); }
    public int getCount() { return this.count; }

    // To String
    @Override
    public String toString() {
        return StringUtils.toStringJoiner("Choice")
            .add("options=" + options)
            .add("count=" + count)
            .toString();
    }
}
