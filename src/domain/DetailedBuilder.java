// src/domain/DetailedBuilder.java
package domain;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Abstract builder to be extended by other builder patterns for eased 
 * {@link Detailed} entity construction. When extended, create a new 
 * {@code Builder} object, call the relevant construction methods upon it, then 
 * finalise the process with the {@link #build()} method.
 */
public abstract class DetailedBuilder<T extends Detailed> extends DescribedBuilder<T> {

    /* ---------------------------- Construction ---------------------------- */

    // --- Attributes ---
    protected PriorityQueue<Detail> details;

    // Builder Constructor
    public DetailedBuilder(String name, String description) {
        super(name, description);
        this.details = new PriorityQueue<Detail>();
    }

    // Overloaded Constructor (optional description)
    public DetailedBuilder(String name) {
        this(name, null);
    }

    /* ----------------------------- Dependants ----------------------------- */

    /** 
     * Appends all {@link Detail}s from provided parameter to current list.
     */
    public DetailedBuilder<T> details(List<Detail> details) {
        this.details.addAll(details); return this;
    }

    /** 
     * Appends a {@link Detail} to current list.
     */
    public DetailedBuilder<T> detail(Detail detail) {
        this.details.add(detail); return this;
    }
}
