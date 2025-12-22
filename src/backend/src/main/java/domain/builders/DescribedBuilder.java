// java/domain/builders/DescribedBuilder.java
package domain.builders;

import domain.core.Describable;
import domain.core.Description;

/**
 * Abstract builder to be extended by other builder patterns for eased
 * {@link Described} entity construction. When extended, create a new
 * {@code Builder} object, call the relevant construction methods upon it, then
 * finalise the process with the {@link #build()} method.
 */
public abstract class DescribedBuilder<T extends Describable> extends AbstractBuilder<T> {

    /* ---------------------------- Construction ---------------------------- */

    // --- Attributes ---
    protected Description description;

    // Builder Constructor
    protected DescribedBuilder(String name, String description) {
        this.description = new Description(name, description);
    }

    // Overloaded Constructor (optional description)
    protected DescribedBuilder(String name) {
        this(name, null);
    }

    /* ------------------------- Simple  Attributes ------------------------- */

    public DescribedBuilder<T> description(String description) {
        this.description.setDescription(description);
        return this;
    }

    /* ------------------------------ Getters  ------------------------------ */

    public String getName() { return this.description.getName(); }
    public String getDescription() { return this.description.getDescription(); }
}
