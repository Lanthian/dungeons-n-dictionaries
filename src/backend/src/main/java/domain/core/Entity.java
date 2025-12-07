// java/domain/core/Entity.java
package domain.core;

/**
 * Declaration that a domain model object is an entity and stores a database
 * level unique identifier. Contains a type safe ID via the Curiously Recurring
 * Template Pattern. ID does not need to be set on entity creation, but once
 * set, is fixed.
 */
public abstract class Entity<T> {

    // --- Attributes ---
    protected EntityId<T> id;

    // Constructor
    protected Entity(EntityId<T> id) {
        this.id = id;
    }

    // Overloaded Constructor (no ID set at creation time)
    protected Entity() {
        this.id = null;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    // --- Getters ---
    public EntityId<T> getId() { return this.id; }
    public boolean hasId() { return this.id != null; }

    /**
     * Setter: Add an {@link Entity}'s {@code id} value post-creation.
     *
     * @param id Uniquely identifying, type-safe entity ID
     * @return true if ID has not already been set and passed value is not null,
     *         false otherwise
     */
    public boolean setId(EntityId<T> id) {
        // Only allow ID setting if entity doesn't currently have an ID
        if (hasId() || id == null) return false;
        this.id = id;
        return true;
    }
}
