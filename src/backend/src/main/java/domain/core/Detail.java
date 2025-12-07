// java/domain/core/Detail.java
package domain.core;

import domain.utils.StringUtils;

/**
 * Additional textual information that can be appended to any fixed entity.
 * Details are sorted by descending {@code order}, then default
 * {@link Described} sorting.
 */
public class Detail extends Described<Detail> {

    // --- Constants ---
    private static final int DEFAULT_ORDER = 0;

    // --- Attributes ---
    private int order;

    // Constructor
    public Detail(String name, String description, int order) {
        super(name, description);
        this.order = order;
    }

    // Overloaded Constructor (optional order)
    public Detail(String title, String description) {
        this(title, description, DEFAULT_ORDER);
    }

    /* ======================================================================
     * -------------------------- Comparison Logic --------------------------
     * ====================================================================== */

    @Override
    protected int compareBefore(Described<Detail> o) {
        // Cast `Described` object to class `Detail`
        if (o.getClass() != this.getClass()) {
            throw new ClassCastException("Class mismatch - Described objects cannot be differentiated on order");
        }
        Detail cast = (Detail) o;

        // Compare ordering (descending)
        return ((Integer) cast.order).compareTo(this.order);
    }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("Detail")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("order=" + order)
            .toString();
    }
}
