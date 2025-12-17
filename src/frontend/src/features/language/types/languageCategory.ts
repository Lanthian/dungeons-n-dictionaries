// features/language/types/languageCategory.ts
/**
 * Type and utility methods for formatting 'exotic' quality of a Language in
 * the frontend. Derived from boolean exotic parameter included in a Language.
 */
import type { Language } from "./language";

// --- Constants ---
export const LANGUAGE_CATEGORIES = ["Standard", "Exotic"] as const;

// --- Types ---
export type LanguageCategory = typeof LANGUAGE_CATEGORIES[number];

// --- Conversions ---
export function categoryFromExotic(exotic: boolean): LanguageCategory {
  return exotic ? 'Exotic' : 'Standard';
}

export function categoryFromLanguage(language: Language): LanguageCategory {
  return language.exotic ? 'Exotic' : 'Standard';
}

export function categoryToExotic(category: LanguageCategory): boolean {
  return category.toLowerCase() === 'exotic';
}
