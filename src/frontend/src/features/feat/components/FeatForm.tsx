// features/feat/components/FeatForm.tsx
import { useState } from "react";
import type { Feat } from "../types/feat";
import ProficiencySelector from "../../proficiency/components/ProficiencySelector";
import AsmSelector from "../../asm/components/AsmSelector";
import type { Asm } from "../../asm/types/asm";
import type { Proficiency } from "../../proficiency/types/proficiency";

// Input parameters for form
type Props = {
  initial: Feat | null;
  onSubmit: (data: Feat) => void;
  onCancel: () => void;
};

export default function FeatForm({ initial, onSubmit, onCancel }: Props) {
  // Variables
  const [name, setName] = useState<string>(initial?.name ?? "");
  const [desc, setDesc] = useState<string>(initial?.description ?? "");
  const [asms, setAsms] = useState<Asm[]>(initial?.abilityScoreModifiers ?? []);
  const [profs, setProfs] = useState<Proficiency[]>(initial?.proficiencies ?? []);

  const disabled = !name || !desc;

  function submit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    onSubmit({
      ...(initial?.id != null && { id: initial.id }),
      name,
      description: desc,
      abilityScoreModifiers: asms,
      proficiencies: profs
    });
  }

  return (
    <form className="form" onSubmit={submit}>
      <h1>{initial ? "Update" : "Add"} Feat</h1>

      {/* User input fields */}
      <label>Name</label>
      <input
        value={name}
        onChange={e => setName(e.target.value)}
        placeholder="Name..."
      />
      <br />

      <label>Description</label>
      <input
        value={desc}
        onChange={e => setDesc(e.target.value)}
        placeholder="Description..."
      />
      <br />

      <label>Ability Score Modifiers</label>
      <AsmSelector
        selected={asms}
        onChange={setAsms}
      />
      <br />

      <label>Proficiencies</label>
      <ProficiencySelector
        selected={profs}
        onChange={setProfs}
      />
      <br />

      {/* Interaction options */}
      <div>
        <button type="submit" disabled={disabled}>Submit</button>
        <button type="button" onClick={onCancel}>Cancel</button>
      </div>
    </form>
  );
}
