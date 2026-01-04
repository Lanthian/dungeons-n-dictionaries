// features/feat/pages/FeatPage.tsx
import { useState } from 'react';
import FeatAPI from '../../../api/feat'
import type { Feat } from "../types/feat";
import Modal from '../../../components/Modal';
import { toastApiResponse } from '../../../utils/toastApiResponse';
import FeatForm from '../components/FeatForm';
import FeatView from '../components/FeatView';
import type { Column } from '../../../components/Table';
import { getFeatFull } from '../utils/getFeatFull';

export default function FeatPage() {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Feat | null>(null);
  const [reloadKey, setReloadKey] = useState<number>(0);

  async function onDelete(feat: Feat) {
    const res = await FeatAPI.deleteFeat(feat);
    if (toastApiResponse(res)) { setReloadKey(k => k+1); }
  }

  async function onSubmit(feat: Feat) {
    // POST or PUT depending on editing state
    const res = editing
      ? await FeatAPI.updateFeat(feat)
      : await FeatAPI.createFeat(feat);
    if (toastApiResponse(res)) {
      setShowModal(false);
      setEditing(null);
      setReloadKey(k => k+1);
    }
  }

  // Extra table columns to enable editing
  const cols: Column<Feat>[] = [
    { key: "edit", header: "", cell: f => (
      <button onClick={async () => {
        // Type assertions here for guarantees
        const featFull = await getFeatFull({feat: f, silentToast: true});
        if (featFull == undefined) return;
        setEditing(featFull);
        setShowModal(true);
      }}>
        Edit
      </button>
    )},
    { key: "delete", header: "", cell: f => (
      <button onClick={() => { onDelete(f) }}>Delete</button>
    )},
  ];

  return (
    <div>
      {/* Header */}
      <h1>Feats</h1>

      {/* Add Feat */}
      <button onClick={() => { setEditing(null); setShowModal(true); }}>
        Add Feat
      </button>

      {/* Feat Table */}
      <FeatView
        extraColumns={cols}
        reloadKey={reloadKey}
      />

      {/* FeatForm Modal (on top of layout) */}
      <Modal open={showModal} onClose={() => { setEditing(null); setShowModal(false); }}>
        <FeatForm
          initial={editing}
          onSubmit={onSubmit}
          onCancel={() => { setEditing(null); setShowModal(false); }}
        />
      </Modal>
    </div>
  )
}
