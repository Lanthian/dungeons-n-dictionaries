// features/language/pages/LanguagePage.tsx
import { useEffect, useState } from 'react';
import LanguageAPI from '../../../api/language'
import { getData } from '../../../api/apiResponse';
import type { Language } from '../types/language';
import LanguageItem from '../components/LanguageItem';
import LanguageForm from '../components/LanguageForm';
import Modal from '../../../components/Modal';
import { toastApiResponse } from '../../../utils/toastApiResponse';

export default function LanguagePage() {
  const [languages, setLanguages] = useState<Language[] | null>([]);
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Language | null>(null);

  async function refreshLanguages() {
    // Fetch current languages
    const res = await LanguageAPI.fetchLanguages();
    // If refresh fetch fails, set languages to [] to avoid viewing stale data
    setLanguages(getData(res) || []);
  }

  // Fetch languages upon navigating to this page
  useEffect(() => {
    async function load() {
      // Redeclared `refreshLanguages()` to avoid cascading render warning
      const res = await LanguageAPI.fetchLanguages();
      if (toastApiResponse(res)) { setLanguages(getData(res) || []) };
    }
    load();
  }, [])

  async function onDelete(language: Language) {
    const res = await LanguageAPI.deleteLanguage(language);
    if (toastApiResponse(res)) { await refreshLanguages(); }
  }

  async function onSubmit(language: Language) {
    // POST or PUT depending on editing state
    const res = editing
      ? await LanguageAPI.updateLanguage(language)
      : await LanguageAPI.createLanguage(language);
    if (toastApiResponse(res)) {
      setShowModal(false);
      setEditing(null);
      await refreshLanguages();
    }
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
