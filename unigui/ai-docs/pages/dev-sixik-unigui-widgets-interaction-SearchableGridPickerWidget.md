# `SearchableGridPickerWidget` (extends `LinearBox`)

Abstract searchable dialog picker displaying items in a virtualized grid with preview, filter, and tooltip.

- **Purpose:** Provides a foundation for complex modal pickers (items, blocks, icons, emojis) with search and virtualized rendering.

### USE WHEN

- Building modal pickers for large catalogs of items or icons.
- Subclasses customize item rendering, search keys, and tooltip texts.

### CRITICAL RULES & GOTCHAS:

- VirtualListView handles efficient recycling of grid cell elements.
- Search query automatically filters entries in real time.

- **Related:** `VirtualListView`, `SearchField`, `WindowWidget`, `OverlayLayer`

