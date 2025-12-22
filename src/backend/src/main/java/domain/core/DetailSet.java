// java/domain/core/DetailSet.java
package domain.core;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A small {@link Detail} collection object. Mutable.
 */
public class DetailSet {

    // --- Attributes ---
    protected PriorityQueue<Detail> details;

    /* ---------------------------- Constructors ---------------------------- */

    // Constructor
    public DetailSet() {
        this.details = new PriorityQueue<Detail>();
    }

    // Overloaded Constructor (non-empty starting point)
    protected DetailSet(List<Detail> details) {
        this.details = new PriorityQueue<Detail>(details);
    }

    // Overloaded Constructor (copy a Detailed object)
    protected DetailSet(DetailSet old) {
        this.details = new PriorityQueue<>(old.details);
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    /**
     * Getter: Flattens {@link Detail} {@link PriorityQueue} to a {@link List},
     * maintaining queue ordering in result without altering the original queue.
     *
     * @return Ordered list of Details this object currently posseses
     */
    public List<Detail> getDetails() {
        List<Detail> list = new ArrayList<>(details.size());
        PriorityQueue<Detail> copy = new PriorityQueue<>(details);
        while (!copy.isEmpty()) { list.add(copy.poll()); }
        return list;
    }

    /**
     * Setter: Add a {@link Detail} to this object.
     *
     * @param detail Detail added to this detail set.
     */
    public void addDetail(Detail detail) { this.details.add(detail); }

    /**
     * Setter: Add a {@link List} of {@link Detail}s to this object.
     *
     * @param details Details added to this detail set.
     */
    public void setDetails(List<Detail> details) { this.details.addAll(details); }

    /**
     * Setter: Replace current {@link Detail}s {@link PriorityQueue} with
     * supplied parameter.
     *
     * @param details Details overwriting current detail set.
     */
    public void setDetails(PriorityQueue<Detail> details) {
        this.details = new PriorityQueue<>(details);
    }
}
