// components/Table.tsx
import type { ReactNode } from "react";

/**
 * Column type definition for use within a Table.
 */
export type Column<T> = {
  key: string;
  header: string;
  // Translation from row column data to a table ready format
  cell: (row: T) => ReactNode
};

// Input parameters for Table
type Props<T> = {
  rows: T[];
  columns: Column<T>[];
}

/**
 * Reusuable Table component. Utilises basic HTML Table Tags.
 */
export default function Table<T>({ rows, columns }: Props<T>) {
  return (
    <table>
      {/* Table header */}
      <thead>
        <tr>
          {columns.map(col => (
            <th key={col.key}>{col.header}</th>
          ))}
        </tr>
      </thead>
      {/* Table body */}
      <tbody>
        {rows.map((row, i) => (
          <tr key={i}>
            {columns.map(col => (
              <td key={col.key}>{col.cell(row)}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  )
}
