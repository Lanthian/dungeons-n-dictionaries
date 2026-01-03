// features/feat/pages/FeatPage.tsx
import { useEffect, useState } from 'react';
import FeatAPI from '../../../api/feat'
import type { Feat } from "../types/feat";
import Table, { type Column } from "../../../components/Table";
import Modal from '../../../components/Modal';
import { getData } from '../../../api/apiResponse';
import { toastApiResponse } from '../../../utils/toastApiResponse';
import FeatForm from '../components/FeatForm';

export default function FeatPage() {
  const [data, setData] = useState<Feat[] | null>([]);
  const [showModal, setShowModal] = useState<boolean>(false);
  const [editing, setEditing] = useState<Feat | null>(null);

  async function refreshData() {
    // Fetch current data
    const res = await FeatAPI.fetchFeats();
    // If refresh fetch fails, set data to [] to avoid viewing stale data
    setData(getData(res) || []);
  }

  // Fetch feats upon navigating to this page
  useEffect(() => {
    async function load() {
      // Redeclared `refreshData()` to avoid cascading render warning
      const res = await FeatAPI.fetchFeats();
      if (toastApiResponse(res)) { setData(getData(res) || []) };
    }
    load();
  }, [])

  async function onDelete(feat: Feat) {
    const res = await FeatAPI.deleteFeat(feat);
    if (toastApiResponse(res)) { await refreshData(); }
  }

  /**
   * Retrieve a fully saturated Feat.
   *
   * @param proficiency A Feat object that has an ID set
   */
  async function getFull(feat: Feat) {
    const res = await FeatAPI.fetchFeat(feat.id as number);
    // TODO:
    if (toastApiResponse(res)) return getData(res);
  }

  async function onSubmit(feat: Feat) {
    // POST or PUT depending on editing state
    const res = editing
      ? await FeatAPI.updateFeat(feat)
      : await FeatAPI.createFeat(feat);
    if (toastApiResponse(res)) {
      setShowModal(false);
      setEditing(null);
      await refreshData();
    }
  }

  // Table columns
  const cols: Column<Feat>[] = [
    { key: "name", header: "Name", cell: f => f.name },
    { key: "description", header: "Description", cell: f => f.description },
    // { key: "proficiencies", header: "Proficiencies", cell: f => f.proficiencies },

    // TODO: Add a view button here

    { key: "edit", header: "", cell: f => (
      <button onClick={async () => {
        // Type assertions here for guarantees
        const featFull = await getFull(f);
        if (featFull == undefined) return;
        console.log(featFull);
        setEditing(featFull as Feat);
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
      {Array.isArray(data) && data.length !== 0 ? (
        <Table
          rows={data}
          columns={cols}
        />
      ) : (
        <p>No feats found</p>
      )}

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
