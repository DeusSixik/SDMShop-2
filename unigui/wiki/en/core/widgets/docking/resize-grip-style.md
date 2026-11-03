---
title: "ResizeGripStyle"
slug: "resize-grip-style"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# ResizeGripStyle

Styling and rendering interface for bottom-right window resize grips, including preset TRIANGLE and LINES patterns.

## Overview

`ResizeGripStyle` renders the corner grab handle used to resize floating windows.

### Built-in Styles

| Preset | Visual [Style](<../../styling/style.md>) |
| --- | --- |
| `ResizeGripStyle.TRIANGLE` | Solid or shaded corner triangle. |
| `ResizeGripStyle.LINES` | Diagonal textured grip ridges. |

## Custom Resize Grip Painter

```java
theme.resizeGrip(ResizeGripStyle.LINES);
```

## Related links

- [DockTheme](<dock-theme.md>): Styling theme for the docking system configuring palette colors ([DockColor](<dock-color.md>)), tab geometry, split handles, shadows, and window resize grips.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ResizeGripStyle::</span>`paint(DrawScope, float, float, float, float, ColorView)` {: #paintdrawscope-float-float-float-float-colorview data-toc-label="paint(DrawScope, float, float, float, float, ColorView)" }

Paints the custom resize grip into the provided drawing scope.

| Parameter | Type | Description |
| --- | --- | --- |
| `draw` | `DrawScope` | [DrawScope](<../../render/draw-scope.md>) used for 2D rendering. |
| `right` | `float` | Right edge X coordinate. |
| `bottom` | `float` | Bottom edge Y coordinate. |
| `size` | `float` | Grip size in pixels. |
| `cornerRadius` | `float` | Corner radius of the window. |
| `color` | `ColorView` | Color of the grip lines or indicator. |

</div>

