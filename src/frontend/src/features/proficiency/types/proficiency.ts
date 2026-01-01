// features/proficiency/types/proficiency.ts

export const PROFICIENCY_TYPES = ["ARMOUR", "SKILL", "TOOL"] as const;

export type ProficiencyType = typeof PROFICIENCY_TYPES[number];

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
  type: string;
};

export interface SkillProficiency extends BaseProficiency {
  proficiencyType: "SKILL";
  skill: string;
};

export interface ToolProficiency extends BaseProficiency {
  proficiencyType: "TOOL";
  name: string;
  description: string;
  type: string;
};
