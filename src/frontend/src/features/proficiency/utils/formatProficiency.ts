// features/proficiency/utils/formatProficiency.ts
import { formatUnderscore } from "../../../utils/stringFormatting";
import type { ArmourProficiency, Proficiency, SkillProficiency, ToolProficiency } from "../types/proficiency";

/**
 * Utility function to format a Proficiency of unknown type as a string.
 */
export function formatProficiency(p: Proficiency): string {
  switch (p.proficiencyType) {
    case "ARMOUR": {
      const a = p as ArmourProficiency;
      if (a.type === "SHIELD") return `${a.type}s`;
      return `${a.type} Armour`;
    }

    case "SKILL": {
      const s = p as SkillProficiency;
      return formatUnderscore(s.skill);
    }

    case "TOOL": {
      const t = p as ToolProficiency;
      return t.name;
    }

    default: {
      // This guard should never be reached - make sure all proficiency types
      //  can be displayed.
      const guard: never = p;
      return guard;
    }
  }
}
