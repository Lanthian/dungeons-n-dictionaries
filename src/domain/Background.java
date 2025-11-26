// src/domain/Background.java
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The attributes a {@link Character} Background has and provides in D&D.
 * TODO: Implement Choice<> mechanism from #2 for proficiencies and languages
 * TODO: Implement background variants (choice?)
 */
public class Background extends Detailed implements CharacterModifier {

    // --- Attributes ---
    // private final Background parentBackground;
    private final List<Language> languages;
    private final List<Proficiency> proficiencies;

    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for eased {@link Background} construction.
     * Create a new {@code Builder} object, call the relevant construction 
     * methods upon it, then finalise the process with the {@link #build()} 
     * method.
     */
    public static class Builder extends DetailedBuilder<Background> {

        // --- Attributes ---
        private List<Language> languages;
        private List<Proficiency> proficiencies;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder(String name, String description) {
            super(name, description);
            this.languages = new ArrayList<>();
            this.proficiencies = new ArrayList<>();
        }

        // Build method
        public Background build() { return new Background(this); }

        /* ---------------------- Foreign Associations ---------------------- */
        
        /** 
         * Replaces current {@link Language}s with provided list parameter.
         */
        public Builder languages(List<Language> languages) {
            this.languages = languages; return this;
        }

        /** 
         * Replaces current {@link Proficiency}s with provided list parameter.
         */
        public Builder proficiencies(List<Proficiency> proficiencies) {
            this.proficiencies = proficiencies; return this;
        }
    }

    // Local Constructor
    private Background(Builder builder) {
        // name, description & details copied from DetailedBuilder
        super(builder.name, builder.description);
        this.setDetails(builder.details);
        
        this.languages = builder.languages;
        this.proficiencies = builder.proficiencies;
    }

    /* ======================================================================
     * ------------------ CharacterModifier Implementation ------------------ 
     * ====================================================================== */

    @Override
    public List<Language> getLanguages() { return List.copyOf(this.languages); }
    
    @Override
    public List<Proficiency> getProficiencies() { return List.copyOf(this.proficiencies); }
}
