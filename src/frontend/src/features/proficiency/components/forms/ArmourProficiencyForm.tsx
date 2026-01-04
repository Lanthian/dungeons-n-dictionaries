// features/proficiency/components/forms/ArmourProficiencyForm.tsx
import { useEffect, useState } from "react";
import { ARMOUR_TYPES, type ArmourProficiency, type ArmourType } from "../../types/proficiency";
import { toTitleCase } from "../../../../utils/formatString";

// Input parameters for form
type Props = {
  initial: ArmourProficiency | null;
  onChange: (updated: ArmourProficiency) => void;
  onValidityChange: (valid: boolean) => void;
};

export default function ArmourProficiencyForm({ initial, onChange, onValidityChange }: Props) {
  // Variables
  const [type, setType] = useState<ArmourType>(initial?.type ?? ARMOUR_TYPES[0]);

  /**
   * Utility method to shorthand valid proficiency checking
   */
  function isValid(p: Partial<ArmourProficiency>): boolean {
    return !!p.type;
  }

  /* GPT generated section: update stored proficiency upon field change ..... */
  function update(next: Partial<ArmourProficiency>) {
    const updated: ArmourProficiency = {
      ...(initial?.id != null && { id: initial.id }),
      proficiencyType: "ARMOUR",
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
      <label>Type</label>
      <select
        value={type}
        onChange={e => {
          const next = e.target.value as ArmourType;
          setType(next);
          update({ type: next });
        }}
      >
        {ARMOUR_TYPES.map(a => (
          <option key={a} value={a}>{toTitleCase(a)}</option>
        ))}
      </select>
      <br />
    </>
  );
}
