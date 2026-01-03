// features/proficiency/types/proficiency.ts

export const PROFICIENCY_TYPES = ["ARMOUR", "SKILL", "TOOL"] as const;
export type ProficiencyType = typeof PROFICIENCY_TYPES[number];

export const ARMOUR_TYPES = ["LIGHT", "MEDIUM", "HEAVY", "SHIELD"] as const;
export type ArmourType = typeof ARMOUR_TYPES[number];

export const SKILLS = ["ACROBATICS", "ANIMAL_HANDLING", "ARCANA",
  "ATHLETICS", "DECEPTION", "HISTORY", "INSIGHT", "INTIMIDATION",
  "INVESTIGATION", "MEDICINE", "NATURE", "PERCEPTION", "PERFORMANCE",
  "PERSUASION", "RELIGION", "SLEIGHT_OF_HAND", "STEALTH", "SURVIVAL"] as const;
export type SkillType = typeof SKILLS[number];

export const TOOL_TYPES = ["ARTISAN_TOOLS", "GAMING_SET", "MUSICAL_INSTRUMENT",
  "MISCELLANEOUS"] as const;
export type ToolType = typeof TOOL_TYPES[number];

/**
 * Base interface extended by all concrete Proficiency types. `id` is optional
 * to allow the posting of new proficiencies. `proficiencyType` differentiates
 * between types, and is a key field for the backend to decipher which kind of
 * proficiency it is handling.
 */
interface BaseProficiency {
  id?: number;
  proficiencyType: ProficiencyType;
};

/**
 * Union of all known proficiencies modelled in the frontend.
 */
export type Proficiency =
  | ArmourProficiency
  | SkillProficiency
  | ToolProficiency;

export interface ArmourProficiency extends BaseProficiency {
  proficiencyType: "ARMOUR";
  type: ArmourType;
};

export interface SkillProficiency extends BaseProficiency {
  proficiencyType: "SKILL";
  skill: SkillType;
};

export interface ToolProficiency extends BaseProficiency {
  proficiencyType: "TOOL";
  name: string;
  description: string;
  type: ToolType;
};
