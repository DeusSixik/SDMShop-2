---
title: "PanelWidget"
slug: "panel-widget"
category: "Widgets/Containers"
locale: "en"
order: 10
---

# PanelWidget

Base container for multi-child widgets managing hierarchy, deferred mutations, and rendering order.

## Overview

`PanelWidget` is the abstract foundation for all multi-child containers in UniGUI. It manages child widget ownership, assigns parent links, propagates `UIContext`, and handles deferred mutation queues so adding or removing children during event or render passes is safe.

By default, `PanelWidget` lays out ordinary children like a stack/overlay within its content bounds, while out-of-flow absolute children are positioned via `AbsoluteLayoutEngine`.

## Deferred Mutations & Snapshotting

- **Deferred Queue**: Mutations triggered via `addChild()`, `removeChild()`, and `clearChildren()` are queued and applied during `applyQueuedMutations()` before layout and render passes.
- **Array Snapshot**: Traversal passes use a cached `childSnapshot()` array, avoiding iterator allocations in hot tick and render loops.
- **Z-Order & Culling**: Children are sorted according to `ZOrder` and culled against scissor/render clip boundaries.

## Managing Children

```java
PanelWidget panel = new PanelWidget();
panel.addChild(new Label("Header"));
panel.addChild(new Button("Click Me"));
panel.removeChild(oldChild);
```

## Related links

- [StackPanel](<stack-panel.md>)
- [SurfaceWidget](<surface-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`addChild(Widget)` {: #addchildwidget data-toc-label="addChild(Widget)" }

**Add child**

Queues a child widget for addition at the end of the children list.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | Child widget to add |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`insertChild(int, Widget)` {: #insertchildint-widget data-toc-label="insertChild(int, Widget)" }

**Insert Child**

Enqueues insertion of a child widget at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | zero-based index in children list |
| `child` | `Widget` | Child widget to add |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`removeChild(Widget)` {: #removechildwidget data-toc-label="removeChild(Widget)" }

**Remove child**

Queues a child widget for removal from this container.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `Widget` | Child widget to remove |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`clearChildren()` {: #clearchildren data-toc-label="clearChildren()" }

**Clear children**

Queues removal and disposal of all child widgets.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`children()` {: #children data-toc-label="children()" }

**Child Widgets**

Returns the list of child widgets in layout/traversal order.

Returns `List`: ordered list of children.

</div>

