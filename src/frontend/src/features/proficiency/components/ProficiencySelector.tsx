// features/proficiency/components/ProficiencySelector.tsx
import type { Proficiency } from "../types/proficiency";
import ProficiencyView from "./ProficiencyView";
import {
  useSelectableColumn,
  type SelectableColumnParams
} from "../../../components/useSelectableColumn";

/**
 * Reusable UI Selector for Proficiencies.
 */
export default function ProficiencySelector({
  selected,
  onChange,
}: SelectableColumnParams<Proficiency>) {

  // Extra table column to enable selecting
  const col = useSelectableColumn({ selected, onChange })

  return (
    // Selectable Proficiency View
    <ProficiencyView
      extraColumns={[col]}
      silentToast={true}
    />
  );
}
