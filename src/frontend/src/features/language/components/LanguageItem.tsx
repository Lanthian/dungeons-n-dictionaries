// features/language/components/LanguageItem.tsx
import type { Language } from "../types/language";
import { categoryFromLanguage } from "../types/languageCategory";

// Input parameters for LanguageItem
type Props = {
  language: Language;
  onEdit?: () => void;
  onDelete?: () => void;
}

export default function LanguageItem({ language, onEdit, onDelete }: Props) {
  return (
    <div className="language-item">
      <div className="name">Name: {language.name}</div>
      <div className="script">Script: {language.script ?? "N/A"}</div>
      <div className="exotic">{categoryFromLanguage(language)}</div>
      <div className="desc">Description: {language.description ?? "N/A"}</div>
      {/* Interaction options */}
      {onEdit && <button onClick={() => onEdit()}>Edit</button>}
      {onDelete && <button onClick={() => onDelete()}>Delete</button>}
    </div>
  );
};
