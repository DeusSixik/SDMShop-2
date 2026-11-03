---
title: "DockPanel"
slug: "dock-panel"
category: "Widgets/Containers"
locale: "en"
order: 120
---

# DockPanel

Edge-docking layout container arranging child widgets along outer boundaries (left, right, top, bottom).

## Overview

DockPanel sequentially lays out each child along one of the outer edges defined by `DockSide`. Each docked child consumes space along that side, shrinking the remaining rectangular area for subsequent children.

## Last Child Fill

When `lastChildFill` is true (default), the final child widget occupies the entire remaining interior area. For complex IDE docking (tear-off windows, tabs, split docking zones), use `DockingRoot` and `DockingManager` instead of `DockPanel`.

## Application Shell Layout

```java
DockPanel shell = new DockPanel();
shell.addChild(toolbar, DockSide.TOP);
shell.addChild(sidebar, DockSide.LEFT);
shell.addChild(mainView);
shell.lastChildFill(true);
```

## Related links

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)
- [SplitPanel](<split-panel.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPanel::</span>`addChild(Widget, DockSide)` {: #addchildwidget-dockside data-toc-label="addChild(Widget, DockSide)" }

**Add Docked Child**

Adds a child widget docked to a specific edge.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | the child widget |
| `dockSide` | `DockSide` | the edge to dock the child against |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPanel::</span>`dockSide(Widget, DockSide)` {: #docksidewidget-dockside data-toc-label="dockSide(Widget, DockSide)" }

**Set Dock Side**

Gets or sets the dock side for a child widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | the child widget |
| `dockSide` | `DockSide` | the edge to dock the child against |

Returns `DockPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockPanel::</span>`lastChildFill(boolean)` {: #lastchildfillboolean data-toc-label="lastChildFill(boolean)" }

**Set Last Child Fill**

Controls whether the last child expands to fill all remaining interior space.

| Parameter | Type | Description |
| --- | --- | --- |
| `lastChildFill` | `boolean` | true to make the last child fill remaining area |

Returns `DockPanel`: this widget for fluent configuration.

</div>

