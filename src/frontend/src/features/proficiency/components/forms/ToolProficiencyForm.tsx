// features/proficiency/components/forms/ToolProficiencyForm.tsx
import { useEffect, useState } from "react";
import { TOOL_TYPES, type ToolProficiency, type ToolType } from "../../types/proficiency";
import { formatUnderscore } from "../../../../utils/stringFormatting";

// Input parameters for form
type Props = {
  initial: ToolProficiency | null;
  onChange: (updated: ToolProficiency) => void;
  onValidityChange: (valid: boolean) => void;
};

export default function ToolProficiencyForm({ initial, onChange, onValidityChange }: Props) {
  // Variables
  const [name, setName] = useState<string>(initial?.name ?? "");
  const [desc, setDesc] = useState<string>(initial?.description ?? "");
  const [type, setType] = useState<ToolType>(initial?.type ?? "MISCELLANEOUS");

  /**
   * Utility method to shorthand valid proficiency checking
   */
  function isValid(p: Partial<ToolProficiency>): boolean {
    return !!p.name && !!p.type;
  }


  /* GPT generated section: update stored proficiency upon field change ..... */
  function update(next: Partial<ToolProficiency>) {
    const updated: ToolProficiency = {
      ...(initial?.id != null && { id: initial.id }),
      proficiencyType: "TOOL",
      name,
      description: desc,
      type,
      ...next,
    }

    onChange(updated);
    // Update validity of change/insert commit
    onValidityChange?.(isValid(updated));
  };
  /* ..................................................... End GPT disclaimer */

  // Verify if proficiency is valid to submit on component mount
  useEffect(() => {
    update({});
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <>
      {/* User input fields */}
      <label>Name</label>
      <input
        value={name}
        onChange={e => {
          const next = e.target.value;
          setName(next);
          update({ name: next });
        }}
      />
      <br />

      <label>Description</label>
      <input
        value={desc}
        onChange={e => {
          const next = e.target.value;
          setDesc(next);
          update({ description: next });
        }}
      />
      <br />

      <label>Type</label>
      <select
        value={type}
        onChange={e => {
          const next = e.target.value as ToolType;
          setType(next);
          update({ type: next });
        }}
      >
        {TOOL_TYPES.map(t => (
          <option key={t} value={t}>{formatUnderscore(t)}</option>
        ))}
      </select>
    </>
  );
}
