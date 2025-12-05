// src/domain/builders/DescribedBuilder.java
package domain.builders;

import domain.core.Described;

/**
 * Abstract builder to be extended by other builder patterns for eased 
 * {@link Described} entity construction. When extended, create a new 
 * {@code Builder} object, call the relevant construction methods upon it, then 
 * finalise the process with the {@link #build()} method.
 */
public abstract class DescribedBuilder<T extends Described> extends AbstractBuilder<T> {

    /* ---------------------------- Construction ---------------------------- */

    // --- Attributes ---
    protected String name;
    protected String description;

    // Builder Constructor
    protected DescribedBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Overloaded Constructor (optional description)
    protected DescribedBuilder(String name) {
        this(name, null);
    }

    /* ------------------------- Simple  Attributes ------------------------- */

    public DescribedBuilder<T> description(String description) { this.description = description; return this; }

    /* ------------------------------ Getters  ------------------------------ */

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
}
