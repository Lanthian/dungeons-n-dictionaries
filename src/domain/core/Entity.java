// src/domain/core/Entity.java
package domain.core;

/**
 * Declaration that a domain model object is an entity and requires a database 
 * level unique identifier. Contains a fixed, type safe ID via the Curiously 
 * Recurring Template Pattern.
 */
public abstract class Entity<T> {
    
    // --- Attributes ---
    protected final EntityId<T> id;

    // Constructor
    protected Entity(EntityId<T> id) {
        this.id = id;
    }

    // --- Getter ---
    public EntityId<T> getId() { return id; }
}
