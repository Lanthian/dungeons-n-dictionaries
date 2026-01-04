// java/controller/utils/NumberUtils.java
package api.utils;

/**
 * Utility class to collect number based helper functions.
 */
public class NumberUtils {

    // Private Constructor (disallow instantiation)
    private NumberUtils() {}

    /**
     * Check if a non-empty String can legally be parsed to a {@link Long}.
     *
     * @param s String input to validate
     * @return {@code true} if valid, {@code false} if null, empty, or illegal
     */
    public static boolean isLong(String s) {
        if (s == null || s.isEmpty()) return false;
        try { Long.parseLong(s); return true; }
        catch (NumberFormatException e) { return false; }
    }
}
