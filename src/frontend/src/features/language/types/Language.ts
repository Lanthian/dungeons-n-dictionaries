// features/language/types/Language.ts
/**
 * A type used to track all the information relevant to a language in the
 * system. Mimics backend data structure.
 */
export type Language = {
  id: string;
  name: string;
  description?: string;
  script?: string;
  exotic: boolean;
};
