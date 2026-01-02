// features/language/components/LanguageForm.tsx
import { useState } from "react";
import type { Language } from "../types/language";
import {
  categoryFromExotic,
  categoryToExotic,
  LANGUAGE_CATEGORIES,
  type LanguageCategory
} from "../types/languageCategory";

// Input parameters for LanguageForm
type Props = {
  initial: Language | null;
  onSubmit: (data: Language) => void;
  onCancel: () => void;
};

export default function LanguageForm({ initial, onSubmit, onCancel }: Props) {
  // Variables
  const [name, setName] = useState<string>(initial?.name ?? "");
  const [desc, setDesc] = useState<string | null>(initial?.description ?? null);
  const [script, setScript] = useState<string | null>(initial?.script ?? null);
  const [category, setCategory] = useState<LanguageCategory>(
    categoryFromExotic(initial?.exotic ?? false)
  );

  const disabled = !name;

  function submit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    onSubmit({
      id: initial?.id ?? -1,
      name,
      description: desc ?? null,
      script: script ?? null,
      exotic: categoryToExotic(category)
    });
  }

  return (
    <form className="form" onSubmit={submit}>
      <h1>{initial ? "Update" : "Add"} Language</h1>

      {/* User input fields */}
      <label>Name</label>
      <input
        value={name}
        onChange={e => setName(e.target.value)}
        placeholder="Name..."
      />
      <br />

      <label>Description</label>
      <input
        value={desc ?? ""}
        onChange={e => setDesc(e.target.value || null)}
        placeholder="Description..."
      />
      <br />

      <label>Script</label>
      <input
        value={script ?? ""}
        onChange={e => setScript(e.target.value || null)}
        placeholder="Script..."
      />
      <br />

      <label>Category</label>
      <select
        value={category}
        onChange={e => setCategory(e.target.value as LanguageCategory)}
      >
        {LANGUAGE_CATEGORIES.map(c =>
          <option key={c} value={c}>{c}</option>
        )}
      </select>
      <br />

      {/* Interaction options */}
      <div>
        <button type="submit" disabled={disabled}>Submit</button>
        <button type="button" onClick={onCancel}>Cancel</button>
      </div>
    </form>
  );
}
