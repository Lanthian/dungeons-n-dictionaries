// src/domain/core/Detailed.java
package domain.core;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * An abstract extension upon the already abstract class {@link Described} to 
 * define an entity that can have additional {@link Detail} information
 * attached. Mutable.
 */
public abstract class Detailed extends Described {

    // --- Attributes ---
    protected PriorityQueue<Detail> details;

    // Constructor
    public Detailed(String name, String description) {
        super(name, description);
        this.details = new PriorityQueue<Detail>();
    }

    // Overloaded Constructor (optional description)
    public Detailed(String name) {
        this(name, null);
    }

    // Overloaded Constructor (copy a Detailed object)
    public Detailed(Detailed old) {
        // TODO: Check if getter needed here
        super(old);
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
     * @param detail Detail added to this entity.
     */
    public void addDetail(Detail detail) { this.details.add(detail); }

    /**
     * Setter: Add a {@link List} of {@link Detail}s to this object.
     * 
     * @param details Details added to this entity.
     */
    public void setDetails(List<Detail> details) { this.details.addAll(details); }

    /**
     * Setter: Replace current {@link Detail}s {@link PriorityQueue} with 
     * supplied parameter.
     * 
     * @param details Details overwriting current entity details.
     */
    public void setDetails(PriorityQueue<Detail> details) { 
        this.details = new PriorityQueue<>(details);
    }
}
