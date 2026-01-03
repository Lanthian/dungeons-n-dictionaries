// features/proficiency/components/forms/SkillProficiencyForm.tsx
import { useEffect, useState } from "react";
import { SKILLS, type SkillProficiency, type SkillType } from "../../types/proficiency";
import { formatUnderscore } from "../../../../utils/stringFormatting";

// Input parameters for form
type Props = {
  initial: SkillProficiency | null;
  onChange: (updated: SkillProficiency) => void;
  onValidityChange: (valid: boolean) => void;
};

export default function SkillProficiencyForm({ initial, onChange, onValidityChange }: Props) {
  // Variables
  const [skill, setSkill] = useState<SkillType>(initial?.skill ?? SKILLS[0]);

  /**
   * Utility method to shorthand valid proficiency checking
   */
  function isValid(p: Partial<SkillProficiency>): boolean {
    return !!p.skill;
  }

  /* GPT generated section: update stored proficiency upon field change ..... */
  function update(next: Partial<SkillProficiency>) {
    const updated: SkillProficiency = {
      ...(initial?.id != null && { id: initial.id }),
      proficiencyType: "SKILL",
      skill,
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
      <label>Skill</label>
      <select
        value={skill}
        onChange={e => {
          const next = e.target.value as SkillType;
          setSkill(next);
          update({ skill: next });
        }}
      >
        {SKILLS.map(s => (
          <option key={s} value={s}>{formatUnderscore(s)}</option>
        ))}
      </select>
      <br />
    </>
  );
}
