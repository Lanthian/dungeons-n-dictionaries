// features/proficiency/pages/ProficiencyPage.tsx
import { useEffect, useState } from "react";
import type { Proficiency, ProficiencyType } from "../types/proficiency";
import { PROFICIENCY_REGISTRY } from "../proficiencyRegistry";
import TabBar, { type TabItem } from "../../../components/TabBar";
import { toastApiResponse } from "../../../utils/toastApiResponse";
import { getData, type ApiResponse } from "../../../api/apiResponse";
import Table from "../../../components/Table";
// import type { ProficiencyType } from "../types/proficiency";

export default function ProficiencyPage() {
  const [data, setData] = useState<Partial<Record<ProficiencyType, Proficiency[]>>>({});

  // Fetch proficiencies upon navigating to this page
  useEffect(() => {
    PROFICIENCY_REGISTRY.forEach(async module => {
      const res = await module.fetch();
      if (toastApiResponse(res)) {
        setData(prev => ({
          ...prev,
          [module.proficiencyType]: getData(res as ApiResponse<Proficiency[]>)
        }))
      };
    });
  }, []);

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
          columns={activeModule.columns}
        />
      )}
    </div>
  )
}
