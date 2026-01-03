// java/domain/utils/StringUtils.java
package domain.utils;

import java.util.StringJoiner;

/**
 * Utility class to abstract toString() method formatting away from case by case
 * development.
 */
public class StringUtils {

    // Private Constructor (disallow instantiation)
    private StringUtils() {}

    /**
     * Surround a String with quotation marks. Returns the raw text {@code null}
     * if String input is null.
     *
     * @param s String input to wrap
     * @return Wrapped or plain null String
     */
    public static String quote(String s) {
        return s == null ? "null" : '\'' + s + '\'';
    }

    /**
     * Prepare and return a default {@link StringJoiner} with standardised
     * formatting for toString usage.
     *
     * @param className Prelude to joined output - "className{...}"
     * @return StringJoiner with standardised delimiter, prefix & suffix
     */
    public static StringJoiner toStringJoiner(String className) {
        return new StringJoiner(", ", className + "{", "}");
    }
}
