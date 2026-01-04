// features/language/components/LanguageView.tsx
import { useEffect, useState } from "react";
import type { Column } from "../../../components/Table";
import type { Language } from "../types/language";
import { categoryFromLanguage } from "../types/languageCategory";
import { toastApiResponse } from "../../../utils/toastApiResponse";
import { getData, isSuccess, type ApiResponse } from "../../../api/apiResponse";
import LanguageAPI from "../../../api/language";
import Table from "../../../components/Table";

// Input parameters for LanguageView
type Props = {
  extraColumns?: Column<Language>[];
  reloadKey?: number;
  silentToast?: boolean;
}

/**
 * Reusable base view for Languages. Add smart behaviour via extra columns.
 */
export default function LanguageView({
  extraColumns = [],
  reloadKey = 0,
  silentToast = false,
}: Props) {
  const [data, setData] = useState<Language[]>([]);

  // Fetch Languages upon mounting or signalling a reload
  useEffect(() => {
    async function load() {
      const res = await LanguageAPI.fetchLanguages();
      if (silentToast ? isSuccess(res) : toastApiResponse(res)) {
        setData(getData(res as ApiResponse<Language[]>) || []);
      }
    }
    load();
  }, [reloadKey, silentToast]);

  // Table columns
  const cols: Column<Language>[] = [
    { key: "name", header: "Name", cell: l => l.name },
    { key: "description", header: "Description", cell: l => l.description ?? "N/A" },
    { key: "script", header: "Script", cell: l => l.script ?? "N/A" },
    { key: "exotic", header: "Category", cell: l => categoryFromLanguage(l) },
    ...extraColumns
  ];

  return (
    <div>
      {/* Table */}
      {data.length === 0 ? (
        <p>No Languages found</p>
      ) : (
        <Table
          rows={data}
          columns={cols}
        />
      )}
    </div>
  )
}
