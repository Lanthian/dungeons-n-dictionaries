// src/domain/modifiers/proficiency/ToolProficiency.java
package domain.modifiers.proficiency;

import domain.Character;
import domain.core.Described;
import domain.types.ToolType;

/**
 * A {@link Proficiency} providing a {@link Character} with expertise regarding
 * a particular set of tools. 
 * 
 * <p> ToolProficiency {@code name} is the relevant tool's name, 
 * {@code description} describes the tool.
 */
public class ToolProficiency extends Described implements Proficiency {

    // --- Attributes ---
    private ToolType type;

    // Constructor
    public ToolProficiency(String name, String description, ToolType toolType) {
        super(name, description);
        this.type = toolType;
    }
    
    // Overloaded Constructor (defaults Miscellaneous)
    public ToolProficiency(String name, String description) {
        this(name, description, ToolType.MISCELLANEOUS);
    }

    // --- Getter ---
    public ToolType getType() { return this.type; }
}
