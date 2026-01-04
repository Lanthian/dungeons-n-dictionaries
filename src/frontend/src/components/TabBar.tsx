/** components/TabBar.tsx
 *
 * Developed in a private project, in collaboration with:
 * - Yuk Hang Cheng : https://github.com/SKYYKS-9998
 */
import type { ReactNode } from "react";

/**
 * A simple type defining the information a TabBar can utilise.
 */
export type TabItem = {
  key: string;
  label: string;
  render?: () => ReactNode;
}

// Input parameters for TabBar
type Props = {
  tabs: TabItem[]
  activeKey: string;
  onChange: (key: string) => void;
}

/**
 * Reusable TabItem collection component. Does not handle any actual tab
 * switching, instead offering an onChange hook.
 */
export default function TabBar({ tabs, activeKey, onChange }: Props) {
  return (
    <div className="tab-bar">
      {tabs.map((tab) => (
        <button
          key={tab.key}
          onClick={() => onChange(tab.key)}
          // Disable tab currently selected
          disabled={activeKey==tab.key}
        >
          {tab.label}
        </button>
      ))}
    </div>
  );
}
