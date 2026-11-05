# `NavigationView` (extends `TreeView`)

Sidebar navigation: collapsible categories of selectable items with full-width accent selection, built on TreeView.

- **Purpose:** Gives gallery-style left menus a real widget instead of hand-built button stacks.

### USE WHEN

- A sidebar or settings menu groups pages into collapsible categories and exactly one page is shown at a time.

### CRITICAL RULES & GOTCHAS:

- Category headers are never selected; clicking one only expands or collapses it.
- Item ids must be unique across the whole view.
- selectItem ignores unknown and disabled ids; selecting a valid item expands its category first.
- onItemSelected fires for item selection, not when selection is cleared.
- Put the view into a ScrollView when the list can be taller than its container.

- **Related:** `TreeView`, `ExpandablePanel`, `Accordion`

### API

- `NavigationView()` — Creates an empty navigation menu with category headers and selectable item rows.

### CATEGORIES

- `addCategory(String)` → NavigationCategory — Appends an expanded category to this navigation view.
- `addCategory(String, boolean)` → NavigationCategory — Appends a category and sets its initial expanded state.
- `clear()` → NavigationView — Removes all categories and items and clears the item lookup.

### ITEMS AND SELECTION

- `selectItem(String)` → NavigationView — Selects an enabled item by id, expanding its category first.
- `onItemSelected(Consumer<? super NavigationItem>)` → EventSubscription — Subscribes to item selection changes; no callback is made when selection is cleared.

### APPEARANCE

- `categoryColor(ColorView)` → NavigationView — Sets the background color used to paint category header rows; null leaves the current color unchanged.
- `categoryTextColor(ColorView)` → NavigationView — Sets category header text color, refreshes category rows, and invalidates the view; null leaves the current color unchanged.

