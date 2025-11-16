// src/domain/Background.java
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The attributes a {@link Character} Background has and provides in D&D.
 * TODO: Implement Choice<> mechanism from #2 for proficiencies and languages
 * TODO: Implement Setters for CharacterModifier attributes
 */
public class Background extends Detailed implements CharacterModifier {

    // --- Attributes ---
    private ArrayList<Language> languages;
    private ArrayList<Proficiency> proficiencies;

    // Constructor
    public Background(String name, String description) {
        super(name, description);
        this.languages = new ArrayList<>();
        this.proficiencies = new ArrayList<>();
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Language> getLanguages() { return this.languages; }
    
    @Override
    public List<Proficiency> getProficiencies() { return this.proficiencies; }
}
