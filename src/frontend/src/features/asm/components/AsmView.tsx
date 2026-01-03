// features/asm/components/AsmView.tsx
import { useEffect, useState } from "react";
import type { Column } from "../../../components/Table";
import { formatValue, type Asm } from "../types/asm";
import { toastApiResponse } from "../../../utils/toastApiResponse";
import { getData, isSuccess, type ApiResponse } from "../../../api/apiResponse";
import AsmAPI from "../../../api/asm";
import Table from "../../../components/Table";

// Input parameters for ASMView
type Props = {
  extraColumns?: Column<Asm>[];
  reloadKey?: number;
  silentToast?: boolean;
}

/**
 * Reusable base view for ASMs. Add smart behaviour via extra columns.
 */
export default function AsmView({
  extraColumns = [],
  reloadKey = 0,
  silentToast = false,
}: Props) {
  const [data, setData] = useState<Asm[]>([]);

  // Fetch ASMs upon mounting or signalling a reload
  useEffect(() => {
    async function load() {
      const res = await AsmAPI.fetchAsms();
      if (silentToast ? isSuccess(res) : toastApiResponse(res)) {
        setData(getData(res as ApiResponse<Asm[]>) || []);
      }
    }
    load();
  }, [reloadKey, silentToast]);

  // Table columns
  const cols: Column<Asm>[] = [
    { key: "ability", header: "Ability", cell: asm => asm.ability },
    { key: "value", header: "Value", cell: asm => formatValue(asm.value) },
    ...extraColumns
  ];

  return (
    <div>
      {/* Table */}
      {data.length === 0 ? (
        <p>No ASMs found</p>
      ) : (
        <Table
          rows={data}
          columns={cols}
        />
      )}
    </div>
  )
}
