/** features/proficiency/view/columns.ts
 *
 * GPT generated file to define how Proficiency types are translated to UI in a
 * way that allows keeping design away from use location.
 */
import type { Column } from "../../../components/Table";
import { toTitleCase } from "../../../utils/formatString";
import type {
  ArmourProficiency,
  SkillProficiency,
  ToolProficiency,
} from "../types/proficiency";

export const armourColumns: Column<ArmourProficiency>[] = [
  { key: "type", header: "Type", cell: p => toTitleCase(p.type) },
];

export const skillColumns: Column<SkillProficiency>[] = [
  { key: "skill", header: "Skill", cell: p => toTitleCase(p.skill) },
];

export const toolColumns: Column<ToolProficiency>[] = [
  { key: "name", header: "Name", cell: p => p.name },
  { key: "description", header: "Description", cell: p => p.description },
  { key: "type", header: "Type", cell: p => toTitleCase(p.type) },
];
