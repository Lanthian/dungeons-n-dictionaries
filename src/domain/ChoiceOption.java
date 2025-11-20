// src/domain/ChoiceOption.java
package domain;

/**
 * A record to wrap multiple {@link Character} {@link Choice} options under the
 * same type together. Does not require any methods to be implemented.
 */
public record ChoiceOption<T>(T value) { 

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
    public Class<? extends Object> getValueClass() {
        return this.value.getClass();
    }
}
