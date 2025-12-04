// src/domain/utils/StringUtils.java
package domain.utils;

import java.util.StringJoiner;

/**
 * Utility class to abstract toString() method formatting away from case by case
 * development.
 */
public class StringUtils {

    /**
     * Surround
     * @param s
     * @return
     */
    public static String quote(String s) { 
        return '\'' + s + '\''; 
    }

    public static String quoteOrNull(String s) { 
        return s == null ? "null" : quote(s) ;
    }

    public static StringJoiner toStringJoiner(String className) {
        return new StringJoiner(", ", className + "{", "}");
    } 
}
