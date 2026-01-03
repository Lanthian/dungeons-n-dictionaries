// features/language/pages/LanguagePage.tsx
import { useEffect, useState } from 'react';
import LanguageAPI from '../../../api/language'
import { getData } from '../../../api/apiResponse';
import type { Language } from '../types/language';
import LanguageForm from '../components/LanguageForm';
import Modal from '../../../components/Modal';
import { toastApiResponse } from '../../../utils/toastApiResponse';
import Table, { type Column } from '../../../components/Table';
import { categoryFromLanguage } from '../types/languageCategory';

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

  // Table columns
  const cols: Column<Language>[] = [
    { key: "name", header: "Name", cell: l => l.name },
    { key: "description", header: "Description", cell: l => l.description ?? "N/A" },
    { key: "script", header: "Script", cell: l => l.script ?? "N/A" },
    { key: "exotic", header: "Category", cell: l => categoryFromLanguage(l) },
    { key: "edit", header: "", cell: l => (
      <button onClick={() => { setEditing(l); setShowModal(true); }}>Edit</button>
    )},
    { key: "delete", header: "", cell: l => (
      <button onClick={() => { onDelete(l) }}>Delete</button>
    )},
  ];

  return (
    <div>
      {/* Header */}
      <h1>Languages</h1>

      {/* Add Language */}
      <button onClick={() => { setEditing(null); setShowModal(true); }}>
        Add Language
      </button>

      {/* Language Table */}
      {Array.isArray(languages) && languages.length !== 0 ? (
        <Table
          rows={languages}
          columns={cols}
        />
      ) : (
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
