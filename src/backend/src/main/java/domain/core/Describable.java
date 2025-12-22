package domain.core;

public interface Describable {
    Description description();

    default String getName() { return description().getName(); }

    default String getDescription() { return description().getDescription(); }
}
