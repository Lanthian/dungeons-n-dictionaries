// java/domain/modifiers/proficiency/ToolProficiency.java
package domain.modifiers.proficiency;

import domain.character.Character;
import domain.types.ProficiencyType;
import domain.types.ToolType;
import domain.utils.StringUtils;

/**
 * A {@link Proficiency} providing a {@link Character} with expertise regarding
 * a particular set of tools.
 *
 * <p> ToolProficiency {@code name} is the relevant tool's name,
 * {@code description} describes the tool.
 */
public class ToolProficiency extends Proficiency {

    // --- Attributes ---
    private final String name;
    private final String description;
    private ToolType type;

    // Constructor
    public ToolProficiency(String name, String description, ToolType toolType) {
        this.name = name;
        this.description = description;
        this.type = toolType;
    }

    // Overloaded Constructor (defaults Miscellaneous)
    public ToolProficiency(String name, String description) {
        this(name, description, ToolType.MISCELLANEOUS);
    }

    // Subtype specification
    @Override
    public ProficiencyType getProficiencyType() { return ProficiencyType.TOOL; }

    /* ======================================================================
     * ------------------------- Getters & Setters  -------------------------
     * ====================================================================== */

    // --- Getters ---
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public ToolType getType() { return this.type; }

    // --- Setters ---
    // public void setName(String val) { this.name = val; }
    // public void setDescription(String val) { this.description = val; }

    /* ======================================================================
     * --------------------------- Object Methods ---------------------------
     * ====================================================================== */

    @Override
    public String toString() {
        return StringUtils.toStringJoiner("ToolProficiency")
            .add("name=" + StringUtils.quote(name))
            .add("description=" + StringUtils.quote(description))
            .add("type=" + type)
            .toString();
    }
}
