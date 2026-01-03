// features/proficiency/components/forms/ProficiencyForm.tsx
import { useState } from "react";
import type {
  ArmourProficiency,
  Proficiency,
  ProficiencyType,
  SkillProficiency,
  ToolProficiency
} from "../../types/proficiency";
import ArmourProficiencyForm from "./ArmourProficiencyForm";
import SkillProficiencyForm from "./SkillProficiencyForm";
import ToolProficiencyForm from "./ToolProficiencyForm";

// Input parameters for form
type Props = {
  initial: Proficiency | null;
  createType: ProficiencyType;
  onSubmit: (data: Proficiency) => void;
  onCancel: () => void;
};

export default function ProficiencyForm({
  initial,
  createType = "TOOL",
  onSubmit,
  onCancel
}: Props) {
  // Variables
  const proficiencyType: ProficiencyType = initial?.proficiencyType ?? createType;
  const [proficiency, setProficiency] = useState<Proficiency | null>(initial);
  const [valid, setValid] = useState<boolean>(false);

  // Delegate proficiency setting to subtype forms
  const handleChange = (updated: Proficiency) => {
    setProficiency(updated);
  };

  /* GPT generated section: assembling proficiency form on subtype .......... */
  let subtypeForm;
  switch (proficiencyType) {
    case "ARMOUR":
      subtypeForm = (
        <ArmourProficiencyForm
          initial={proficiency as ArmourProficiency | null}
          onChange={handleChange}
          onValidityChange={setValid}
        />
      );
      break;
    case "SKILL":
      subtypeForm = (
        <SkillProficiencyForm
          initial={proficiency as SkillProficiency | null}
          onChange={handleChange}
          onValidityChange={setValid}
        />
      );
      break;
    case "TOOL":
      subtypeForm = (
        <ToolProficiencyForm
          initial={proficiency as ToolProficiency | null}
          onChange={handleChange}
          onValidityChange={setValid}
        />
      );
      break;
  }
  /* ..................................................... End GPT disclaimer */

  function submit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (proficiency) onSubmit(proficiency);
  }

  return (
    <form className="form" onSubmit={submit}>
      <h1>{initial ? "Update" : "Add"} {proficiencyType} Proficiency</h1>

      {/* Proficiency Type dependent form */}
      {subtypeForm}

      {/* Interaction options */}
      <div>
        <button type="submit" disabled={!valid}>Submit</button>
        <button type="button" onClick={onCancel}>Cancel</button>
      </div>
    </form>
  );
}
