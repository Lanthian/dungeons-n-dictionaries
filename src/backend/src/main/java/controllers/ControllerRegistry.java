// java/controllers/ControllerRegistry.java
package controllers;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to identify and return a relevant {@link Controller} for any
 * given resource routed. All utilised Controllers must be registered here.
 */
public class ControllerRegistry {
    private final static Map<String, Controller> REGISTRY = new HashMap<>();
    static {
        // Register all Controller routes here
        REGISTRY.put("language", new LanguageController());
    }

    /**
     * Retrieve an instantiated {@link Controller} registered to the supplied
     * resource name.
     *
     * @param resource keyname to which specific Controller is mapped
     * @return relevant Controller or null if unregistered/undefined
     */
    public static Controller getController(String resource) {
        return REGISTRY.get(resource);
    }
}
