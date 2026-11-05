# `OverlayDismissable`

Contract for overlay widgets that an OverlayLayer dismisses when the pointer is pressed outside them.

- **Purpose:** Lets custom floating widgets get the same click-away behavior as popups and context menus.

### USE WHEN

- Implement it on a custom overlay added through `OverlayLayer.addOverlay`.

### CRITICAL RULES & GOTCHAS:

- `isInteractionTarget` must return true for the overlay itself and all its descendants.
- The layer only calls `dismiss` while `isOpenOverlay` and `dismissOnOutsideClick` are both true.

- **Related:** `OverlayLayer`, `Popup`, `ContextMenu`

