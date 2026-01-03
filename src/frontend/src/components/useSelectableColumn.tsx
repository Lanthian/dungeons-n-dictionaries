// components/useSelectableColumn.tsx
import { useCallback, useMemo } from "react";
import type { Column } from "./Table";

// Helper type to assert data has an ID to be compared on
type Identifiable = { id?: number };

// Input parameters for selectable column
export type SelectableColumnParams<T extends Identifiable> = {
  selected: T[];
  onChange: (next: T[]) => void;
}

/**
 * Reusable UI selectable column generator hook for table views.
 */
export function useSelectableColumn<T extends Identifiable>({
  selected,
  onChange,
}: SelectableColumnParams<T>): Column<T> {

  // Memoize toggle function to avoid recomputing unless dependency changes
  const toggle = useCallback((item: T) => {
    onChange(
      // Remove ID from selection if already present, otherwise select it
      selected.some(x => x.id === item.id)
        ? selected.filter(x => x.id !== item.id)
        : [...selected, item]
    );
  }, [selected, onChange]);


  // Extra table column to enable selecting
  return useMemo(() => (
    {
      key: "select",
      header: "",
      cell: (item: T) => (
        <input
          type="checkbox"
          checked={selected.some(x => x.id === item.id)}
          onChange={() => toggle(item)}
        />
      ),
    }
  ), [selected, toggle]);
}
