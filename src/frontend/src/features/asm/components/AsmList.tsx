// features/asm/components/AsmList.tsx
import { formatValue, type Asm } from "../types/asm";

// Input properties for display
type Props = {
  asms: Asm[];
};

export default function AsmList({ asms }: Props) {
  // Safety check
  if (asms.length === 0) return <p>None</p>;

  return (
    // List out ASMs, unsorted
    <ul>
      {asms.map((a,i) => (
        <li key={a.id ?? i}>
          {a.ability} {formatValue(a.value)}
        </li>
      ))}
    </ul>
  );
}
