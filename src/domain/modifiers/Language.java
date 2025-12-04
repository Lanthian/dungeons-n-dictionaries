// src/domain/modifiers/Language.java
package domain.modifiers;

import domain.core.Described;
import domain.utils.StringUtils;

/**
 * Basic implementation of {@link Described} for a Language a Character knows.
 * A language can either be standard (not-exotic) or exotic.
 * 
 * <p> Language {@code description} describes typical speakers of the language.
 * {@code script} defines the written script of the language.
 */
public class Language extends Described {

    // --- Attributes ---
    private String script;
    private boolean exotic;

    // Constructor
    public Language(String name, String description, String script, boolean exotic) {
        super(name, description);
        this.script = script;
        this.exotic = exotic;
    }

    // Overloaded Constructor (defaults standard)
    public Language(String name, String script, String description) {
        this(name, description, script, false);
    }

    // --- Getters ---
    public boolean isExotic() { return this.exotic; }
    public String getScript() { return this.script; }

    // To String
    @Override
    public String toString() {
        return StringUtils.toStringJoiner("Language")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("script=" + StringUtils.quote(script))
            .add("exotic=" + exotic)
            .toString();
    }
}
