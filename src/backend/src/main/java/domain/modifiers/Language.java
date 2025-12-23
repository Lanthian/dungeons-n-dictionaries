// java/domain/modifiers/Language.java
package domain.modifiers;

import domain.core.Entity;
import domain.utils.StringUtils;

/**
 * Basic implementation of {@link Entity} for a Language a Character knows.
 * A language can either be standard (not-exotic) or exotic.
 *
 * <p> Language {@code description} describes typical speakers of the language.
 * {@code script} defines the written script of the language.
 */
public class Language extends Entity<Language> {

    // --- Attributes ---
    private final String name;
    private final String description;
    private String script;
    private boolean exotic;

    // Constructor
    public Language(String name, String description, String script, boolean exotic) {
        this.name = name;
        this.description = description;
        this.script = script;
        this.exotic = exotic;
    }

    // Overloaded Constructor (defaults standard)
    public Language(String name, String description, String script) {
        this(name, description, script, false);
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    // --- Getters ---
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public boolean isExotic() { return this.exotic; }
    public String getScript() { return this.script; }

    // --- Setters ---
    // public void setName(String val) { this.name = val; }
    // public void setDescription(String val) { this.description = val; }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

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
