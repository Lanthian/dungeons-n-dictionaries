// java/datasource/mappers/MapperRegistry.java
package datasource.mappers;

import java.util.HashMap;
import java.util.Map;

import datasource.mappers.proficiency.ProficiencyMapper;
import domain.modifiers.AbilityScoreModifier;
import domain.modifiers.Feat;
import domain.modifiers.Language;
import domain.modifiers.proficiency.Proficiency;

/**
 * Utility class to identify and return a relevant datasource {@link Mapper} for
 * any given domain model object. All utilised Mappers must be registered here.
 */
public class MapperRegistry {

    // --- Constants ---
    // One singular mapper for supplier composition - do not offer public access
    private final static CharacterModifierMapper SUPPLY_MAPPER = new CharacterModifierMapper();
    // Registered, accessible mappers
    private final static Map<Class<?>, Mapper<?>> REGISTRY = new HashMap<>();
    static {
        // Register all Mappers here
        REGISTRY.put(AbilityScoreModifier.class, new AsmMapper());
        REGISTRY.put(Feat.class, new FeatMapper(SUPPLY_MAPPER));
        REGISTRY.put(Language.class, new LanguageMapper());
        REGISTRY.put(Proficiency.class, new ProficiencyMapper());
    }

    /**
     * Retrieve an instantiated {@link Mapper} registered to the supplied domain
     * model class. Returns a valid subtype mapper if no exact match registered.
     *
     * @param <T> type of the the object and Mapper generic type queried
     * @param c {@code .class} of the object to be mapped
     * @return relevant Mapper or null if unregistered/undefined
     */
    @SuppressWarnings("unchecked")
    public static <T> Mapper<T> getMapper(Class<T> c) {
        // 1. Exact match
        Mapper<?> mapper = REGISTRY.get(c);
        if (mapper != null) return (Mapper<T>) mapper;

        // 2. Polymorphic match
        for (Map.Entry<Class<?>, Mapper<?>> entry : REGISTRY.entrySet()) {
            // Check supertype / interface
            if (entry.getKey().isAssignableFrom(c)) {
                return (Mapper<T>) entry.getValue();
            }
        }

        // 3. No match
        return null;
    }
}
