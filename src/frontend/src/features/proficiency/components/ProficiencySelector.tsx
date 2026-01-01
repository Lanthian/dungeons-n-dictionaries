// features/proficiency/components/ProficiencySelector.tsx
import type { Proficiency } from "../types/proficiency";
import type { Column } from "../../../components/Table";
import ProficiencyView from "./ProficiencyView";

// Input parameters for ProficiencySelector
type Props = {
  selected: Proficiency[];
  onChange: (next: Proficiency[]) => void;
}

/**
 * Reusable UI Selector for Proficiencies.
 */
export default function ProficiencySelector({ selected, onChange }: Props) {

  function toggle(p: Proficiency) {
    onChange(
      // Remove ID from selection if already present, otherwise select it
      selected.some(x => x.id === p.id)
        ? selected.filter(x => x.id !== p.id)
        : [...selected, p]
    );
  }

  // Extra table columns to enable selecting
  const cols: Column<Proficiency>[] = [
    { key: "select", header: "", cell: p => (
      <input
        type="checkbox"
        checked={selected.some(x => x.id === p.id)}
        onChange={() => toggle(p)}
      />
    )},
  ];

  return (
    // Selectable Proficiency View
    <ProficiencyView
      extraColumns={cols}
      silentToast={true}
    />
  );
}
