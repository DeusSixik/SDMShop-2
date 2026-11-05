# `DrawScope`

Safe draw-list facade for widget renderers that translates high-level drawing calls into backend-neutral commands, manages transform pivot adjustments, and provides zero-allocation immediate primitives.

- **Purpose:** Decouples custom widget visual rendering from direct graphics API calls, enabling automatic command batching, multi-threading, retained render caching, and cross-backend parity.

### USE WHEN

- Implement custom procedural visuals inside a widget's `WidgetRender#render(Widget, DrawScope)` method.
- Draw standard geometric shapes, textured panels, gradients, vector paths, or procedural ribbons with automatic transform coordination.
- Execute zero-allocation drawing routines in high-framerate rendering loops using ColorView primitives.

### CRITICAL RULES & GOTCHAS:

- DrawScope records commands into the active RenderContext and DrawList; it does not issue immediate GPU draw calls.
- When created with widget layout bounds (`transformBounds`), DrawScope automatically offsets transform pivots so child visuals rotate/scale as a unified unit.
- Prefer `*Filled` and `*Stroke` overloads accepting direct `ColorView` in tight render loops to avoid temporary `Paint` allocations.
- Always balance `pushClip` or `pushTextClip` with exactly one `popClip()` call.
- When clipping text, prefer `pushTextClip` over standard `pushClip` to prevent clipping font antialiasing fringes.

- **Related:** `WidgetRender`, `RenderContext`, `DrawList`, `DrawCommand`, `Paint`, `Transform`, `NineSlice`, `VectorPath`, `TextureHandle`

### EXAMPLES

#### Draw a styled card with background and nine-slice header

```java
RectView b = widget.layoutBounds();
scope.roundedRectFilled(b.x(), b.y(), b.width(), b.height(), 6.0f, ColorView.rgb(32, 34, 37));
scope.roundedRectStroke(b.x(), b.y(), b.width(), b.height(), 6.0f, ColorView.rgb(55, 58, 64), 1.0f);
scope.nineSlice(headerSlice, b.x() + 1, b.y() + 1, b.width() - 2, 28.0f, Paint.NONE, true);
scope.pushTextClip(b.x() + 10, b.y() + 6, b.width() - 20, 18.0f);
try {
    scope.addText("Settings Card", b.x() + 10, b.y() + 6, b.width() - 20, 18.0f, ColorView.WHITE);
} finally {
    scope.popClip();
}
```

#### Draw procedural textured ribbon between points

```java
List<DrawPoint> path = List.of(new DrawPoint(20.0f, 40.0f), new DrawPoint(140.0f, 90.0f));
RibbonOptions options = RibbonOptions.defaults().closed(false).miterLimit(3.0f);
scope.ribbon(cableTexture, path, 6.0f, 32.0f, ColorView.rgb(0, 200, 255), options);
```

