# `PageView` (extends `PanelWidget`)

Retained page container that keeps exactly one page visible at a time.

### API

- `addPage(Widget)` → PageView — Appends a page widget to the end of the page list.
- `clearPages()` → PageView — Removes all pages from the container.
- `insertPage(int, Widget)` → PageView — Inserts a page widget at the specified index.
- `onSelectionChanged(EventListener<? super SelectionChangedEvent>)` → EventSubscription — Registers a listener invoked when the selected page index changes.
- `pageCount()` → int — Returns the total number of pages.
- `pages()` → List<Widget> — Returns an unmodifiable list of all contained page widgets.
- `removePage(Widget)` → PageView — Removes the specified page widget.
- `removePage(int)` → PageView — Removes the page at the specified index.
- `selectRelative(int)` → PageView — Shifts current page selection by the specified relative delta offset.
- `selectedIndex()` → int — Returns the 0-based index of the currently selected page.
- `selectedIndex(int)` → PageView — Selects the active page by index, firing a selection change event.
- `selectedPage()` → Widget — Returns the currently selected active page widget, or null if none.
- `silentSelectedIndex(int)` → PageView (default: `0`) — Silently selects a page index without emitting selection change events.

