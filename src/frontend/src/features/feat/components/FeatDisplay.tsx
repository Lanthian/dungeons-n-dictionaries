// features/feat/components/FeatDisplay.tsx
import AsmList from "../../asm/components/AsmList";
import ProficiencyList from "../../proficiency/components/ProficiencyList";
import type { Feat } from "../types/feat";

// Input properties for display
type Props = {
  data: Feat;
  onClose?: () => void;
};

export default function FeatDisplay({ data, onClose }: Props) {
  // Safety check
  if (!data) return null;

  return (
    <>
      <h1>{data.name}</h1>
      <p><i>{data.description}</i></p>

      {/* ASMs */}
      <label>Ability Score Modifiers</label>
      <AsmList asms={data.abilityScoreModifiers ?? []} />

      {/* Proficiencies */}
      <label>Proficiencies</label>
      <ProficiencyList proficiencies={data.proficiencies ?? []} />

      {/* onClose dependent button - optional */}
      {onClose && (
        <div>
          <button type="button" onClick={onClose}>
            Close
          </button>
        </div>
      )}
    </>
  );
}