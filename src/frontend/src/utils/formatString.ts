// utils/formatString.ts

/**
 * Utility method to format text into title case, removing underlines present.
 *
 * @param s raw string with underscores and any casing
 * @returns string in title case with underscores replaced with spaces
 */
export function toTitleCase(s: string): string {
  return s
    .toLowerCase()
    .split("_")
    .map(w => w.charAt(0).toUpperCase() + w.slice(1))
    .join(" ");
}
