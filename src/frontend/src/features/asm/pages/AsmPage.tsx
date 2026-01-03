// features/asm/pages/AsmPage.tsx
import { useState } from 'react';
import AsmAPI from '../../../api/asm'
import type { Column } from "../../../components/Table";
import { toastApiResponse } from '../../../utils/toastApiResponse';
import AsmView from "../components/AsmView";
import type { Asm } from "../types/asm";
import Modal from '../../../components/Modal';
import AsmForm from '../components/AsmForm';

export default function AsmPage() {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Asm | null>(null);
  const [reloadKey, setReloadKey] = useState<number>(0);

  async function onDelete(asm: Asm) {
    const res = await AsmAPI.deleteAsm(asm);
    if (toastApiResponse(res)) { setReloadKey(k => k+1); }
  }

  async function onSubmit(asm: Asm) {
    // POST or PUT depending on editing state
    const res = editing
      ? await AsmAPI.updateAsm(asm)
      : await AsmAPI.createAsm(asm);
    if (toastApiResponse(res)) {
      setShowModal(false);
      setEditing(null);
      setReloadKey(k => k+1);
    }
  }

  // Extra table columns to enable editing
  const cols: Column<Asm>[] = [
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
      <h1>Ability Score Modifiers</h1>

      {/* Add ASM */}
      <button onClick={() => { setEditing(null); setShowModal(true); }}>
        Add ASM
      </button>

      {/* Editable ASM View */}
      <AsmView
        extraColumns={cols}
        reloadKey={reloadKey}
      />

      {/* AsmForm Modal (on top of layout) */}
      <Modal open={showModal} onClose={() => { setEditing(null); setShowModal(false); }}>
        <AsmForm
          initial={editing}
          onSubmit={onSubmit}
          onCancel={() => { setEditing(null); setShowModal(false); }}
        />
      </Modal>
    </div>
  )
}
