# `TabControl` (extends `LinearBox`)

Tabbed navigation container with a scrollable header bar and switched content area.

### API

- `TabControl()` — Constructs a new TabControl instance.
- `addTab(RichText, Widget)` → TabControl — Appends a new tab with RichText title and content widget.
- `addTab(String, Widget)` → TabControl — Appends a new tab with plain text title and content widget.
- `clearTabs()` → TabControl — Removes all tabs and their content.
- `contentHost()` → StackPanel — Returns the stack container holding tab page contents.
- `insertTab(int, RichText, Widget)` → TabControl — Inserts a new tab with RichText title at the specified index.
- `insertTab(int, String, Widget)` → TabControl — Inserts a new tab with plain text title at the specified index.
- `onSelectionChanged(EventListener<? super SelectionChangedEvent>)` → EventSubscription — Registers a listener invoked when the selected tab index changes.
- `removeTab(int)` → TabControl — Removes the tab at the specified index.
- `selectRelative(int)` → TabControl — Shifts tab selection by the relative delta offset.
- `selectTab(int)` → TabControl — Selects the tab at the specified index, firing a selection change event.
- `selectedContent()` → Widget — Returns the content widget of the selected tab, or null if none.
- `selectedIndex()` → int — Returns the 0-based index of the currently selected tab, or -1 if none.
- `selectedTab()` → Tab — Returns the currently selected Tab descriptor, or null if none.
- `silentSelectTab(int)` → TabControl (default: `-1`) — Silently selects the tab at the given index without emitting events.
- `tabButton(int)` → ToggleButton — Returns the toggle button corresponding to the tab at the given index.
- `tabCount()` → int — Returns the total number of tabs.
- `tabHeader()` → HBox — Returns the header container holding tab toggle buttons.
- `tabHeaderScroll()` → ScrollView — Returns the scroll view wrapping the tab header.
- `tabs()` → List<Tab> — Returns an unmodifiable list of tab descriptors.

