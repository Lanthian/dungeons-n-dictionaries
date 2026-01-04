// features/feat/components/FeatView.tsx
import { useEffect, useState } from 'react';
import FeatAPI from '../../../api/feat'
import type { Feat } from "../types/feat";
import Table, { type Column } from "../../../components/Table";
import Modal from '../../../components/Modal';
import { getData, isSuccess, type ApiResponse } from '../../../api/apiResponse';
import { toastApiResponse } from '../../../utils/toastApiResponse';
import { getFeatFull } from '../utils/getFeatFull';
import FeatDisplay from './FeatDisplay';

// Input properties for FeatView
type Props = {
  extraColumns?: Column<Feat>[];
  reloadKey?: number;
  silentToast?: boolean;
}

/**
 * Reusable base view for Feats. Add smart behaviour via extra columns.
 */
export default function FeatView({
  extraColumns = [],
  reloadKey = 0,
  silentToast = false,
}: Props) {
  const [data, setData] = useState<Feat[]>([]);
  const [showFull, setShowFull] = useState<Feat | null>(null);

  // Fetch Feats upon mounting or signalling a reload
  useEffect(() => {
    async function load() {
      const res = await FeatAPI.fetchFeats();
      if (silentToast ? isSuccess(res) : toastApiResponse(res)) {
        setData(getData(res as ApiResponse<Feat[]>) || []);
      }
    }
    load();
  }, [reloadKey, silentToast]);

  // Table columns
  const cols: Column<Feat>[] = [
    { key: "name", header: "Name", cell: f => f.name },
    { key: "description", header: "Description", cell: f => f.description },
    { key: "view", header: "", cell: f => (
      <button onClick={async () => {
        // Type assertions here for guarantees
        const featFull = await getFeatFull({feat: f, silentToast: true});
        if (featFull == undefined) return;
        setShowFull(featFull);
      }}>
        View
      </button>
    )},
    ...extraColumns
  ];

  return (
    <div>
      {/* Table */}
      {data.length === 0 ? (
        <p>No Feats found</p>
      ) : (
        <Table
          rows={data}
          columns={cols}
        />
      )}

      {/* Full FeatDisplay Modal (on top of layout) */}
      <Modal open={showFull != null} onClose={() => setShowFull(null)}>
        <FeatDisplay data={showFull as Feat} />
      </Modal>
    </div>
  )
}
