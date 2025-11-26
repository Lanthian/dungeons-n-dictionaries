// src/domain/ChoiceProvider.java
package domain;

import java.util.List;

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
