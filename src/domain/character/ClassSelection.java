// src/domain/character/ClassSelection.java
package domain.character;

import java.util.List;

import domain.modifiers.choice.ChoiceOption;
import domain.templates.ClassTemplate;

/**
 * An explicit subscription of a {@link Character} to a particular 
 * {@link ClassTemplate}, including resolved choices offered and class level.
 */
public class ClassSelection extends CharacterSelection<ClassTemplate> {

    // --- Constants ---
    private final static int DEFAULT_LEVEL = 1;

    // --- Attributes ---
    private int level;

    // Constructor
    public ClassSelection(ClassTemplate template, List<ChoiceOption<?>> choices, int level) {
        super(template, choices);
        this.level = level;
    }

    // Overloaded Constructor (defaults first level)
    public ClassSelection(ClassTemplate template, List<ChoiceOption<?>> choices) {
        this(template, choices, DEFAULT_LEVEL);
    }

    // Getter
    public int getLevel() { return this.level; }
}
