// src/domain/character/CharacterSelection.java
package domain.character;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.Language;
import domain.modifiers.choice.ChoiceOption;
import domain.modifiers.proficiency.Proficiency;
import domain.utils.StringUtils;

/**
 * An explicit subscription of a {@link Character} to a particular 
 * {@link CharacterModifier}, including resolved choices offered.
 */
public class CharacterSelection<T extends CharacterModifier> implements CharacterModifier {
    
    // --- Attributes ---
    protected final T template;
    protected final List<ChoiceOption<?>> choices;

    // Constructor
    public CharacterSelection(T template, List<ChoiceOption<?>> choices) {
        this.template = template;
        this.choices = List.copyOf(choices);
    }

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
     * Utility method to combine {@link CharacterModifier} {@code template} 
     * lists and type matching {@code choices} items into one returned list.
     * 
     * @param <X> The type of the retrieved / combined list items
     * @param getter The relevant {@link CharacterModifier} getter
     * @return The joined list of type matching values in template and choices
     */
    private <X> List<X> all(Function<CharacterModifier, List<X>> getter) {
        return Stream.concat(
            getter.apply(template).stream(),
            choices.stream().flatMap(c -> getter.apply(c).stream())
        ).toList();
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public T getTemplate() { return this.template; }
    public List<ChoiceOption<?>> getChoices() { return List.copyOf(this.choices); }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("ClassSelection")
            .add("template=" + template)
            .add("choices=" + choices)
            .toString();
    }
}
