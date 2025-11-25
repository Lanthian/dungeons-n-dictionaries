// src/domain/CharacterPhysique.java
package domain;

/**
 * Specialised data store for physical details and appearance information of a 
 * {@link Character}.
 */
public class CharacterPhysique {
    
    // --- Attributes ---
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String eyes;
    private String skin;
    private String hair;

    /* ======================================================================
     * -------------------------- Builder  Pattern -------------------------- 
     * ====================================================================== */

    /**
     * Builder pattern implementation for easy {@link CharacterPhysique} 
     * construction. Create a new {@code Builder} object, call the relevant 
     * construction methods upon it, then finalise the process with the 
     * {@link #build()} method.
     */
    public static class Builder {

        // --- Constants ---
        private final static String UNDEFINED = null;

        // --- Attributes ---
        // Simple attributes
        private String gender;
        private String age;
        private String height;
        private String weight;
        private String eyes;
        private String skin;
        private String hair;

        /* -------------------------- Construction -------------------------- */

        // Builder Constructor
        public Builder() {
            this.gender = UNDEFINED;
            this.age = UNDEFINED;
            this.height = UNDEFINED;
            this.weight = UNDEFINED;
            this.eyes = UNDEFINED;
            this.skin = UNDEFINED;
            this.hair = UNDEFINED;
        }

        // Build method
        public CharacterPhysique build() { return new CharacterPhysique(this); }

        /* ----------------------- Simple  Attributes ----------------------- */

        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder age(String age) { this.age = age; return this; }
        public Builder height(String height) { this.height = height; return this; }
        public Builder weight(String weight) { this.weight = weight; return this; }
        public Builder eyes(String eyes) { this.eyes = eyes; return this; }
        public Builder skin(String skin) { this.skin = skin; return this; }
        public Builder hair(String hair) { this.hair = hair; return this; }
    }

    // Local Constructor
    private CharacterPhysique(Builder builder) {
        this.gender = builder.gender;
        this.age = builder.age;
        this.height = builder.height;
        this.weight = builder.weight;
        this.eyes = builder.eyes;
        this.skin = builder.skin;
        this.hair = builder.hair;
    }

    /* ======================================================================
     * ------------------------- Getters & Setters  ------------------------- 
     * ====================================================================== */

    // --- Getters ---
    public String getGender() { return this.gender; }
    public String getAge() { return this.age; }
    public String getHeight() { return this.height; }
    public String getWeight() { return this.weight; }
    public String getEyes() { return this.eyes; }
    public String getSkin() { return this.skin; }
    public String getHair() { return this.hair; }

    // --- Setters ---
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(String age) { this.age = age; }
    public void setHeight(String height) { this.height = height; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setEyes(String eyes) { this.eyes = eyes; }
    public void setSkin(String skin) { this.skin = skin; }
    public void setHair(String hair) { this.hair = hair; }
}
