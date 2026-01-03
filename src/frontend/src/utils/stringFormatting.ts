// utils/stringFormatting.ts

/**
 * Utility method to format text to hide underscores.
 *
 * @param s raw string with underscores
 * @returns string with underscores replaced with spaces
 */
export function formatUnderscore(s: string): string {
  // Source - https://stackoverflow.com/a/56596217
  // Posted by Ajay Gupta, modified by community. See post 'Timeline' for
  //   change history
  // Retrieved 2026-01-03, License - CC BY-SA 4.0
  return s.split('_').join(' ');
}
