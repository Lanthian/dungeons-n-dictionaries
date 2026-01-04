// features/language/pages/LanguagePage.tsx
import { useState } from 'react';
import LanguageAPI from '../../../api/language'
import type { Language } from '../types/language';
import LanguageForm from '../components/LanguageForm';
import Modal from '../../../components/Modal';
import { toastApiResponse } from '../../../utils/toastApiResponse';
import type { Column } from '../../../components/Table';
import LanguageView from '../components/LanguageView';

export default function LanguagePage() {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Language | null>(null);
  const [reloadKey, setReloadKey] = useState<number>(0);

  async function onDelete(language: Language) {
    const res = await LanguageAPI.deleteLanguage(language);
    if (toastApiResponse(res)) { setReloadKey(k => k+1); }
  }

  async function onSubmit(language: Language) {
    // POST or PUT depending on editing state
    const res = editing
      ? await LanguageAPI.updateLanguage(language)
      : await LanguageAPI.createLanguage(language);
    if (toastApiResponse(res)) {
      setShowModal(false);
      setEditing(null);
      setReloadKey(k => k+1);
    }
  }

  // Extra table columns to enable editing
  const cols: Column<Language>[] = [
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

      {/* Editable Language View */}
      <LanguageView
        extraColumns={cols}
        reloadKey={reloadKey}
      />

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
