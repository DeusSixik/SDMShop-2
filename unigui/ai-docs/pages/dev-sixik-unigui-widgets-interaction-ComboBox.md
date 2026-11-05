# `ComboBox` (extends `LinearBox`)

Selection dropdown widget allowing picking one item from a list.

- **Purpose:** Provides a standard single-selection dropdown list with hover highlighting and popup overlay.

### USE WHEN

- Select one option from a fixed or dynamic list of text options.
- Quality, resolution, language, or preset selector menus.

### CRITICAL RULES & GOTCHAS:

- selectedIndex() returns -1 if nothing is selected.
- Selecting an option automatically closes the dropdown popup and emits selection events.

- **Related:** `DropDownBox`, `TreeListPicker`, `Button`, `Popup`

