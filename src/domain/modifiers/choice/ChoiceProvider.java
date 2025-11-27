// src/domain/modifiers/choice/ChoiceProvider.java
package domain.modifiers.choice;

import java.util.List;

import domain.character.Character;

/**
 * A contract {@link Character} components implement in order to be suppliers 
 * of {@link Choice}s. These choices represent unresolved decisions.
 */
public interface ChoiceProvider {

    /**
     * Getter for a ChoiceProvider's supplied {@link Choice}s.
     * 
     * @return A potentially empty {@code List} of choices offered.
     */
    List<Choice> getChoices();
}
