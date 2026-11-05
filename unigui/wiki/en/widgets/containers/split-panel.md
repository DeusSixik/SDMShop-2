---
title: "SplitPanel"
slug: "split-panel"
category: "Widgets/Containers"
locale: "en"
order: 130
---

# SplitPanel

Two-pane container separated by an interactive draggable splitter for dynamic resizing.

## Overview

SplitPanel manages two child widget slots: `first` and `second`. An internal interactive `Splitter` widget is positioned between them, allowing the user to resize the two sections via mouse dragging.

## Orientation & Constraints

The panel supports both horizontal (`Orientation.HORIZONTAL` - first slot left, second slot right) and vertical (`Orientation.VERTICAL` - first slot top, second slot bottom) splitting. Space division is specified by `splitRatio` (0.0 to 1.0) and constrained by `minFirstSize` and `minSecondSize`.

## Horizontal Split Panel

```java
SplitPanel split = new SplitPanel(sidebar, editor)
        .orientation(Orientation.HORIZONTAL)
        .splitRatio(0.28f)
        .minFirstSize(120.0f);
```

## Related links

- [DockPanel](<dock-panel.md>)
- [HBox](<hbox.md>)
- [PanelWidget](<panel-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`first(Widget)` {: #firstwidget data-toc-label="first(Widget)" }

**Set First Pane**

Sets the widget in the first slot (left or top).

| Parameter | Type | Description |
| --- | --- | --- |
| `first` | `Widget` | the first child widget |

Returns `SplitPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`second(Widget)` {: #secondwidget data-toc-label="second(Widget)" }

**Set Second Pane**

Sets the widget in the second slot (right or bottom).

| Parameter | Type | Description |
| --- | --- | --- |
| `second` | `Widget` | the second child widget |

Returns `SplitPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Set Orientation**

Sets the split axis orientation (horizontal or vertical).

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | the desired orientation |

Returns `SplitPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`splitRatio(float)` {: #splitratiofloat data-toc-label="splitRatio(float)" }

**Set Split Ratio**

Sets the fraction of available space allocated to the first slot (clamped to 0.0..1.0).

| Parameter | Type | Description |
| --- | --- | --- |
| `splitRatio` | `float` | split ratio between 0.0 and 1.0 |

Returns `SplitPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`minFirstSize(float)` {: #minfirstsizefloat data-toc-label="minFirstSize(float)" }

**Set Min First Size**

Sets the minimum pixel size for the first slot.

| Parameter | Type | Description |
| --- | --- | --- |
| `minFirstSize` | `float` | minimum size in pixels |

Returns `SplitPanel`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`minSecondSize(float)` {: #minsecondsizefloat data-toc-label="minSecondSize(float)" }

**Set Min Second Size**

Sets the minimum pixel size for the second slot.

| Parameter | Type | Description |
| --- | --- | --- |
| `minSecondSize` | `float` | minimum size in pixels |

Returns `SplitPanel`: this widget for fluent configuration.

</div>

