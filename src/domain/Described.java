// src/domain/Described.java
package domain;

/**
 * An abstract class extended by entities that exist with two key attributes:
 * <ul> 
 *   <li>{@code name}, &
 *   <li>{@code description} 
 * </ul>
 * 
 * <p> Described objects are sorted by ascending {@code name} first, and then 
 * {@code description} if name matches. This comparison can be adjusted by 
 * overriding {@link #compareBefore(Described)} and/or
 * {@link #compareAfter(Described)} methods.
 */
public abstract class Described implements Comparable<Described> {
    
    // --- Attributes ---
    protected String name;
    protected String description;

    // Constructor
    public Described(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Overloaded Constructor (optional description)
    public Described(String name) {
        this(name, null);
    }

    /* ======================================================================
     * -------------------------- Comparison Logic -------------------------- 
     * ====================================================================== */

    // Overriden Ordering
    @Override
    public final int compareTo(Described o) {
        // Compare using 'before' extended logic
        int res = compareBefore(o);
        if (res != 0) return res;

        // Compare by name
        res = this.name.compareTo(o.name);
        if (res != 0) return res;

        // Compare by description
        res = this.description.compareTo(o.description);
        if (res != 0) return res;

        // Finally, compare using 'after' extended logic if necessary
        return compareAfter(o);
    }

    /**
     * {@link Comparable#compareTo(Object)} method modified to work with 
     * nullable values, avoiding potential NullPointerExceptions.
     * 
     * <p> Paired null values are considered equal, and a singular nulled object
     * is considered less than a non-nulled object.
     * 
     * @param <T> the type of the values being compared (implement Comparable)
     * @param a the first value, possibly {@code null}
     * @param b the second value, possibly {@code null}
     * @return a negative integer, zero or positive integer if {@code a} is less
     *         than, equal to, or greater than {@code b}
     */
    protected static <T extends Comparable<? super T>> int compareNullable(T a, T b) {
        if (a == null && b == null) return 0;
        else if (a == null) return -1;
        else if (b == null) return 1;
        else return a.compareTo(b);
    }

    /**
     * Extendable comparison injected before default 
     * {@link #compareTo(Described)} method.
     * 
     * @param o {@link Described} extending object being compared against
     * @return a negative integer, zero or positive integer if {@code this} is 
     *         less than, equal to, or greater than {@code o}
     */
    protected int compareBefore(Described o) { return 0; }

    /**
     * Extendable comparison injected after default 
     * {@link #compareTo(Described)} method.
     * 
     * @param o {@link Described} extending object being compared against
     * @return a negative integer, zero or positive integer if {@code this} is 
     *         less than, equal to, or greater than {@code o}
     */
    protected int compareAfter(Described o) { return 0; }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }

    // --- Setters ---
    public void setName(String val) { this.name = val; }
    public void setDescription(String val) { this.description = val; }
}
