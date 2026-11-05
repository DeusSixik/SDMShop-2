# `VirtualListView` (extends `WidgetBase`)

Virtualizes a large data set with switchable GRID and LIST presentation modes.

- **Purpose:** Displays only the visible portion of a model while preserving stable model indices in list and grid modes.

### USE WHEN

- The collection contains many similar records.
- The UI must switch between a list and responsive grid without changing the data model.

### CRITICAL RULES & GOTCHAS:

- `itemFactory` receives the absolute data-item index, not a grid-row index.
- Do not add children manually: content is created through `itemFactory` and managed by the virtualizer.
- Keep `itemCount` current; count and mode changes invalidate layout and visual state automatically.
- The offscreen cache retains widgets for the same index and does not reuse an instance for a different index.

- **Related:** `VirtualTableView`, `GridBox`, `ScrollView`

### CORE CONFIGURATION & BUILDER METHODS

- `viewMode(ViewMode)` → VirtualListView (default: `list`) — Switches virtual items between `LIST` and `GRID` presentation.
- `grid(boolean)` → VirtualListView — Convenience toggle: `true` enables `GRID`, while `false` selects `LIST`.
- `itemCount(int)` → VirtualListView (default: `0`) — Sets the total number of model items; `0` keeps the view empty.
- `itemFactory(IntFunction<? extends Widget>)` → VirtualListView — Creates a widget for the specified data index.
  - Purpose: Keeps the data model separate from temporarily realized UI items.
  - Constraints:
    - In GRID mode the index identifies an item, not a grid row; null restores the index-based Label factory.
- `gridCellWidth(float)` → VirtualListView (default: `96`) — Sets preferred cell width used to calculate the responsive column count.
- `gridCellHeight(float)` → VirtualListView (default: `96`) — Sets the fixed grid-row height in UI pixels.
- `gridColumns(int)` → VirtualListView (default: `0`) — Sets a fixed column count; `0` enables responsive fitting to the available width.
- `itemHeight(float)` → VirtualListView (default: `18`) — Sets the fixed list-row height in UI pixels.
- `overscan(int)` → VirtualListView (default: `1`) — Sets the number of extra rows realized before and after the visible viewport.
- `offscreenCacheSize(int)` → VirtualListView (default: `0`) — Limits offscreen widgets retained for the same index; `0` disables caching.

### SELECTION & NAVIGATION

- `selectionMode(SelectionMode)` → VirtualListView (default: `single`) — Sets `SINGLE` or `MULTIPLE` selection mode.
- `selectedIndex()` → int — Returns the selected index, or `-1` when no item is selected.
- `selectedIndices()` → List<Integer> — Returns the selected model indices.
- `selectIndex(int)` → VirtualListView — Selects an index and makes it active for navigation.
- `toggleIndex(int)` → VirtualListView — Toggles selection for the specified index.
- `clearSelection()` → VirtualListView — Clears all selected indices.
- `activeIndex()` → int — Returns the keyboard-navigation active index, or `-1`.
- `activeIndex(int)` → VirtualListView (default: `-1`) — Sets the keyboard-navigation active index and scrolls it into view.
- `onSelectionChanged(EventListener<? super SelectionChangedEvent>)` → EventSubscription — Registers a selection-change listener and returns a subscription used to unsubscribe.

### SCROLLING & POSITION

- `scrollY()` → float — Returns the current vertical scroll position in UI pixels.
- `scrollTo(float)` → VirtualListView — Sets vertical scroll offset, clamped to the available range.
- `scrollBy(float)` → VirtualListView — Changes vertical scroll offset relative to the current position.
- `scrollStep(float)` → VirtualListView (default: `16`) — Sets vertical scrolling in UI pixels per wheel/page delta unit.
- `scrollStep()` → float — Returns the configured scroll step in UI pixels.

### EXAMPLES

#### Responsive grid

```java
VirtualListView view = new VirtualListView()
        .viewMode(VirtualListView.ViewMode.GRID)
        .gridCellWidth(120.0f)
        .gridCellHeight(144.0f)
        .itemCount(entries.size())
        .itemFactory(index -> createEntryCard(entries.get(index)));

view.layout(style -> style
        .padding(12.0f)
        .columnGap(8.0f)
        .rowGap(12.0f));
```

