// features/language/pages/LanguagePage.tsx
import { useEffect, useState } from 'react';
import LanguageAPI from '../../../api/language'
import type { Language } from '../types/language';
import LanguageItem from '../components/LanguageItem';
import LanguageForm from '../components/LanguageForm';
import Modal from '../../../components/Modal';

export default function LanguagePage() {
  const [languages, setLanguages] = useState<Language[] | null>([]);
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Language | null>(null);

  async function refreshLanguages() {
    // Fetch current languages
    const data = await LanguageAPI.fetchLanguages();
    setLanguages(data);
  }

  // Fetch languages upon navigating to this page
  useEffect(() => {
    async function load() {
      // Redeclared `refreshLanguages()` to avoid cascading render warning
      const data = await LanguageAPI.fetchLanguages();
      setLanguages(data);
    }
    load();
  }, [])

  async function onDelete(language: Language) {
    const res = await LanguageAPI.deleteLanguage(language);
    await refreshLanguages();
  }

  async function onSubmit(language: Language) {
    // POST or PUT depending on editing state
    const res = editing
      ? await LanguageAPI.updateLanguage(language)
      : await LanguageAPI.createLanguage(language);
    setShowModal(false);
    setEditing(null);
    await refreshLanguages();
  }

  return (
    <div>
      {/* Header */}
      <h1>Languages</h1>

      {/* Add Language */}
      <button onClick={() => { setEditing(null); setShowModal(true); }}>
        Add Language
      </button>

      {/* Language List */}
      {Array.isArray(languages) && languages.length !== 0 ? (
        languages?.map(it => (
          <LanguageItem
            key={it.id}
            language={it}
            onEdit={() => { setEditing(it); setShowModal(true); }}
            onDelete={() => onDelete(it)}
          />
        ))
      ) : (
        // Alternative text output if no languages returned
        <p>No languages found</p>
      )}

      {/* LanguageForm Modal (on top of layout) */}
      <Modal open={showModal} onClose={() => { setEditing(null); setShowModal(false); }}>
        <LanguageForm
          initial={editing}
          onSubmit={onSubmit}
          onCancel={() => { setEditing(null); setShowModal(false); }}
        />
      </Modal>
    </div>
  );
}
