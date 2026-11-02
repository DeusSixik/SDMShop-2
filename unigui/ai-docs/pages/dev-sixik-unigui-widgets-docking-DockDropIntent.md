# `DockDropIntent` (extends `Record`)

Resolved docking drop intention capturing the target area, preview bounds, and source/target pane identifiers.

### API

- `floating()` → boolean — Returns true if this intent detaches the pane into a floating window.
- `floating(String, float, float)` → DockDropIntent — Creates a floating tear-off drop intent around the pointer.
- `none()` → DockDropIntent — Returns an invalid empty drop intent representing no drop target.
- `of(String, String, DockArea, RectView, float, float)` → DockDropIntent — Creates a valid dock drop intent for the specified area and bounds.
- `split()` → boolean — Returns true if this intent splits an existing node.
- `tabbed()` → boolean — Returns true if this intent adds the pane as a tab.

