---
title: "WorldLayer"
slug: "world-layer"
category: "Core/Widgets/World"
locale: "en"
order: 0
---

# WorldLayer

Functional contract for background or decorative rendering layers drawn inside [WorldCanvas](<../../../widgets/world/world-canvas.md>).

## Overview

`WorldLayer` is a functional rendering interface for decorative, background, or data-driven visual layers inside `WorldCanvas`.

Usage:
- Ideal for coordinate grids, background terrain textures, route ribbons, heatmaps, and debug geometries.
- Invoked during the canvas rendering pass with access to the host `WorldCanvas` and `DrawScope`.
- Translates world coordinates to screen root coordinates using `canvas.worldToRootX()` and `canvas.worldToRootY()`.

## Implementing Custom World Render Layer

```java
canvas.addWorldLayer((world, draw) -> {
    float x = world.worldToRootX(100.0f);
    float y = world.worldToRootY(200.0f);
    draw.addCircleFilled(x, y, 6.0f, MutableColor.ofRgb(0xFF0000));
});
```

## Related links

- [RibbonLayer](<ribbon-layer.md>): World canvas layer rendering textured or solid ribbon lines, paths, and waypoint routes in world coordinates.
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldLayer::</span>`render(WorldCanvas, DrawScope)` {: #renderworldcanvas-drawscope data-toc-label="render(WorldCanvas, DrawScope)" }

Renders this world layer to the active frame draw scope.

| Parameter | Type | Description |
| --- | --- | --- |
| `canvas` | `WorldCanvas` | [WorldCanvas](<../../../widgets/world/world-canvas.md>) hosting and calling this world layer. |
| `draw` | `DrawScope` | [DrawScope](<../../render/draw-scope.md>) for recording frame render commands. |

</div>

