// features/asm/components/AsmSelector.tsx
import type { Asm } from "../types/asm";
import AsmView from "./AsmView";
import {
  useSelectableColumn,
  type SelectableColumnParams
} from "../../../components/useSelectableColumn";

/**
 * Reusable UI Selector for ASMs.
 */
export default function AsmSelector({
  selected,
  onChange,
}: SelectableColumnParams<Asm>) {

  // Extra table column to enable selecting
  const col = useSelectableColumn({ selected, onChange })

  return (
    // Selectable Proficiency View
    <AsmView
      extraColumns={[col]}
      silentToast={true}
    />
  );
}
