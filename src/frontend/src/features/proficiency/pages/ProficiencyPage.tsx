// features/proficiency/pages/ProficiencyPage.tsx
import { useState } from 'react';
import ProficiencyAPI from '../../../api/proficiency'
import type { Column } from "../../../components/Table";
import { toastApiResponse } from '../../../utils/toastApiResponse';
import ProficiencyView from "../components/ProficiencyView";
import { PROFICIENCY_TYPES, type ProficiencyType, type Proficiency } from "../types/proficiency";
import Modal from '../../../components/Modal';
import ProficiencyForm from '../components/forms/ProficiencyForm';

export default function ProficiencyPage() {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Proficiency | null>(null);
  const [reloadKey, setReloadKey] = useState<number>(0);
  const [addType, setAddType] = useState<ProficiencyType>("TOOL");

  async function onDelete(proficiency: Proficiency) {
    const res = await ProficiencyAPI.deleteProficiency(proficiency);
    if (toastApiResponse(res)) { setReloadKey(k => k+1); }
  }

  async function onSubmit(proficiency: Proficiency) {
    // POST or PUT depending on editing state
    const res = editing
      ? await ProficiencyAPI.updateProficiency(proficiency)
      : await ProficiencyAPI.createProficiency(proficiency);
    if (toastApiResponse(res)) {
      setShowModal(false);
      setEditing(null);
      setReloadKey(k => k+1);
    }
  }

  // Extra table columns to enable editing
  const cols: Column<Proficiency>[] = [
    { key: "edit", header: "", cell: p => (
      <button onClick={() => { setEditing(p); setShowModal(true); }}>Edit</button>
    )},
    { key: "delete", header: "", cell: p => (
      <button onClick={() => { onDelete(p) }}>Delete</button>
    )},
  ];

  return (
    <div>
      {/* Header */}
      <h1>Proficiencies</h1>

      {/* Add Proficiency */}
      <select
        value={addType}
        onChange={e => setAddType(e.target.value as ProficiencyType)}
      >
        {PROFICIENCY_TYPES.map(p => (
          <option key={p} value={p}>{p}</option>
        ))}
      </select>
      <button onClick={() => { setEditing(null); setShowModal(true); }}>
        Add Proficiency
      </button>

      {/* Editable Proficiency View */}
      <ProficiencyView
        extraColumns={cols}
        reloadKey={reloadKey}
      />

      {/* ProficiencyForm Modal (on top of layout) */}
      <Modal open={showModal} onClose={() => { setEditing(null); setShowModal(false); }}>
        <ProficiencyForm
          initial={editing}
          createType={addType}
          onSubmit={onSubmit}
          onCancel={() => { setEditing(null); setShowModal(false); }}
        />
      </Modal>
    </div>
  )
}
