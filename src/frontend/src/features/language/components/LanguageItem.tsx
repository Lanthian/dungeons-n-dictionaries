// features/language/components/LanguageItem.tsx

import type { Language } from "../types/Language";

export default function LanguageItem(language: Language) {
  return (
    <div className="language-item">
      <div className="name">Name: {language.name}</div>
      <div className="script">Script: {language.script ?? "N/A"}</div>
      <div className="exotic">{language.exotic ? "Exotic" : "Standard"}</div>
      <div className="desc">Description: {language.description}</div>
    </div>
  );
};
