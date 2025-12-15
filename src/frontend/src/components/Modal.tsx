// components/Modal.tsx
import type { ReactNode } from "react";
import "./Modal.css";

// Input parameters for Modal
type Props = {
  open: boolean;
  onClose: () => void;
  children: ReactNode;
}

/**
 * Reusable Modal component for pop-up elements on JSX pages.
 */
export default function Modal({ open, onClose, children }: Props) {

  if (!open) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      {/* Disable backdrop click within modal area */}
      <div className="modal" onClick={e => e.stopPropagation()}>
        {children}
      </div>
    </div>
  );
}
