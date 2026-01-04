// features/asm/components/AsmForm.tsx
import { useState } from "react";
import { ABILITIES, type Ability, type Asm } from "../../asm/types/asm";
import { toTitleCase } from "../../../utils/formatString";

// Input parameters for form
type Props = {
  initial: Asm | null;
  onSubmit: (data: Asm) => void;
  onCancel: () => void;
};

export default function AsmForm({ initial, onSubmit, onCancel }: Props) {
  // Variables
  const [ability, setAbility] = useState<Ability>(initial?.ability ?? ABILITIES[0]);
  const [value, setValue] = useState<number>(initial?.value ?? 1);

  const disabled = !ability || !value;

  function submit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    onSubmit({
      ...(initial?.id != null && { id: initial.id }),
      ability,
      value
    });
  }

  return (
    <form className="form" onSubmit={submit}>
      <h1>{initial ? "Update" : "Add"} Ability Score Modifier</h1>

      {/* User input fields */}
      <label>Ability</label>
      <select
        value={ability}
        onChange={e => setAbility(e.target.value as Ability)}
      >
        {ABILITIES.map(a => (
          <option key={a} value={a}>{toTitleCase(a)}</option>
        ))}
      </select>
      <br />

      <label>Value</label>
      <input
        type="number"
        value={value}
        onChange={e => setValue(Number(e.target.value))}
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
