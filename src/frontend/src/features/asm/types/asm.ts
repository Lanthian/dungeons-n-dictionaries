// features/asm/types/asm.ts

export const ABILITIES = ["STR", "DEX", "CON", "INT", "WIS", "CHA"] as const;

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
