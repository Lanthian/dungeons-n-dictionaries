// features/proficiency/components/ProficiencyList.tsx
import type { Proficiency } from "../types/proficiency";
import { formatProficiency } from "../utils/formatProficiency";

// Input properties for display
type Props = {
  proficiencies: Proficiency[];
};

export default function ProficiencyList({ proficiencies }: Props) {
  // Safety check
  if (proficiencies.length === 0) return <p>None</p>;

  return (
    // List out Proficiencies, unsorted
    <ul>
      {proficiencies.map((p,i) => (
        <li key={p.id ?? i}>
          {formatProficiency(p)}
        </li>
      ))}
    </ul>
  );
}
