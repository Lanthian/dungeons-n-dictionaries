// features/proficiency/proficiencyRegistry.ts
import { type ApiResponse } from "../../api/apiResponse";
import ProficiencyAPI from '../../api/proficiency';
import type { Column } from "../../components/Table";
import type {
  ArmourProficiency,
  ProficiencyType,
  SkillProficiency,
  ToolProficiency,
} from "./types/proficiency";
import {
  armourColumns,
  skillColumns,
  toolColumns,
} from "./view/columns";

/* -------------------------- Proficiency  Modules -------------------------- */

type ProficiencyModule<T> = {
  proficiencyType: ProficiencyType;
  label: string;
  fetch: () => Promise<ApiResponse<T[]>>;
  columns: Column<T>[];
}

type ArmourModule = ProficiencyModule<ArmourProficiency> & {
  proficiencyType: "ARMOUR";
}

type SkillModule = ProficiencyModule<SkillProficiency> & {
  proficiencyType: "SKILL";
}

type ToolModule = ProficiencyModule<ToolProficiency> & {
  proficiencyType: "TOOL";
}

/* -------------------------- Proficiency Registry -------------------------- */

/**
 * Union of all known proficiency modules.
 */
export type ProficiencyRegistryEntry =
  | ArmourModule
  | SkillModule
  | ToolModule;

/**
 * Registry of all proficiencies, their labels, API fetch methods and column
 * declarations for displaying within Tables.
 */
export const PROFICIENCY_REGISTRY: ProficiencyRegistryEntry[] = [
  {
    proficiencyType: "ARMOUR",
    label: "Armour",
    fetch: ProficiencyAPI.fetchArmourProficiencies,
    columns: armourColumns,
  },
  {
    proficiencyType: "SKILL",
    label: "Skills",
    fetch: ProficiencyAPI.fetchSkillProficiencies,
    columns: skillColumns,
  },
  {
    proficiencyType: "TOOL",
    label: "Tools",
    fetch: ProficiencyAPI.fetchToolProficiencies,
    columns: toolColumns,
  },
] as const;
