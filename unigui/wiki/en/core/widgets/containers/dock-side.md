---
title: "DockSide"
slug: "dock-side"
category: "Core/Widgets/Containers"
locale: "en"
order: 125
---

# DockSide

Specifies which outer boundary edge (LEFT, RIGHT, TOP, BOTTOM) a child widget docks against in a [DockPanel](<../../../widgets/containers/dock-panel.md>).

## Overview

`DockSide` defines the four peripheral boundary edges used by `DockPanel` to pin child widgets:

- **`LEFT`**: Pins the child against the left boundary of the remaining area.
- **`RIGHT`**: Pins the child against the right boundary.
- **`TOP`**: Pins the child against the top boundary.
- **`BOTTOM`**: Pins the child against the bottom boundary.

Note: `DockSide` is specific to the lightweight `DockPanel` layout container. For complete IDE-style docking (tear-off windows, tabs, docking drop targets), use `DockArea` and `DockingManager`.

## Docking to Edges

```java
dockPanel.addChild(toolbar, DockSide.TOP);
dockPanel.addChild(sidebar, DockSide.LEFT);
dockPanel.addChild(mainView);
dockPanel.lastChildFill(true);
```

## Related links

- [DockPanel](<../../../widgets/containers/dock-panel.md>)

