// src/domain/modifiers/choice/ChoiceOption.java
package domain.modifiers.choice;

import java.util.Collections;
import java.util.List;

import domain.character.Character;
import domain.character.CharacterModifier;
import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.Language;
import domain.modifiers.proficiency.Proficiency;

/**
 * A record to wrap multiple {@link Character} {@link Choice} options under the
 * same type together. Implements {@link CharacterModifier} methods.
 */
public record ChoiceOption<T>(T value) implements CharacterModifier { 

    /**
     * A factory method to instantiate a {@link ChoiceOption} with payload 
     * {@code value}.
     * 
     * @param <T> Type of payload stored
     * @param value Payload stored
     * @return ChoiceOption constructed
     */
    public static <T> ChoiceOption<T> of(T value) {
        return new ChoiceOption<T>(value);
    }

    /**
     * Shorthand utility method to return the class of the value stored in this 
     * {@link ChoiceOption}.
     * 
     * @return Class of {@code value}
     */
    public Class<?> getValueClass() {
        return this.value.getClass();
    }

     /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Language> getLanguages() { 
        return match(Language.class); 
    }
    
    @Override
    public List<Proficiency> getProficiencies() { 
        return match(Proficiency.class); 
    }

    @Override
    public List<Feat> getFeats() { 
        return match(Feat.class); 
    }

    @Override
    public List<AbilityScoreModifier> getAbilityScoreModifiers() { 
        return match(AbilityScoreModifier.class); 
    }

    /**
     * Utility method to verify if {@link ChoiceOption} payload {@code value} is
     * an instance of a particular {@link CharacterModifier}-supplied class. 
     * 
     * <p> Returns a list of this option's payload if type matches, or an empty 
     * list if not.
     *  
     * @param <X> The type of class to check against
     * @param type The {@link Class} object representing the type to match
     * @return A {@link List} containing the payload if it matches {@code type},
     *         {@link Collections#emptyList()} if otherwise
     */
    private <X> List<X> match(Class<X> type) {
        return type.isInstance(value) 
            ? List.of(type.cast(value))
            : Collections.emptyList();
    }
}
