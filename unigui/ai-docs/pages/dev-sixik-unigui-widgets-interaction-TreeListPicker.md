# `TreeListPicker` (extends `ComboBox`)

Generic dropdown picker binding domain objects to displayed text options.

- **Purpose:** Provides strongly typed selection over domain models without losing object references.

### USE WHEN

- Selecting complex model objects (players, entities, presets) in dropdowns.
- Custom label formatting for domain items.

### CRITICAL RULES & GOTCHAS:

- selectedValue() returns null if nothing is selected or list is empty.
- labelProvider determines the displayed string for each item.

- **Related:** `ComboBox`, `DropDownBox`

