---
title: "AnchorLayer"
slug: "anchor-layer"
category: "Core/Widgets/World"
locale: "en"
order: 0
---

# AnchorLayer

Layer managing screen-space UI widgets pinned to 3D/2D world coordinates inside [WorldCanvas](<../../../widgets/world/world-canvas.md>).

## Overview

`AnchorLayer` manages a collection of screen-space UI widgets pinned to world coordinates within a `WorldCanvas`.

Key responsibilities:
- Adding, looking up, and removing anchored widgets (`add`, `anchor`, `remove`, `clear`).
- Hosting interactive widgets (buttons, labels, panels) that maintain screen-space interactivity while following camera movement.
- Efficient snapshot caching for fast frame-by-frame rendering and event routing.

## Adding Anchored [Widget](<../../../widgets/widget.md>) to World Canvas

```java
AnchorWidget anchor = canvas.anchorLayer().add("flag", worldX, worldY, new Button("Waypoint"))
    .screenSize(80.0f, 24.0f)
    .pivot(0.5f, 1.0f);
```

## Related links

- [AnchorWidget](<anchor-widget.md>): Spatial anchor pinning a screen-space UI widget to 3D/2D world coordinates with zoom scaling, pivot, and viewport culling.
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`add(String, float, float, Widget)` {: #addstring-float-float-widget data-toc-label="add(String, float, float, Widget)" }

Adds a UI widget anchored to the specified world coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `worldX` | `float` | World X coordinate. |
| `worldY` | `float` | World Y coordinate. |
| `widget` | `Widget` | UI widget to anchor in world coordinate space. |

Returns `AnchorWidget`: The created or found [AnchorWidget](<anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`anchor(String)` {: #anchorstring data-toc-label="anchor(String)" }

Finds an [AnchorWidget](<anchor-widget.md>) by its string identifier, or returns null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |

Returns `AnchorWidget`: The created or found [AnchorWidget](<anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`anchors()` {: #anchors data-toc-label="anchors()" }

Returns an unmodifiable list of all active anchors in this layer.

Returns `List`: Unmodifiable list of active AnchorWidgets..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`clear()` {: #clear data-toc-label="clear()" }

Removes and detaches all anchors from this layer.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`remove(AnchorWidget)` {: #removeanchorwidget data-toc-label="remove(AnchorWidget)" }

Removes the specified [AnchorWidget](<anchor-widget.md>) from this layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `AnchorWidget` | [AnchorWidget](<anchor-widget.md>) instance to remove. |

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`remove(String)` {: #removestring data-toc-label="remove(String)" }

Removes an anchored widget by its string identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`size()` {: #size data-toc-label="size()" }

Returns the count of active anchors in this layer.

Returns `int`: The integer count value..

</div>

