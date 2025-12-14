// features/language/components/LanguageItem.tsx

import type { Language } from "../types/language";
import { categoryFromLanguage } from "../types/languageCategory";

export default function LanguageItem(language: Language) {
  return (
    <div className="language-item">
      <div className="name">Name: {language.name}</div>
      <div className="script">Script: {language.script ?? "N/A"}</div>
      <div className="exotic">{categoryFromLanguage(language)}</div>
      <div className="desc">Description: {language.description}</div>
    </div>
  );
};
