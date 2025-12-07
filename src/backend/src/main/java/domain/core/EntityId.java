// java/domain/core/EntityId.java
package domain.core;

/**
 * Type safe unique identifier for entities within the domain model. Prevents ID
 * misuse across different entity types. Follows the Curiously Recurring
 * Template Pattern.
 *
 * <p> CRTP: https://en.wikipedia.org/wiki/Curiously_recurring_template_pattern
 */
public record EntityId<T>(long value) { }
