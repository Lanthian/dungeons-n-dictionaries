// src/domain/AbstractBuilder.java
package domain;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Abstract builder to be extended by other concrete builder patterns. Defines
 * a basic contract - all builders must implement a {@link #build()} method 
 * that returns an object of type {@code T}.
 * 
 * <p> When extended, create a new {@code Builder} object, call the relevant 
 * construction methods upon it, then finalise the process via {@code .build()}.
 */
public abstract class AbstractBuilder<T> {

    // --- Constants ---
    protected final static String UNDEFINED = null;

    /**
     * {@link AbstractBuilder} extending Builders must implement the below 
     * method. Constructs a concrete entity of type {@code T}.
     * 
     * @return {@code T} entity constructed by this builder
     */
    abstract public T build();

    /**
     * Utility method to have a {@code Builder} that constructs type {@code R} 
     * perform actions defined by a {@code Builder} {@code consumer} before 
     * finalising this process by returning {@link #build()} output.
     * 
     * <p> Demo usage: 
     * <pre>{@code 
     * Test result = buildWith(Test.Builder::new, 
     *                         b -> (b.attribute("Hello World"));
     * }</pre>
     * 
     * @param <R> Type returned by this Builder
     * @param <Builder> Type of builder object that constructs {@code R}
     * @param supplier Supplier that creates a new builder instance
     * @param consumer Consumer that modifies builder construction process
     * @return The {@code R} instance produced after consumer operations
     */
    protected static <R, Builder extends AbstractBuilder<R>> R buildWith(
        Supplier<Builder> supplier, 
        Consumer<Builder> consumer
    ) {
        Builder builder = supplier.get();
        consumer.accept(builder);
        return builder.build();
    }
}
