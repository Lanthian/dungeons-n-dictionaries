// features/asm/types/asm.ts

export const ABILITIES = ["STRENGTH", "DEXTERITY", "CONSTITUTION",
  "INTELLIGENCE", "WISDOM", "CHARISMA"] as const;

export type Ability = typeof ABILITIES[number];

/**
 * A type used to track all the information relevant to an ASM in the system.
 * Mimics backend data structure. `id` is optional to allow the posting of new
 * ability score modifiers.
 */
export type Asm = {
  id?: number;
  ability: Ability;
  value: number;
};

/**
 * Utility method to format an ASM value to emphasise it is a modifier.
 *
 * @param value numeric value of an Ability Score Modifier
 * @returns "+"/"-"{number} string format for value; no prefix if 0
 */
export function formatValue(value: number): string {
  if (value > 0) return "+" + value.toString();
  return value.toString();
}
