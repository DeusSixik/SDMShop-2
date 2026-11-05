---
title: "DragGhost"
slug: "drag-ghost"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragGhost

Renderer for visual ghost representation of dragged elements displayed in the overlay layer beneath the cursor.

## Overview

`DragGhost` renders visual feedback underneath the pointer on the top-level overlay during drag gestures.

### Built-in Factories

| Method | Behavior |
| --- | --- |
| `DragGhost.widget(widget, opacity)` | Renders a clone of the source widget with custom opacity (e.g. 0.8f). |
| `DragGhost.text()` | Renders a styled badge containing `session.payload().preview()`. |

### Custom Rendering

Implement `render(RenderContext context, DragSession session)` to render arbitrary shapes, item icons, or multi-item stacks:

```java
DragGhost custom = (context, session) -> {
    context.rect(session.pointerX() + 8, session.pointerY() + 8, 32, 32, Paint.fill(ColorView.CYAN));
};
```

## Cloning a [Widget](<../../../widgets/widget.md>) as a Drag Ghost

```java
DragGhost ghost = DragGhost.widget(itemWidget, 0.8f);
```

## Related links

- [DragController](<drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragGhost::</span>`render(RenderContext, DragSession)` {: #renderrendercontext-dragsession data-toc-label="render(RenderContext, DragSession)" }

Renders the ghost visualization into the overlay render context.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Overlay render context. |
| `session` | `DragSession` | Active drag session. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragGhost::</span>`text()` {: #text data-toc-label="text()" }

Creates a lightweight text badge ghost displaying the payload preview text.

Returns `DragGhost`: A new DragGhost rendering a text badge..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragGhost::</span>`widget(Widget, float)` {: #widgetwidget-float data-toc-label="widget(Widget, float)" }

Creates a ghost that clones the cached rendering of a live widget with specified opacity.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | Source widget to clone appearance from. |
| `opacity` | `float` | Opacity level of the ghost clone. |

Returns `DragGhost`: A new DragGhost cloning the source widget..

</div>

