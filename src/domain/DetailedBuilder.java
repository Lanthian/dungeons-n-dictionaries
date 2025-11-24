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
public abstract class DetailedBuilder<T extends Detailed> {

    /* ---------------------------- Construction ---------------------------- */

    // --- Attributes ---
    protected String name;
    protected String description;
    protected PriorityQueue<Detail> details;

    // Builder Constructor
    public DetailedBuilder(String name, String description) {
        this.name = name;
        this.description = description;
        this.details = new PriorityQueue<Detail>();
    }

    // Overloaded Constructor (optional description)
    public DetailedBuilder(String name) {
        this(name, null);
    }

    /**
     * {@link DetailedBuilder} extending Builders must implement the below 
     * method. Constructs a concrete {@link Detailed} entity.
     * 
     * @return {@link Detailed} entity constructed by this builder
     */
    abstract public T build();

    /* ------------------------- Simple  Attributes ------------------------- */

    public DetailedBuilder<T> description(String description) { this.description = description; return this; }

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
