---
title: "CanvasWidget"
slug: "canvas-widget"
category: "Widgets/Display"
locale: "en"
order: 110
---

# CanvasWidget

Procedural drawing surface widget invoking custom [CanvasDrawCallback](<../../core/widgets/display/canvas-draw-callback.md>) painting routines.

## Overview

`CanvasWidget` provides a lightweight drawing canvas. Developers register callbacks that receive a `RenderContext` with current bounds to render custom procedural visuals, animations, or diagrams.

## Custom Canvas Drawing

```java
CanvasWidget canvas = new CanvasWidget()
        .onDraw(ctx -> {
            DrawScope scope = new DrawScope(ctx);
            scope.line(0, 0, 100, 100, 2.0f, Color.RED);
        });
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CanvasWidget::</span>`clearDrawCallbacks()` {: #cleardrawcallbacks data-toc-label="clearDrawCallbacks()" }

**Clear Draw Callbacks**

Removes all registered drawing callbacks.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CanvasWidget::</span>`onDraw(CanvasDrawCallback)` {: #ondrawcanvasdrawcallback data-toc-label="onDraw(CanvasDrawCallback)" }

**Register Draw Callback**

Registers a custom painting callback invoked on every render pass.

| Parameter | Type | Description |
| --- | --- | --- |
| `callback` | `CanvasDrawCallback` | — |

Returns `CanvasWidget`.

</div>

