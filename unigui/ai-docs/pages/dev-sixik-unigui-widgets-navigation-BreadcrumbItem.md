# `BreadcrumbItem`

Navigation item descriptor representing a step or crumb in a breadcrumb trail.

### API

- `BreadcrumbItem()` — Constructs an empty BreadcrumbItem.
- `BreadcrumbItem(RichText)` — Constructs a BreadcrumbItem with a RichText formatted label.
- `BreadcrumbItem(String)` — Constructs a BreadcrumbItem with a plain text label.
- `enabled()` → boolean — Returns true if this item is interactive.
- `enabled(boolean)` → BreadcrumbItem — Sets whether this item is interactive.
- `richText()` → RichText — Returns the RichText formatted label.
- `richText(RichText)` → BreadcrumbItem — Sets the RichText formatted label.
- `text()` → String — Returns the plain text label.
- `text(String)` → BreadcrumbItem — Sets the plain text label.
- `value()` → String — Returns the associated data payload or route string.
- `value(String)` → BreadcrumbItem — Sets the associated data payload or route string.

