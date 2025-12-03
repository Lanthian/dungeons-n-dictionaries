// src/domain/character/CharacterProfile.java
package domain.character;

import domain.builders.AbstractBuilder;

/**
 * Specialised data store for profile information (personality & story) of a
 * {@link Character}.
 */
public class CharacterProfile {
    
    // --- Attributes ---
    private String backstory;
    private String traits;
    private String ideals;
    private String bonds;
    private String flaws;
    private String allies;
    private String deity;
    private String trinket;
    private String notes;

    /**
     * Builder pattern implementation for easy {@link CharacterProfile} 
     * construction. Create a new {@code Builder} object, call the relevant 
     * construction methods upon it, then finalise the process with the 
     * {@link #build()} method.
     */
    public static class Builder extends AbstractBuilder<CharacterProfile> {

        // --- Attributes ---
        // Simple attributes
        private String backstory;
        private String traits;
        private String ideals;
        private String bonds;
        private String flaws;
        private String allies;
        private String deity;
        private String trinket;
        private String notes;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder() {
            this.backstory = UNDEFINED;
            this.traits = UNDEFINED;
            this.ideals = UNDEFINED;
            this.bonds = UNDEFINED;
            this.flaws = UNDEFINED;
            this.allies = UNDEFINED;
            this.deity = UNDEFINED;
            this.trinket = UNDEFINED;
            this.notes = UNDEFINED;
        }

        // Build method
        public CharacterProfile build() { return new CharacterProfile(this); }

        /* ----------------------- Simple  Attributes ----------------------- */

        public Builder backstory(String backstory) { this.backstory = backstory; return this; }
        public Builder traits(String traits) { this.traits = traits; return this; }
        public Builder ideals(String ideals) { this.ideals = ideals; return this; }
        public Builder bonds(String bonds) { this.bonds = bonds; return this; }
        public Builder flaws(String flaws) { this.flaws = flaws; return this; }
        public Builder allies(String allies) { this.allies = allies; return this; }
        public Builder deity(String deity) { this.deity = deity; return this; }
        public Builder trinket(String trinket) { this.trinket = trinket; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
    }

    // Local Constructor
    private CharacterProfile(Builder builder) {
        this.backstory = builder.backstory;
        this.traits = builder.traits;
        this.ideals = builder.ideals;
        this.bonds = builder.bonds;
        this.flaws = builder.flaws;
        this.allies = builder.allies;
        this.deity = builder.deity;
        this.trinket = builder.trinket;
        this.notes = builder.notes;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getBackstory() { return this.backstory; }
    public String getTraits() { return this.traits; }
    public String getIdeals() { return this.ideals; }
    public String getBonds() { return this.bonds; }
    public String getFlaws() { return this.flaws; }
    public String getAllies() { return this.allies; }
    public String getDeity() { return this.deity; }
    public String getTrinket() { return this.trinket; }
    public String getNotes() { return this.notes; }

    // --- Setters ---
    public void setBackstory(String backstory) { this.backstory = backstory; }
    public void setTraits(String traits) { this.traits = traits; }
    public void setIdeals(String ideals) { this.ideals = ideals; }
    public void setBonds(String bonds) { this.bonds = bonds; }
    public void setFlaws(String flaws) { this.flaws = flaws; }
    public void setAllies(String allies) { this.allies = allies; }
    public void setDeity(String deity) { this.deity = deity; }
    public void setTrinket(String trinket) { this.trinket = trinket; }
    public void setNotes(String notes) { this.notes = notes; }
}
