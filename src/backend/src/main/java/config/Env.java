// java/config/Env.java
package config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Utility class to hide environment variable / property retrieval.
 * Access variables via {@link #get(String) Env.get(key)}.
 */
public class Env {

    // --- Constants ---
    private static final Dotenv DOTENV = Dotenv.load();

    /**
     * Returns an environment variable specified by a particular {@code key}.
     * First checks root .env file, and then {@link System#getProperty(String)
     * System.getProperty(key)} if no match.
     *
     * @param key of queried env file variable
     * @return String variable if found, {@code null} if otherwise
     */
    public static String get(String key) {
        try { return DOTENV.get(key, System.getProperty(key)); }
        catch (NullPointerException | IllegalArgumentException ignored) {
            // If key is null or empty, return null
            return null;
        }
    }
}
