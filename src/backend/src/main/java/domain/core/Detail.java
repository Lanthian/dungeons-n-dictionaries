// java/domain/core/Detail.java
package domain.core;

import domain.utils.StringUtils;

/**
 * Additional textual information that can be appended to any fixed entity.
 * Details are sorted by descending {@code order}, then default
 * {@link Described} sorting.
 */
public class Detail extends Entity<Detail> implements Comparable<Detail> {

    // --- Constants ---
    private static final int DEFAULT_ORDER = 0;

    // --- Attributes ---
    private String title;
    private String body;
    private int order;

    // Constructor
    public Detail(String title, String body, int order) {
        this.title = title;
        this.body = body;
        this.order = order;
    }

    // Overloaded Constructor (optional order)
    public Detail(String title, String body) {
        this(title, body, DEFAULT_ORDER);
    }

    /* ======================================================================
     * -------------------------- Comparison Logic --------------------------
     * ====================================================================== */

    @Override
    public int compareTo(Detail o) {
        // Compare ordering (descending)
        int res = Integer.compare(o.order, this.order);
        if (res != 0) return res;

        // Compare by name
        res = this.title.compareTo(o.title);
        if (res != 0) return res;

        // Compare by description
        res = this.body.compareTo(o.body);
        return res;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    // --- Getters ---
    public String getTitle() { return this.title; }
    public String getBody() { return this.body; }

    // --- Setters ---
    public void setTitle(String val) { this.title = val; }
    public void setBody(String val) { this.body = val; }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("Detail")
            .add("title=" + StringUtils.quote(title))
            .add("body=" + StringUtils.quote(body))
            .add("order=" + order)
            .toString();
    }
}
