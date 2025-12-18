// java/config/Env.java
package config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Utility class to hide environment variable / property retrieval.
 * Access variables via {@link #get(String) Env.get(key)}.
 *
 * <p> Requires the System property {@code ABSOLUTE_BACKEND_ROOT} to be set to
 * utilise {@link Dotenv} variable retrieval functionality.
 */
public class Env {

    // --- Constants ---
    private static final Dotenv DOTENV;

    /* GPT generated section: Safeguarding inability to find .env ........... */
    static {
        String baseDir = System.getProperty("ABSOLUTE_BACKEND_ROOT");
        if (baseDir != null && !baseDir.isBlank()) {
            DOTENV = Dotenv.configure()
                    .directory(baseDir)
                    .ignoreIfMissing()
                    .load();
            System.out.println("Loaded .env from ABSOLUTE_BACKEND_ROOT: " + baseDir);
        } else {
            DOTENV = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            System.out.println(".env not found via ABSOLUTE_BACKEND_ROOT, fallback to default");
        }
    }
    /* ................................................... End GPT disclaimer */

    // Private Constructor (disallow instantiation)
    private Env() {}

    /**
     * Returns an environment variable specified by a particular {@code key}.
     * First checks root .env file, then {@link System#getProperty(String)
     * System.getProperty(key)} and then {@link System#getenv(String)
     * System.getenv(key)} if no match.
     *
     * @param key of queried env file variable
     * @return String variable if found, {@code null} if otherwise
     */
    public static String get(String key) {
        // If key is null or empty, return null
        if (key == null || key.isBlank()) return null;
        return DOTENV.get(key, System.getProperty(key, System.getenv(key)));
    }

    /**
     * Returns an environment variable specified by a particular {@code key}.
     * Requires variable to be specified or throws an exception.
     *
     * @param key of queried env file variable
     * @return String variable found
     * @throws IllegalStateException if no String variable is found
     */
    public static String getRequired(String key) throws IllegalStateException {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required config: " + key);
        }
        return value;
    }
}
