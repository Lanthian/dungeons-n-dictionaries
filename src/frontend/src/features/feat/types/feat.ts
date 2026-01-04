// features/feat/types/feat.ts
import type { Asm } from "../../asm/types/asm";
import type { Proficiency } from "../../proficiency/types/proficiency";

/**
 * A type used to track all the information relevant to a feat in the system.
 * Mimics backend data structure.
 */
export type Feat = {
  id?: number;
  name: string;
  description: string;
  // Supplied character modifications
  abilityScoreModifiers: Asm[];
  proficiencies: Proficiency[];
};
