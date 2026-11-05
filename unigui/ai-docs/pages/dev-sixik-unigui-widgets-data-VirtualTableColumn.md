# `VirtualTableColumn` (extends `Record`)

Column configuration for VirtualTableView defining header text, width, alignments, and overflow mode.

- **Purpose:** Configures column geometry, header rendering, and content alignment within a VirtualTableView.

### USE WHEN

- Adding columns to VirtualTableView.
- Customizing column text alignment or cell clipping behavior.

### CRITICAL RULES & GOTCHAS:

- width is clamped to a minimum of 1.0 pixel.
- Alignment and overflowMode default to START, CENTER, and CLIP respectively.

- **Related:** `VirtualTableView`, `Alignment`, `TextOverflowMode`

