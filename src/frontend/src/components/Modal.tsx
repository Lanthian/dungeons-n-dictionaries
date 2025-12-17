// components/Modal.tsx
import { useEffect, type ReactNode } from "react";
import "./Modal.css";

// Input parameters for Modal
type Props = {
  open: boolean;
  onClose: () => void;
  children: ReactNode;
}

/**
 * Reusable Modal component for pop-up elements on JSX pages. Closes when
 * clicking outside focussed area.
 */
export default function Modal({ open, onClose, children }: Props) {

  /* GPT generated section: Disable background scroll while open............. */
  useEffect(() => {
    if (!open) return;

    const originalOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";

    return () => {
      document.body.style.overflow = originalOverflow;
    };
  }, [open]);
  /* ..................................................... End GPT disclaimer */

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
