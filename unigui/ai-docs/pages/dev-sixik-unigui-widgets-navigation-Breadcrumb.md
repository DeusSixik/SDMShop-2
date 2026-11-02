# `Breadcrumb` (extends `PanelWidget`)

Hierarchical navigation breadcrumb trail displaying sequenced items with separators and selection callbacks.

### API

- `Breadcrumb()` — Constructs an empty Breadcrumb navigation bar.
- `addItem(BreadcrumbItem)` → Breadcrumb — Appends a BreadcrumbItem instance to the trail.
- `addItem(RichText)` → Breadcrumb — Appends a RichText formatted item to the trail.
- `addItem(String)` → Breadcrumb — Appends a plain text item to the trail.
- `breadcrumbItems(List<BreadcrumbItem>)` → Breadcrumb — Replaces the trail with BreadcrumbItem instances.
- `clearItems()` → Breadcrumb — Clears all items from the breadcrumb trail.
- `host()` → WrapPanel — Returns the internal WrapPanel container hosting the breadcrumb buttons.
- `itemButton(int)` → Button — Returns the Button widget representing the item at the specified index.
- `itemCount()` → int — Returns the total number of items in the breadcrumb trail.
- `items()` → List<BreadcrumbItem> — Returns an unmodifiable list of breadcrumb items.
- `items(List<String>)` → Breadcrumb — Replaces the trail with plain-text items.
- `lineSpacing(float)` → Breadcrumb (default: `2`) — Sets the vertical line spacing when items wrap to multiple lines.
- `onSelectionChanged(EventListener<? super SelectionChangedEvent>)` → EventSubscription — Registers a listener invoked when the selected breadcrumb step changes.
- `removeItem(int)` → Breadcrumb — Removes the breadcrumb item at the specified index.
- `richItems(List<RichText>)` → Breadcrumb — Replaces the trail with RichText formatted items.
- `selectedIndex()` → int — Returns the zero-based index of the currently selected breadcrumb item.
- `selectedIndex(int)` → Breadcrumb — Selects the item at the specified index and dispatches a selection changed event.
- `selectedItem()` → BreadcrumbItem — Returns the currently selected BreadcrumbItem, or null if none.
- `selectedPath()` → List<BreadcrumbItem> — Returns a sublist of items from index 0 up to the selected index inclusive.
- `separator()` → String — Returns the separator string placed between items.
- `separator(String)` → Breadcrumb (default: `›`) — Sets the separator string displayed between breadcrumb items.
- `silentSelectedIndex(int)` → Breadcrumb (default: `-1`) — Selects the item at the specified index without firing event callbacks.
- `spacing(float)` → Breadcrumb (default: `2`) — Sets the horizontal spacing between items and separators.

