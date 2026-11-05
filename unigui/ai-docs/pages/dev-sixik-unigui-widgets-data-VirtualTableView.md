# `VirtualTableView` (extends `WidgetBase`)

High-performance virtualized grid/table view widget supporting column resizing, reordering, sorting, cell editing and dynamic row/column insertion.

- **Purpose:** Displays high-density tabular data with virtualized row recycling, customizable columns, and cell interactions.

### USE WHEN

- Presenting multi-column data sheets, inventory manifests, or log tables.
- Interactive tables requiring column sorting, moving, resizing or dynamic row/column management.

### CRITICAL RULES & GOTCHAS:

- Only rows within visible scroll viewport are measured and rendered.
- Columns list determines horizontal cell layout and header order.
- Cell providers receive the column modelIndex for data access, which stays stable across reorders.
- Row and column API indices refer to the current model or display order as stated by each operation; column reordering does not change modelIndex.
- Editing commits through commitEdit() and can be discarded through cancelEdit(); the widget does not mutate an external data model itself.

- **Related:** `VirtualTableColumn`, `VirtualListView`, `WidgetBase`

### ROWS AND COLUMNS

- `rowCount(int)` → VirtualTableView (default: `0`) — Sets the number of virtual rows; reducing the count clamps selection and cancels editing on removed rows.
- `insertRows(int, int)` → VirtualTableView — Inserts rows at a clamped model index and shifts selection and active-row state; sorting is reset.
- `columns(List<VirtualTableColumn>)` → VirtualTableView — Replaces the displayed column list; adopted columns receive stable model indices for provider access.

### SORTING

- `sortBy(int, SortDirection)` → VirtualTableView — Sorts by a displayed column using its configured sort-key provider and direction; NONE clears sorting.

### CELL EDITING

- `beginEdit(int, int)` → VirtualTableView — Begins editing a valid active cell when editing is enabled; an existing edit is committed first.
- `commitEdit()` → VirtualTableView — Commits the current editor text by emitting a cell-edit-committed event; the application remains responsible for updating its data model.

