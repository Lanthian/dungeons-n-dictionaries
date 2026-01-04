// features/proficiency/components/ProficiencyView.tsx
import { useEffect, useState } from "react";
import type { Column } from "../../../components/Table";
import type { Proficiency, ProficiencyType } from "../types/proficiency";
import { toastApiResponse } from "../../../utils/toastApiResponse";
import { getData, isSuccess, type ApiResponse } from "../../../api/apiResponse";
import { PROFICIENCY_REGISTRY } from "../proficiencyRegistry";
import type { TabItem } from "../../../components/TabBar";
import TabBar from "../../../components/TabBar";
import Table from "../../../components/Table";

// Input parameters for ProficiencyView
type Props = {
  extraColumns?: Column<Proficiency>[];
  reloadKey?: number;
  silentToast?: boolean;
}

/**
 * Reusable base view for Proficiencies. Add smart behaviour via extra columns.
 */
export default function ProficiencyView({
  extraColumns = [],
  reloadKey = 0,
  silentToast = false,
}: Props) {
  const [data, setData] = useState<Partial<Record<ProficiencyType, Proficiency[]>>>({});

  // Fetch proficiencies upon navigating to this page or signalling a reload
  useEffect(() => {
    PROFICIENCY_REGISTRY.forEach(async module => {
      const res = await module.fetch();
      // Cast response to silence typescript concerns from union type
      if (silentToast ? isSuccess(res as ApiResponse<Proficiency[]>) : toastApiResponse(res as ApiResponse<Proficiency[]>)) {
        setData(prev => ({
          ...prev,
          [module.proficiencyType]: getData(res as ApiResponse<Proficiency[]>)
        }))
      };
    });
  }, [reloadKey, silentToast]);

  // Construct tabs from known proficiency types registered
  const tabs: TabItem[] = PROFICIENCY_REGISTRY.map(module => ({
    key: module.proficiencyType,
    label: module.label,
  }));

  const [activeTab, setActiveTab] = useState<string>(tabs[0].key);

  const activeModule = PROFICIENCY_REGISTRY.find(
    m => m.proficiencyType === activeTab
  );
  // If no module found, bug here (safety check)
  if (!activeModule) return null;

  // Data to display
  const rows = data[activeModule.proficiencyType];

  // Table columns
  const cols = [
    ...activeModule.columns,
    ...extraColumns
  ] as Column<Proficiency>[];

  return (
    <div>
      {/* Tabs */}
      <TabBar
        tabs={tabs}
        activeKey={activeTab}
        onChange={(key) => setActiveTab(key)}
      />

      {/* Table */}
      {!rows ? (
        // Display loading message if rows haven't loaded
        <p>loading...</p>
      ) : (
        <Table
          rows={rows}
          columns={cols}
        />
      )}
    </div>
  )
}
