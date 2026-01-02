// features/asm/components/AsmSelector.tsx
import type { Asm } from "../types/asm";
import type { Column } from "../../../components/Table";
import AsmView from "./AsmView";

// Input parameters for AsmSelector
type Props = {
  selected: Asm[];
  onChange: (next: Asm[]) => void;
}

/**
 * Reusable UI Selector for ASMs.
 */
export default function AsmSelector({ selected, onChange }: Props) {

  function toggle(a: Asm) {
    onChange(
      // Remove ID from selection if already present, otherwise select it
      selected.some(x => x.id === a.id)
        ? selected.filter(x => x.id !== a.id)
        : [...selected, a]
    );
  }

  // Extra table columns to enable selecting
  const cols: Column<Asm>[] = [
    { key: "select", header: "", cell: asm => (
      <input
        type="checkbox"
        checked={selected.some(x => x.id === asm.id)}
        onChange={() => toggle(asm)}
      />
    )},
  ];

  return (
    // Selectable ASM View
    <AsmView
      extraColumns={cols}
      silentToast={true}
    />
  );
}

