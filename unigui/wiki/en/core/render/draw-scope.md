---
title: "DrawScope"
slug: "draw-scope"
category: "Core/Render"
locale: "en"
order: 10
---

# DrawScope

Safe draw-list facade for widget renderers that translates high-level drawing calls into backend-neutral commands, manages transform pivot adjustments, and provides zero-allocation immediate primitives.

## Architecture & Command Recording

`DrawScope` is the primary drawing facade provided to custom widget renderers (`WidgetRender#render(Widget, DrawScope)`). It bridges immediate-mode procedural rendering with UniGUI's deferred draw-list architecture.

Instead of issuing immediate OpenGL, Vulkan, or Direct3D state mutations, `DrawScope` translates each method invocation into an immutable `DrawCommand` appended to the frame's `DrawList`. This decoupled design enables automatic draw-call batching, multi-threaded rendering, retained display caching, and hardware-neutral compatibility across all supported platforms.

| Drawing Subsystem | Primary Methods | Capabilities & Architecture |
| --- | --- | --- |
| **Standard Shapes** | `rect`, `roundedRect`, `circle`, `line`, `path` | High-level 2D geometric shapes styled using reusable `Paint` instances. |
| **Garbage-Free Primitives** | `rectFilled`, `rectStroke`, `circleFilled`, etc. | Zero-allocation drawing calls accepting raw `ColorView` parameters directly without creating temporary objects. |
| **Textures & Nine-Slice** | `texture`, `textureTinted`, `nineSlice` | Full-atlas UV mapping, rounded textures, and 9-slice expandable GUI panels. |
| **[Text](<../../widgets/display/text.md>) & Typography** | `text`, `addText` | Plain string and structured `RichText` typography with subpixel rasterization alignment. |
| **Direct Geometry & Meshes** | `addTriangle`, `addQuad`, `addMesh`, `ribbon` | Vertex-level arbitrary polygons, textured quads, custom indexed meshes, and procedural path ribbons. |
| **Vector Paths & Channels** | `pathLineTo`, `pathArcTo`, `pathBezier*`, `channelsSplit` | Interactive vector path construction and multi-channel draw-order layering. |
| **Clipping & Scissor** | `pushClip`, `pushTextClip`, `popClip` | Hardware-accelerated rectangular scissor regions with font-bleed compensation. |

!!! note "Immediate API, Deferred Execution"
    Calling drawing methods on `DrawScope` does not immediately render pixels to the screen. All commands are recorded in the active `RenderContext` and subsequently replayed, batched, and rendered by the active graphics backend.

## Transforms & Pivot Anchoring

When rendering custom widget graphics, child shapes and text must adhere to the widget's global transformations (translation, scale, rotation, skew, and pivot). `DrawScope` handles this coordination automatically.

### Automatic Pivot Adjustment (`transformFor`)

When a `DrawScope` is created with widget layout bounds (`transformBounds`), it calculates the relative offset between the widget's layout origin and each individual command's local coordinates.

```java
// Conceptual adjustment performed internally:
Transform adjusted = transform.copy();
adjusted.pivot().set(
        transform.pivot().x() + transformBoundsX - commandX,
        transform.pivot().y() + transformBoundsY - commandY);
```

This guarantees that all elements drawn inside the widget (such as background plates, icons, and text labels) rotate and scale as a unified visual unit around the widget's designated transform pivot, rather than rotating around their individual top-left coordinates.

### Nested Sub-Scopes

Use `withTransform(Transform)` to produce a lightweight child scope sharing the parent render context but applying a different local transformation (e.g. for spinning loading indicators or pulsating icons):

```java
DrawScope iconScope = scope.withTransform(spinningTransform);
iconScope.texture(iconTexture, 0, 0, 16, 16, paint);
```

## Garbage-Free Drawing Primitives

In high-performance Minecraft rendering running at 60 to 144+ FPS, allocating temporary objects during procedural drawing triggers frequent garbage collection pauses.

`DrawScope` provides two complementary drawing paradigms:

1. **Standard `Paint`-based Overloads (`rect`, `circle`, etc.)**: Convenient when styling parameters (fill, stroke, shader, filter) are encapsulated inside a reusable, cached `Paint` instance.
2. **Garbage-Free Overloads (`rectFilled`, `rectStroke`, `circleFilled`, etc.)**: Accept primitive float coordinates and `ColorView` instances directly. In standard UI rendering, these methods perform **zero heap allocations** per frame.

| Operation | Standard ([Paint](<paint.md>)) | Garbage-Free (ColorView) | Allocation Impact |
| --- | --- | --- | --- |
| **Solid Rectangle** | `scope.rect(x, y, w, h, paint)` | `scope.rectFilled(x, y, w, h, color)` | Zero allocations |
| **Rectangle Outline**| `scope.rect(x, y, w, h, paint)` | `scope.rectStroke(x, y, w, h, color, width)` | Zero allocations |
| **Rounded Rectangle**| `scope.roundedRect(..., paint)`| `scope.roundedRectFilled(..., color)` | Zero allocations |
| **Circle / Ellipse** | `scope.circle(x, y, w, h, paint)`| `scope.circleFilled(x, y, w, h, color)` | Zero allocations |
| **Textured Quad** | `scope.texture(tex, placement, ...)` | `scope.addTexturedQuad(tex, x1, y1, ...)` | Zero allocations |

> [!TIP]
> In widget render methods, always prefer the `*Filled` and `*Stroke` overloads with constant or pre-allocated `ColorView` instances for maximum framerate stability.

## Textures & Nine-Slice System

UniGUI features a comprehensive 2D texturing engine built directly into `DrawScope`.

### Nine-Slice Scalable Panels

Nine-slice (9-patch) textures allow rectangular UI panels of arbitrary dimensions to be drawn using a small source texture without distorting corner geometry or borders:

- **4 Fixed Corners**: Top-left, top-right, bottom-left, and bottom-right corners are rendered at their native 1:1 pixel scale without stretching.
- **4 Stretched Edges**: Top and bottom edges stretch horizontally; left and right edges stretch vertically.
- **Center Area**: Scaled across both horizontal and vertical axes, or left transparent when `fillCenter = false`.

```java
NineSlice borderSlice = NineSlice.of(windowTexture, 8, 8, 8, 8);
// Draws full window panel:
scope.nineSlice(borderSlice, 0, 0, width, height, Paint.NONE, true);
// Draws hollow selection outline:
scope.nineSlice(borderSlice, 0, 0, width, height, Paint.NONE, false);
```

### Proportional Overflow Protection

If the target width or height is smaller than the combined corner margins, `DrawScope` automatically scales corner borders down proportionally (`Math.min(srcLeft, width * 0.5f)`). This ensures the panel never clips or inverts its corners on small widget sizes.

## Vector Paths, Meshes & Ribbons

For custom procedural visuals, node graphs, map connections, and complex vector art, `DrawScope` offers rich path and polygon operations.

### Procedural Ribbons

The `ribbon(...)` method generates smooth, continuous textured ribbons along arbitrary polyline paths with automatic miter joints and butt caps:

- **Graph Connections**: Ideal for wiring, fluid pipes, and node-graph curves.
- **Texture Tiling**: When using textures with `REPEAT` wrap mode and specifying `textureLength` equal to the tile size, the texture repeats seamlessly along the ribbon length without elongation.

### Direct Vertex Polygons & Meshes

- **Polygons**: `addPolyline`, `addConvexPolyFilled`, and `addConcavePolyFilled` (using ear-clipping triangulation).
- **Meshes**: `addMesh(DrawMesh, TextureHandle)` allows precomputed indexed vertex meshes to be submitted in a single backend call.
- **Quads & Triangles**: `addQuadFilled`, `addTriangleFilled`, and `addRectFilledMultiColor` for smooth 4-corner color gradients.

## Clipping & Scissor Hierarchy

Clipping restricts drawing commands to a rectangular screen region, essential for scroll views, overflow containers, and text entry fields.

### Standard Scissor vs [Text](<../../widgets/display/text.md>) Clip Bleed

```java
// Standard rectangular clipping:
scope.pushClip(x, y, width, height);
// Render clipped content...
scope.popClip();
```

When rendering font glyphs, standard hardware scissor rectangles can clip subpixel antialiasing fringes or slightly oversized italic accents along line boundaries.

`DrawScope#pushTextClip` adds specialized micro-bleed margins (`bleedX = 1.0f`, `bleedTop = 1.0f`, `bleedBottom = 2.0f`) to prevent font cutoff while strictly maintaining containment for empty or zero-sized viewports.

!!! important "Always Balance Push and Pop"
    Every invocation of `pushClip(...)` or `pushTextClip(...)` must be paired with exactly one `popClip()` call, typically within a `try / finally` block:
    ```java
    scope.pushClip(bounds.x(), bounds.y(), bounds.width(), bounds.height());
    try {
        renderContent(scope);
    } finally {
        scope.popClip();
    }
    ```

## Draw a Styled Card with Background and Nine-Slice Header

```java
@Override
public void render(Widget widget, DrawScope scope) {
    RectView b = widget.layoutBounds();
    
    // 1. Draw rounded drop shadow and solid background
    scope.roundedRectFilled(b.x() + 2, b.y() + 2, b.width(), b.height(), 6.0f, ColorView.rgba(0, 0, 0, 80));
    scope.roundedRectFilled(b.x(), b.y(), b.width(), b.height(), 6.0f, ColorView.rgb(32, 34, 37));
    scope.roundedRectStroke(b.x(), b.y(), b.width(), b.height(), 6.0f, ColorView.rgb(55, 58, 64), 1.0f);
    
    // 2. Draw 9-slice header bar
    NineSlice headerSlice = NineSlice.of(cardHeaderTex, 4, 4, 4, 4);
    scope.nineSlice(headerSlice, b.x() + 1, b.y() + 1, b.width() - 2, 28.0f, Paint.NONE, true);
    
    // 3. Render clipped title text with protective bleed
    scope.pushTextClip(b.x() + 10, b.y() + 6, b.width() - 20, 18.0f);
    try {
        scope.addText("Settings Card", b.x() + 10, b.y() + 6, b.width() - 20, 18.0f, ColorView.WHITE);
    } finally {
        scope.popClip();
    }
}
```

## Draw Procedural Textured Ribbon Between Points

```java
List<DrawPoint> path = List.of(
        new DrawPoint(20.0f, 40.0f),
        new DrawPoint(80.0f, 40.0f),
        new DrawPoint(140.0f, 90.0f),
        new DrawPoint(200.0f, 90.0f)
);

// Draw continuous cable/pipe with miter joints
RibbonOptions options = RibbonOptions.defaults()
        .closed(false)
        .miterLimit(3.0f);

scope.ribbon(cableTexture, path, 6.0f, 32.0f, ColorView.rgb(0, 200, 255), options);
```

## API

### Scope & Transforms

| Method | Description |
| --- | --- |
| [`DrawScope(RenderContext, Transform)`](#drawscoperendercontext-transform) | Creates a drawing scope without anchor bounds for transform pivot. |
| [`DrawScope(RenderContext, Transform, RectView)`](#drawscoperendercontext-transform-rectview) | Creates a drawing scope with widget layout bounds used to maintain relative transform pivot anchoring. |
| [`context()`](#context) | Returns the underlying render context that receives draw commands. |
| [`transform()`](#transform) | Returns the current transform applied by this scope to all generated commands. |
| [`withTransform(Transform)`](#withtransformtransform) | Creates a nested scope targeting the same render context with a different transform. |

<div class="api-member-card" markdown="1">

#### `DrawScope(RenderContext, Transform)` {: #drawscoperendercontext-transform data-toc-label="DrawScope(RenderContext, Transform)" }

**Construct Scope**

Creates a drawing scope without anchor bounds for transform pivot.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext receiving the recorded drawing commands. |
| `transform` | `Transform` | [Widget](<../../widgets/widget.md>) Transform applied to commands recorded within the scope. |

</div>

<div class="api-member-card" markdown="1">

#### `DrawScope(RenderContext, Transform, RectView)` {: #drawscoperendercontext-transform-rectview data-toc-label="DrawScope(RenderContext, Transform, RectView)" }

**Construct Anchored Scope**

Creates a drawing scope with widget layout bounds used to maintain relative transform pivot anchoring.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | RenderContext receiving the recorded drawing commands. |
| `transform` | `Transform` | [Widget](<../../widgets/widget.md>) Transform applied to commands recorded within the scope. |
| `transformBounds` | `RectView` | Initial layout bounds of the widget used to anchor transform pivot. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`context()` {: #context data-toc-label="context()" }

**Get Render Context**

Returns the underlying render context that receives draw commands.

Returns `RenderContext`: Active render context instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`transform()` {: #transform data-toc-label="transform()" }

**Get Scope Transform**

Returns the current transform applied by this scope to all generated commands.

Returns `Transform`: Active Transform instance, or null if unscaled..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`withTransform(Transform)` {: #withtransformtransform data-toc-label="withTransform(Transform)" }

**Create Sub-Scope with Transform**

Creates a nested scope targeting the same render context with a different transform.

| Parameter | Type | Description |
| --- | --- | --- |
| `transform` | `Transform` | [Widget](<../../widgets/widget.md>) Transform applied to commands recorded within the scope. |

Returns `DrawScope`: New DrawScope sharing the parent context with the specified transform..

</div>

### Basic Shapes & Primitives

| Method | Description |
| --- | --- |
| [`rect(float, float, float, float, Paint)`](#rectfloat-float-float-float-paint) | Draws a rectangle styled with the specified [Paint](<paint.md>) instance. |
| [`roundedRect(float, float, float, float, float, Paint)`](#roundedrectfloat-float-float-float-float-paint) | Draws a rectangle with rounded corners using corner radius and [Paint](<paint.md>) styling. |
| [`circle(float, float, float, float, Paint)`](#circlefloat-float-float-float-paint) | Draws an ellipse or circle inscribed inside the specified bounding box. |
| [`line(float, float, float, float, Paint)`](#linefloat-float-float-float-paint) | Draws a straight line between two points using [Paint](<paint.md>) styling. |
| [`path(VectorPath, float, float, float, float, Paint)`](#pathvectorpath-float-float-float-float-paint) | Draws a reusable VectorPath fitted within target bounds using [Paint](<paint.md>) styling. |
| [`rectFilled(float, float, float, float, ColorView)`](#rectfilledfloat-float-float-float-colorview) | Draws a solid filled rectangle with zero allocations by passing ColorView directly. |
| [`rectStroke(float, float, float, float, ColorView, float)`](#rectstrokefloat-float-float-float-colorview-float) | Draws a stroked rectangle outline with zero allocations. |
| [`roundedRectFilled(float, float, float, float, float, ColorView)`](#roundedrectfilledfloat-float-float-float-float-colorview) | Draws a solid filled rounded rectangle with zero allocations. |
| [`roundedRectStroke(float, float, float, float, float, ColorView, float)`](#roundedrectstrokefloat-float-float-float-float-colorview-float) | Draws a stroked rounded rectangle outline with zero allocations. |
| [`circleFilled(float, float, float, float, ColorView)`](#circlefilledfloat-float-float-float-colorview) | Draws a solid filled circle or ellipse with zero allocations. |
| [`circleStroke(float, float, float, float, ColorView, float)`](#circlestrokefloat-float-float-float-colorview-float) | Draws a stroked circle or ellipse outline with zero allocations. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`rect(float, float, float, float, Paint)` {: #rectfloat-float-float-float-paint data-toc-label="rect(float, float, float, float, Paint)" }

**Draw Rectangle**

Draws a rectangle styled with the specified [Paint](<paint.md>) instance.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`roundedRect(float, float, float, float, float, Paint)` {: #roundedrectfloat-float-float-float-float-paint data-toc-label="roundedRect(float, float, float, float, float, Paint)" }

**Draw Rounded Rectangle**

Draws a rectangle with rounded corners using corner radius and [Paint](<paint.md>) styling.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`circle(float, float, float, float, Paint)` {: #circlefloat-float-float-float-paint data-toc-label="circle(float, float, float, float, Paint)" }

**Draw Circle / Ellipse**

Draws an ellipse or circle inscribed inside the specified bounding box.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`line(float, float, float, float, Paint)` {: #linefloat-float-float-float-paint data-toc-label="line(float, float, float, float, Paint)" }

**Draw Line**

Draws a straight line between two points using [Paint](<paint.md>) styling.

| Parameter | Type | Description |
| --- | --- | --- |
| `x1` | `float` | X coordinate of the first point. |
| `y1` | `float` | Y coordinate of the first point. |
| `x2` | `float` | X coordinate of the second point. |
| `y2` | `float` | Y coordinate of the second point. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`path(VectorPath, float, float, float, float, Paint)` {: #pathvectorpath-float-float-float-float-paint data-toc-label="path(VectorPath, float, float, float, float, Paint)" }

**Draw Vector [Path](<../../widgets/display/path.md>)**

Draws a reusable VectorPath fitted within target bounds using [Paint](<paint.md>) styling.

| Parameter | Type | Description |
| --- | --- | --- |
| `path` | `VectorPath` | Reusable vector path instance containing the shape contour. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`rectFilled(float, float, float, float, ColorView)` {: #rectfilledfloat-float-float-float-colorview data-toc-label="rectFilled(float, float, float, float, ColorView)" }

**Draw Filled Rectangle (Garbage-Free)**

Draws a solid filled rectangle with zero allocations by passing ColorView directly.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`rectStroke(float, float, float, float, ColorView, float)` {: #rectstrokefloat-float-float-float-colorview-float data-toc-label="rectStroke(float, float, float, float, ColorView, float)" }

**Draw Rectangle Outline (Garbage-Free)**

Draws a stroked rectangle outline with zero allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `strokeWidth` | `float` | Thickness of the stroke outline in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`roundedRectFilled(float, float, float, float, float, ColorView)` {: #roundedrectfilledfloat-float-float-float-float-colorview data-toc-label="roundedRectFilled(float, float, float, float, float, ColorView)" }

**Draw Filled Rounded Rectangle (Garbage-Free)**

Draws a solid filled rounded rectangle with zero allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`roundedRectStroke(float, float, float, float, float, ColorView, float)` {: #roundedrectstrokefloat-float-float-float-float-colorview-float data-toc-label="roundedRectStroke(float, float, float, float, float, ColorView, float)" }

**Draw Rounded Rectangle Outline (Garbage-Free)**

Draws a stroked rounded rectangle outline with zero allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `strokeWidth` | `float` | Thickness of the stroke outline in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`circleFilled(float, float, float, float, ColorView)` {: #circlefilledfloat-float-float-float-colorview data-toc-label="circleFilled(float, float, float, float, ColorView)" }

**Draw Filled Circle (Garbage-Free)**

Draws a solid filled circle or ellipse with zero allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`circleStroke(float, float, float, float, ColorView, float)` {: #circlestrokefloat-float-float-float-colorview-float data-toc-label="circleStroke(float, float, float, float, ColorView, float)" }

**Draw Circle Outline (Garbage-Free)**

Draws a stroked circle or ellipse outline with zero allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `strokeWidth` | `float` | Thickness of the stroke outline in local pixels. |

</div>

### Textures, Nine-Slice & Shaders

| Method | Description |
| --- | --- |
| [`texture(TextureHandle, float, float, float, float, Paint)`](#texturetexturehandle-float-float-float-float-paint) | Draws a texture stretched over destination bounds with [Paint](<paint.md>) tint. |
| [`texture(TextureHandle, TexturePlacement, float, Paint)`](#texturetexturehandle-textureplacement-float-paint) | Draws a texture using UV placement parameters and optional corner radius. |
| [`nineSlice(NineSlice, float, float, float, float, Paint)`](#nineslicenineslice-float-float-float-float-paint) | Draws a scalable 9-slice panel preserving corners and tiling/stretching borders and center. |
| [`nineSlice(NineSlice, float, float, float, float, Paint, boolean)`](#nineslicenineslice-float-float-float-float-paint-boolean) | Draws a 9-slice panel with an option to omit the central tile, leaving a hollow frame. |
| [`textureTinted(TextureHandle, float, float, float, float, ColorView)`](#texturetintedtexturehandle-float-float-float-float-colorview) | Draws a textured rectangle tinted with a direct ColorView without allocating [Paint](<paint.md>). |
| [`textureTinted(TextureHandle, TexturePlacement, float, ColorView)`](#texturetintedtexturehandle-textureplacement-float-colorview) | Draws a texture with UV placement, corner radius, and direct ColorView tint. |
| [`shader(ShaderHandle, float, float, float, float, ShaderUniforms, ShaderDrawOptions)`](#shadershaderhandle-float-float-float-float-shaderuniforms-shaderdrawoptions) | Executes a custom shader pass within the specified bounding rectangle with uniforms. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`texture(TextureHandle, float, float, float, float, Paint)` {: #texturetexturehandle-float-float-float-float-paint data-toc-label="texture(TextureHandle, float, float, float, float, Paint)" }

**Draw Texture**

Draws a texture stretched over destination bounds with [Paint](<paint.md>) tint.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`texture(TextureHandle, TexturePlacement, float, Paint)` {: #texturetexturehandle-textureplacement-float-paint data-toc-label="texture(TextureHandle, TexturePlacement, float, Paint)" }

**Draw Texture with Placement & Radius**

Draws a texture using UV placement parameters and optional corner radius.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `placement` | `TexturePlacement` | TexturePlacement defining destination rectangle and source UV bounds. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`nineSlice(NineSlice, float, float, float, float, Paint)` {: #nineslicenineslice-float-float-float-float-paint data-toc-label="nineSlice(NineSlice, float, float, float, float, Paint)" }

**Draw Nine-Slice Panel**

Draws a scalable 9-slice panel preserving corners and tiling/stretching borders and center.

| Parameter | Type | Description |
| --- | --- | --- |
| `slice` | `NineSlice` | [NineSlice](<../textures/nine-slice.md>) configuration defining texture margins and scale. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`nineSlice(NineSlice, float, float, float, float, Paint, boolean)` {: #nineslicenineslice-float-float-float-float-paint-boolean data-toc-label="nineSlice(NineSlice, float, float, float, float, Paint, boolean)" }

**Draw Nine-Slice with Hollow Center Option**

Draws a 9-slice panel with an option to omit the central tile, leaving a hollow frame.

| Parameter | Type | Description |
| --- | --- | --- |
| `slice` | `NineSlice` | [NineSlice](<../textures/nine-slice.md>) configuration defining texture margins and scale. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |
| `fillCenter` | `boolean` | Whether to draw the center tile (false leaves a hollow frame). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`textureTinted(TextureHandle, float, float, float, float, ColorView)` {: #texturetintedtexturehandle-float-float-float-float-colorview data-toc-label="textureTinted(TextureHandle, float, float, float, float, ColorView)" }

**Draw Tinted Texture (Garbage-Free)**

Draws a textured rectangle tinted with a direct ColorView without allocating [Paint](<paint.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`textureTinted(TextureHandle, TexturePlacement, float, ColorView)` {: #texturetintedtexturehandle-textureplacement-float-colorview data-toc-label="textureTinted(TextureHandle, TexturePlacement, float, ColorView)" }

**Draw Tinted Texture with Placement (Garbage-Free)**

Draws a texture with UV placement, corner radius, and direct ColorView tint.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `placement` | `TexturePlacement` | TexturePlacement defining destination rectangle and source UV bounds. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`shader(ShaderHandle, float, float, float, float, ShaderUniforms, ShaderDrawOptions)` {: #shadershaderhandle-float-float-float-float-shaderuniforms-shaderdrawoptions data-toc-label="shader(ShaderHandle, float, float, float, float, ShaderUniforms, ShaderDrawOptions)" }

**Draw with Custom Shader**

Executes a custom shader pass within the specified bounding rectangle with uniforms.

| Parameter | Type | Description |
| --- | --- | --- |
| `shader` | `ShaderHandle` | Handle to the compiled custom shader. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `uniforms` | `ShaderUniforms` | Shader uniform parameters passed to the program. |
| `options` | `ShaderDrawOptions` | Shader execution options or ribbon styling settings. |

</div>

### [Text](<../../widgets/display/text.md>) Rendering

| Method | Description |
| --- | --- |
| [`text(String, float, float, float, float, Paint)`](#textstring-float-float-float-float-paint) | Renders a plain text string within bounding bounds using [Paint](<paint.md>) styling. |
| [`text(RichText, float, float, float, float, Paint)`](#textrichtext-float-float-float-float-paint) | Renders a formatted [RichText](<../text/rich-text.md>) structure with pixel snapping enabled. |
| [`text(RichText, float, float, float, float, Paint, boolean)`](#textrichtext-float-float-float-float-paint-boolean) | Renders a formatted [RichText](<../text/rich-text.md>) structure with configurable glyph pixel snapping. |
| [`addText(String, float, float, float, float, ColorView)`](#addtextstring-float-float-float-float-colorview) | Records a plain text drawing command with direct ColorView tint and zero [Paint](<paint.md>) allocations. |
| [`addText(RichText, float, float, float, float, ColorView)`](#addtextrichtext-float-float-float-float-colorview) | Records a formatted [RichText](<../text/rich-text.md>) drawing command with direct ColorView tint. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`text(String, float, float, float, float, Paint)` {: #textstring-float-float-float-float-paint data-toc-label="text(String, float, float, float, float, Paint)" }

**Draw Plain [Text](<../../widgets/display/text.md>)**

Renders a plain text string within bounding bounds using [Paint](<paint.md>) styling.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string or [RichText](<../text/rich-text.md>) content to render. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`text(RichText, float, float, float, float, Paint)` {: #textrichtext-float-float-float-float-paint data-toc-label="text(RichText, float, float, float, float, Paint)" }

**Draw Rich [Text](<../../widgets/display/text.md>)**

Renders a formatted [RichText](<../text/rich-text.md>) structure with pixel snapping enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | [Text](<../../widgets/display/text.md>) string or [RichText](<../text/rich-text.md>) content to render. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`text(RichText, float, float, float, float, Paint, boolean)` {: #textrichtext-float-float-float-float-paint-boolean data-toc-label="text(RichText, float, float, float, float, Paint, boolean)" }

**Draw Rich [Text](<../../widgets/display/text.md>) with Snapping Option**

Renders a formatted [RichText](<../text/rich-text.md>) structure with configurable glyph pixel snapping.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | [Text](<../../widgets/display/text.md>) string or [RichText](<../text/rich-text.md>) content to render. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `paint` | `Paint` | [Paint](<paint.md>) styling parameters controlling fill, stroke, tint, and shader. |
| `textPixelSnap` | `boolean` | Whether to align glyph positions to integer display pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addText(String, float, float, float, float, ColorView)` {: #addtextstring-float-float-float-float-colorview data-toc-label="addText(String, float, float, float, float, ColorView)" }

**Add [Text](<../../widgets/display/text.md>) Command (Garbage-Free)**

Records a plain text drawing command with direct ColorView tint and zero [Paint](<paint.md>) allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string or [RichText](<../text/rich-text.md>) content to render. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addText(RichText, float, float, float, float, ColorView)` {: #addtextrichtext-float-float-float-float-colorview data-toc-label="addText(RichText, float, float, float, float, ColorView)" }

**Add Rich [Text](<../../widgets/display/text.md>) Command (Garbage-Free)**

Records a formatted [RichText](<../text/rich-text.md>) drawing command with direct ColorView tint.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | [Text](<../../widgets/display/text.md>) string or [RichText](<../text/rich-text.md>) content to render. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

### Direct Geometry & Meshes

| Method | Description |
| --- | --- |
| [`addDrawCmd(DrawCommand)`](#adddrawcmddrawcommand) | Appends a pre-built DrawCommand, automatically adjusting its transform pivot. |
| [`addCallback(CustomDraw)`](#addcallbackcustomdraw) | Inserts a custom drawing callback executed during backend command replay. |
| [`addLine(float, float, float, float, ColorView, float)`](#addlinefloat-float-float-float-colorview-float) | Appends a line command between two points with exact color and pixel thickness. |
| [`addRect(float, float, float, float, ColorView, float)`](#addrectfloat-float-float-float-colorview-float) | Appends a stroked rectangular frame with specified border thickness. |
| [`addRect(float, float, float, float, float, ColorView, float)`](#addrectfloat-float-float-float-float-colorview-float) | Appends a stroked rounded rectangle frame with corner radius and thickness. |
| [`addRectFilled(float, float, float, float, ColorView)`](#addrectfilledfloat-float-float-float-colorview) | Appends a solid rectangle fill command with direct ColorView tint. |
| [`addRectFilled(float, float, float, float, float, ColorView)`](#addrectfilledfloat-float-float-float-float-colorview) | Appends a solid rounded rectangle fill command with specified corner radius. |
| [`addRectFilledMultiColor(float, float, float, float, ColorView, ColorView, ColorView, ColorView)`](#addrectfilledmulticolorfloat-float-float-float-colorview-colorview-colorview-colorview) | Renders a rectangle with per-vertex color interpolation at each of the 4 corners. |
| [`addQuad(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView, float)`](#addquaddrawpoint-drawpoint-drawpoint-drawpoint-colorview-float) | Appends a stroked arbitrary four-sided polygon defined by four vertices. |
| [`addQuadFilled(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)`](#addquadfilleddrawpoint-drawpoint-drawpoint-drawpoint-colorview) | Appends a filled arbitrary quadrilateral defined by four vertices. |
| [`addTriangle(DrawPoint, DrawPoint, DrawPoint, ColorView, float)`](#addtriangledrawpoint-drawpoint-drawpoint-colorview-float) | Appends a stroked triangle outline defined by three points. |
| [`addTriangleFilled(DrawPoint, DrawPoint, DrawPoint, ColorView)`](#addtrianglefilleddrawpoint-drawpoint-drawpoint-colorview) | Appends a solid filled triangle defined by three points. |
| [`addCircle(float, float, float, ColorView, int, float)`](#addcirclefloat-float-float-colorview-int-float) | Appends a stroked circle outline tessellated with the given segment count. |
| [`addCircleFilled(float, float, float, ColorView, int)`](#addcirclefilledfloat-float-float-colorview-int) | Appends a solid filled circle tessellated with the given segment count. |
| [`addNgon(float, float, float, ColorView, int, float)`](#addngonfloat-float-float-colorview-int-float) | Appends a stroked regular N-gon outline (pentagon, hexagon, octagon, etc.). |
| [`addNgonFilled(float, float, float, ColorView, int)`](#addngonfilledfloat-float-float-colorview-int) | Appends a solid filled regular N-sided polygon. |
| [`addEllipse(float, float, float, float, ColorView, int, float)`](#addellipsefloat-float-float-float-colorview-int-float) | Appends a stroked ellipse outline with independent X and Y radii. |
| [`addEllipseFilled(float, float, float, float, ColorView, int)`](#addellipsefilledfloat-float-float-float-colorview-int) | Appends a solid filled ellipse with independent X and Y radii. |
| [`addPolyline(List, ColorView, boolean, float)`](#addpolylinelist-colorview-boolean-float) | Appends a multi-segment line path connecting a sequence of points. |
| [`addConvexPolyFilled(List, ColorView)`](#addconvexpolyfilledlist-colorview) | Appends a solid fill command for a convex polygon of arbitrary vertices. |
| [`addConcavePolyFilled(List, ColorView)`](#addconcavepolyfilledlist-colorview) | Appends a solid fill command for a complex concave polygon using ear-clipping. |
| [`addImage(TextureHandle, float, float, float, float, ColorView)`](#addimagetexturehandle-float-float-float-float-colorview) | Appends a textured rectangle command with full UV coverage and color tint. |
| [`addImage(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView)`](#addimagetexturehandle-float-float-float-float-drawpoint-drawpoint-colorview) | Appends a textured rectangle sampled from an atlas using UV min/max coordinates. |
| [`addImageQuad(TextureHandle, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)`](#addimagequadtexturehandle-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-colorview) | Appends an arbitrary quadrilateral with individual per-vertex UV mappings. |
| [`addTexturedQuad(TextureHandle, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, ColorView)`](#addtexturedquadtexturehandle-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-colorview) | Appends a textured quad using raw primitive float coordinates without creating DrawPoint instances. |
| [`addImageRounded(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView, float)`](#addimageroundedtexturehandle-float-float-float-float-drawpoint-drawpoint-colorview-float) | Appends an image clipped to a rounded rectangle with corner radius. |
| [`addMesh(DrawMesh, TextureHandle)`](#addmeshdrawmesh-texturehandle) | Submits a raw vertex/index DrawMesh with an optional bound texture. |
| [`ribbon(TextureHandle, List, float, float, ColorView, RibbonOptions)`](#ribbontexturehandle-list-float-float-colorview-ribbonoptions) | Renders a textured ribbon along a polyline path with miter joints and configurable [RibbonOptions](<ribbon-options.md>). |
| [`ribbon(TextureHandle, List, float, float, ColorView)`](#ribbontexturehandle-list-float-float-colorview) | Renders a textured ribbon along a polyline with default options. |
| [`ribbon(TextureHandle, List, float, float, ColorView, boolean)`](#ribbontexturehandle-list-float-float-colorview-boolean) | Renders a closed textured ribbon loop along a polyline. |
| [`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean)`](#ribbontexturehandle-list-float-float-colorview-boolean-boolean) | Renders a ribbon with UV axis swapped for vertical strip textures. |
| [`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean, boolean, boolean)`](#ribbontexturehandle-list-float-float-colorview-boolean-boolean-boolean-boolean) | Renders a ribbon with full control over UV swapping and flipping. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addDrawCmd(DrawCommand)` {: #adddrawcmddrawcommand data-toc-label="addDrawCmd(DrawCommand)" }

**Append Raw Draw Command**

Appends a pre-built DrawCommand, automatically adjusting its transform pivot.

| Parameter | Type | Description |
| --- | --- | --- |
| `command` | `DrawCommand` | Pre-built DrawCommand to append directly. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addCallback(CustomDraw)` {: #addcallbackcustomdraw data-toc-label="addCallback(CustomDraw)" }

**Add Custom Draw Callback**

Inserts a custom drawing callback executed during backend command replay.

| Parameter | Type | Description |
| --- | --- | --- |
| `callback` | `CustomDraw` | CustomDraw callback executed during backend rendering. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addLine(float, float, float, float, ColorView, float)` {: #addlinefloat-float-float-float-colorview-float data-toc-label="addLine(float, float, float, float, ColorView, float)" }

**Add Line with Thickness**

Appends a line command between two points with exact color and pixel thickness.

| Parameter | Type | Description |
| --- | --- | --- |
| `x1` | `float` | X coordinate of the first point. |
| `y1` | `float` | Y coordinate of the first point. |
| `x2` | `float` | X coordinate of the second point. |
| `y2` | `float` | Y coordinate of the second point. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRect(float, float, float, float, ColorView, float)` {: #addrectfloat-float-float-float-colorview-float data-toc-label="addRect(float, float, float, float, ColorView, float)" }

**Add Rectangle Frame**

Appends a stroked rectangular frame with specified border thickness.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRect(float, float, float, float, float, ColorView, float)` {: #addrectfloat-float-float-float-float-colorview-float data-toc-label="addRect(float, float, float, float, float, ColorView, float)" }

**Add Rounded Rectangle Frame**

Appends a stroked rounded rectangle frame with corner radius and thickness.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRectFilled(float, float, float, float, ColorView)` {: #addrectfilledfloat-float-float-float-colorview data-toc-label="addRectFilled(float, float, float, float, ColorView)" }

**Add Solid Filled Rectangle**

Appends a solid rectangle fill command with direct ColorView tint.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRectFilled(float, float, float, float, float, ColorView)` {: #addrectfilledfloat-float-float-float-float-colorview data-toc-label="addRectFilled(float, float, float, float, float, ColorView)" }

**Add Solid Filled Rounded Rectangle**

Appends a solid rounded rectangle fill command with specified corner radius.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addRectFilledMultiColor(float, float, float, float, ColorView, ColorView, ColorView, ColorView)` {: #addrectfilledmulticolorfloat-float-float-float-colorview-colorview-colorview-colorview data-toc-label="addRectFilledMultiColor(float, float, float, float, ColorView, ColorView, ColorView, ColorView)" }

**Add 4-Corner Gradient Rectangle**

Renders a rectangle with per-vertex color interpolation at each of the 4 corners.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `topLeft` | `ColorView` | Color applied to the top-left vertex. |
| `topRight` | `ColorView` | Color applied to the top-right vertex. |
| `bottomRight` | `ColorView` | Color applied to the bottom-right vertex. |
| `bottomLeft` | `ColorView` | Color applied to the bottom-left vertex. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addQuad(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView, float)` {: #addquaddrawpoint-drawpoint-drawpoint-drawpoint-colorview-float data-toc-label="addQuad(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView, float)" }

**Add Quad Outline**

Appends a stroked arbitrary four-sided polygon defined by four vertices.

| Parameter | Type | Description |
| --- | --- | --- |
| `p1` | `DrawPoint` | First point defining the quad or triangle. |
| `p2` | `DrawPoint` | Second point defining the quad or triangle. |
| `p3` | `DrawPoint` | Third point defining the quad or triangle. |
| `p4` | `DrawPoint` | Fourth point defining the quadrilateral. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addQuadFilled(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)` {: #addquadfilleddrawpoint-drawpoint-drawpoint-drawpoint-colorview data-toc-label="addQuadFilled(DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)" }

**Add Filled Quad**

Appends a filled arbitrary quadrilateral defined by four vertices.

| Parameter | Type | Description |
| --- | --- | --- |
| `p1` | `DrawPoint` | First point defining the quad or triangle. |
| `p2` | `DrawPoint` | Second point defining the quad or triangle. |
| `p3` | `DrawPoint` | Third point defining the quad or triangle. |
| `p4` | `DrawPoint` | Fourth point defining the quadrilateral. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addTriangle(DrawPoint, DrawPoint, DrawPoint, ColorView, float)` {: #addtriangledrawpoint-drawpoint-drawpoint-colorview-float data-toc-label="addTriangle(DrawPoint, DrawPoint, DrawPoint, ColorView, float)" }

**Add Triangle Outline**

Appends a stroked triangle outline defined by three points.

| Parameter | Type | Description |
| --- | --- | --- |
| `p1` | `DrawPoint` | First point defining the quad or triangle. |
| `p2` | `DrawPoint` | Second point defining the quad or triangle. |
| `p3` | `DrawPoint` | Third point defining the quad or triangle. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addTriangleFilled(DrawPoint, DrawPoint, DrawPoint, ColorView)` {: #addtrianglefilleddrawpoint-drawpoint-drawpoint-colorview data-toc-label="addTriangleFilled(DrawPoint, DrawPoint, DrawPoint, ColorView)" }

**Add Filled Triangle**

Appends a solid filled triangle defined by three points.

| Parameter | Type | Description |
| --- | --- | --- |
| `p1` | `DrawPoint` | First point defining the quad or triangle. |
| `p2` | `DrawPoint` | Second point defining the quad or triangle. |
| `p3` | `DrawPoint` | Third point defining the quad or triangle. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addCircle(float, float, float, ColorView, int, float)` {: #addcirclefloat-float-float-colorview-int-float data-toc-label="addCircle(float, float, float, ColorView, int, float)" }

**Add Circle Outline with Segments**

Appends a stroked circle outline tessellated with the given segment count.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `segments` | `int` | Number of segments approximating the curve/polygon. |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addCircleFilled(float, float, float, ColorView, int)` {: #addcirclefilledfloat-float-float-colorview-int data-toc-label="addCircleFilled(float, float, float, ColorView, int)" }

**Add Filled Circle with Segments**

Appends a solid filled circle tessellated with the given segment count.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `segments` | `int` | Number of segments approximating the curve/polygon. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addNgon(float, float, float, ColorView, int, float)` {: #addngonfloat-float-float-colorview-int-float data-toc-label="addNgon(float, float, float, ColorView, int, float)" }

**Add Regular Polygon Outline**

Appends a stroked regular N-gon outline (pentagon, hexagon, octagon, etc.).

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `segments` | `int` | Number of segments approximating the curve/polygon. |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addNgonFilled(float, float, float, ColorView, int)` {: #addngonfilledfloat-float-float-colorview-int data-toc-label="addNgonFilled(float, float, float, ColorView, int)" }

**Add Filled Regular Polygon**

Appends a solid filled regular N-sided polygon.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `segments` | `int` | Number of segments approximating the curve/polygon. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addEllipse(float, float, float, float, ColorView, int, float)` {: #addellipsefloat-float-float-float-colorview-int-float data-toc-label="addEllipse(float, float, float, float, ColorView, int, float)" }

**Add Ellipse Outline**

Appends a stroked ellipse outline with independent X and Y radii.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radiusX` | `float` | Horizontal radius along the X axis. |
| `radiusY` | `float` | Vertical radius along the Y axis. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `segments` | `int` | Number of segments approximating the curve/polygon. |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addEllipseFilled(float, float, float, float, ColorView, int)` {: #addellipsefilledfloat-float-float-float-colorview-int data-toc-label="addEllipseFilled(float, float, float, float, ColorView, int)" }

**Add Filled Ellipse**

Appends a solid filled ellipse with independent X and Y radii.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radiusX` | `float` | Horizontal radius along the X axis. |
| `radiusY` | `float` | Vertical radius along the Y axis. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `segments` | `int` | Number of segments approximating the curve/polygon. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addPolyline(List, ColorView, boolean, float)` {: #addpolylinelist-colorview-boolean-float data-toc-label="addPolyline(List, ColorView, boolean, float)" }

**Add Connected Polyline**

Appends a multi-segment line path connecting a sequence of points.

| Parameter | Type | Description |
| --- | --- | --- |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `closed` | `boolean` | Whether the polyline or path loop should be closed. |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addConvexPolyFilled(List, ColorView)` {: #addconvexpolyfilledlist-colorview data-toc-label="addConvexPolyFilled(List, ColorView)" }

**Add Filled Convex Polygon**

Appends a solid fill command for a convex polygon of arbitrary vertices.

| Parameter | Type | Description |
| --- | --- | --- |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addConcavePolyFilled(List, ColorView)` {: #addconcavepolyfilledlist-colorview data-toc-label="addConcavePolyFilled(List, ColorView)" }

**Add Filled Concave Polygon**

Appends a solid fill command for a complex concave polygon using ear-clipping.

| Parameter | Type | Description |
| --- | --- | --- |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImage(TextureHandle, float, float, float, float, ColorView)` {: #addimagetexturehandle-float-float-float-float-colorview data-toc-label="addImage(TextureHandle, float, float, float, float, ColorView)" }

**Add Image Command**

Appends a textured rectangle command with full UV coverage and color tint.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImage(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView)` {: #addimagetexturehandle-float-float-float-float-drawpoint-drawpoint-colorview data-toc-label="addImage(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView)" }

**Add Image with Sub-UV Range**

Appends a textured rectangle sampled from an atlas using UV min/max coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `uvMin` | `DrawPoint` | Top-left UV coordinate in texture space. |
| `uvMax` | `DrawPoint` | Bottom-right UV coordinate in texture space. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImageQuad(TextureHandle, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)` {: #addimagequadtexturehandle-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-drawpoint-colorview data-toc-label="addImageQuad(TextureHandle, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, DrawPoint, ColorView)" }

**Add Textured 4-Point Quad**

Appends an arbitrary quadrilateral with individual per-vertex UV mappings.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `p1` | `DrawPoint` | First point defining the quad or triangle. |
| `p2` | `DrawPoint` | Second point defining the quad or triangle. |
| `p3` | `DrawPoint` | Third point defining the quad or triangle. |
| `p4` | `DrawPoint` | Fourth point defining the quadrilateral. |
| `uv1` | `DrawPoint` | UV texture coordinate at vertex 1. |
| `uv2` | `DrawPoint` | UV texture coordinate at vertex 2. |
| `uv3` | `DrawPoint` | UV texture coordinate at vertex 3. |
| `uv4` | `DrawPoint` | UV texture coordinate at vertex 4. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addTexturedQuad(TextureHandle, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, ColorView)` {: #addtexturedquadtexturehandle-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-float-colorview data-toc-label="addTexturedQuad(TextureHandle, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, ColorView)" }

**Add Textured Quad (Primitive Floats, Garbage-Free)**

Appends a textured quad using raw primitive float coordinates without creating DrawPoint instances.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `x1` | `float` | X coordinate of the first point. |
| `y1` | `float` | Y coordinate of the first point. |
| `x2` | `float` | X coordinate of the second point. |
| `y2` | `float` | Y coordinate of the second point. |
| `x3` | `float` | X coordinate of the third vertex. |
| `y3` | `float` | Y coordinate of the third vertex. |
| `x4` | `float` | X coordinate of the fourth vertex. |
| `y4` | `float` | Y coordinate of the fourth vertex. |
| `u1` | `float` | Horizontal U texture coordinate for vertex 1. |
| `v1` | `float` | Vertical V texture coordinate for vertex 1. |
| `u2` | `float` | Horizontal U texture coordinate for vertex 2. |
| `v2` | `float` | Vertical V texture coordinate for vertex 2. |
| `u3` | `float` | Horizontal U texture coordinate for vertex 3. |
| `v3` | `float` | Vertical V texture coordinate for vertex 3. |
| `u4` | `float` | Horizontal U texture coordinate for vertex 4. |
| `v4` | `float` | Vertical V texture coordinate for vertex 4. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addImageRounded(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView, float)` {: #addimageroundedtexturehandle-float-float-float-float-drawpoint-drawpoint-colorview-float data-toc-label="addImageRounded(TextureHandle, float, float, float, float, DrawPoint, DrawPoint, ColorView, float)" }

**Add Rounded Image**

Appends an image clipped to a rounded rectangle with corner radius.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `uvMin` | `DrawPoint` | Top-left UV coordinate in texture space. |
| `uvMax` | `DrawPoint` | Bottom-right UV coordinate in texture space. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`addMesh(DrawMesh, TextureHandle)` {: #addmeshdrawmesh-texturehandle data-toc-label="addMesh(DrawMesh, TextureHandle)" }

**Add Custom Geometry Mesh**

Submits a raw vertex/index DrawMesh with an optional bound texture.

| Parameter | Type | Description |
| --- | --- | --- |
| `mesh` | `DrawMesh` | Precomputed indexed DrawMesh vertex buffer. |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, RibbonOptions)` {: #ribbontexturehandle-list-float-float-colorview-ribbonoptions data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, RibbonOptions)" }

**Draw Procedural Ribbon with Options**

Renders a textured ribbon along a polyline path with miter joints and configurable [RibbonOptions](<ribbon-options.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `textureLength` | `float` | World-space length of a single repeating texture tile along the path. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |
| `options` | `RibbonOptions` | Shader execution options or ribbon styling settings. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView)` {: #ribbontexturehandle-list-float-float-colorview data-toc-label="ribbon(TextureHandle, List, float, float, ColorView)" }

**Draw Procedural Ribbon (Deprecated)**

Renders a textured ribbon along a polyline with default options.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `textureLength` | `float` | World-space length of a single repeating texture tile along the path. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, boolean)` {: #ribbontexturehandle-list-float-float-colorview-boolean data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, boolean)" }

**Draw Procedural Ribbon Closed (Deprecated)**

Renders a closed textured ribbon loop along a polyline.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `textureLength` | `float` | World-space length of a single repeating texture tile along the path. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |
| `closed` | `boolean` | Whether the polyline or path loop should be closed. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean)` {: #ribbontexturehandle-list-float-float-colorview-boolean-boolean data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean)" }

**Draw Ribbon with UV Orientation (Deprecated)**

Renders a ribbon with UV axis swapped for vertical strip textures.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `textureLength` | `float` | World-space length of a single repeating texture tile along the path. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |
| `closed` | `boolean` | Whether the polyline or path loop should be closed. |
| `swapUv` | `boolean` | Whether to swap U and V axes for vertical strip textures. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean, boolean, boolean)` {: #ribbontexturehandle-list-float-float-colorview-boolean-boolean-boolean-boolean data-toc-label="ribbon(TextureHandle, List, float, float, ColorView, boolean, boolean, boolean, boolean)" }

**Draw Ribbon with Transform Flags (Deprecated)**

Renders a ribbon with full control over UV swapping and flipping.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Handle to the GPU texture resource. |
| `points` | `List` | List of 2D vertex points defining the polygon or ribbon. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `textureLength` | `float` | World-space length of a single repeating texture tile along the path. |
| `tint` | `ColorView` | ColorView tint multiplied with the texture pixels. |
| `closed` | `boolean` | Whether the polyline or path loop should be closed. |
| `swapUv` | `boolean` | Whether to swap U and V axes for vertical strip textures. |
| `flipU` | `boolean` | Whether to invert horizontal texture mapping within each tile. |
| `flipV` | `boolean` | Whether to invert vertical texture mapping within each tile. |

</div>

### Vector Paths & Channels

| Method | Description |
| --- | --- |
| [`channelsSplit(int)`](#channelssplitint) | Splits the draw command stream into multiple independent visual depth channels. |
| [`channelsMerge()`](#channelsmerge) | Merges previously split channels back in sequential layer order. |
| [`channelsSetCurrent(int)`](#channelssetcurrentint) | Switches the active output target channel index for subsequent drawing commands. |
| [`path()`](#path) | Returns the context's internal reusable VectorPath builder. |
| [`pathClear()`](#pathclear) | Resets the active vector path, clearing all recorded vertices and segments. |
| [`pathLineTo(DrawPoint)`](#pathlinetodrawpoint) | Appends a point vertex to the active vector path. |
| [`pathLineTo(float, float)`](#pathlinetofloat-float) | Appends coordinate values (x, y) to the active vector path. |
| [`pathLineToMergeDuplicate(DrawPoint)`](#pathlinetomergeduplicatedrawpoint) | Appends a point vertex, ignoring it if it matches the previous vertex within epsilon. |
| [`pathLineToMergeDuplicate(float, float)`](#pathlinetomergeduplicatefloat-float) | Appends coordinates (x, y), ignoring them if identical to the previous vertex. |
| [`pathFillConvex(ColorView)`](#pathfillconvexcolorview) | Fills the active convex vector path using the specified ColorView. |
| [`pathStroke(ColorView, boolean, float)`](#pathstrokecolorview-boolean-float) | Strokes the active vector path with specified color and line thickness. |
| [`pathArcTo(float, float, float, float, float, int)`](#patharctofloat-float-float-float-float-int) | Appends a circular arc segment to the active path between minimum and maximum angles. |
| [`pathArcToFast(float, float, float, int, int)`](#patharctofastfloat-float-float-int-int) | Appends an arc using precomputed 12-sample trigonometric lookup tables. |
| [`pathBezierCubicCurveTo(float, float, float, float, float, float, int)`](#pathbeziercubiccurvetofloat-float-float-float-float-float-int) | Appends a cubic Bezier curve segment defined by two control points and an endpoint. |
| [`pathBezierQuadraticCurveTo(float, float, float, float, int)`](#pathbezierquadraticcurvetofloat-float-float-float-int) | Appends a quadratic Bezier curve segment with one control point and an endpoint. |
| [`pathRect(float, float, float, float)`](#pathrectfloat-float-float-float) | Appends the 4 vertices of a rectangle to the active vector path. |
| [`pathRect(float, float, float, float, float)`](#pathrectfloat-float-float-float-float) | Appends corner arc vertices for a rounded rectangle to the active vector path. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`channelsSplit(int)` {: #channelssplitint data-toc-label="channelsSplit(int)" }

**Split Draw Channels**

Splits the draw command stream into multiple independent visual depth channels.

| Parameter | Type | Description |
| --- | --- | --- |
| `count` | `int` | Number of separate drawing channels to allocate. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`channelsMerge()` {: #channelsmerge data-toc-label="channelsMerge()" }

**Merge Draw Channels**

Merges previously split channels back in sequential layer order.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`channelsSetCurrent(int)` {: #channelssetcurrentint data-toc-label="channelsSetCurrent(int)" }

**Set Active Channel**

Switches the active output target channel index for subsequent drawing commands.

| Parameter | Type | Description |
| --- | --- | --- |
| `channel` | `int` | Target channel index for subsequent draw commands. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`path()` {: #path data-toc-label="path()" }

**Get Active Vector [Path](<../../widgets/display/path.md>)**

Returns the context's internal reusable VectorPath builder.

Returns `VectorPath`: Active VectorPath instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathClear()` {: #pathclear data-toc-label="pathClear()" }

**Clear [Path](<../../widgets/display/path.md>) Points**

Resets the active vector path, clearing all recorded vertices and segments.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineTo(DrawPoint)` {: #pathlinetodrawpoint data-toc-label="pathLineTo(DrawPoint)" }

**Add Point to [Path](<../../widgets/display/path.md>)**

Appends a point vertex to the active vector path.

| Parameter | Type | Description |
| --- | --- | --- |
| `point` | `DrawPoint` | Vertex point coordinates to append. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineTo(float, float)` {: #pathlinetofloat-float data-toc-label="pathLineTo(float, float)" }

**Add Coordinates to [Path](<../../widgets/display/path.md>)**

Appends coordinate values (x, y) to the active vector path.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineToMergeDuplicate(DrawPoint)` {: #pathlinetomergeduplicatedrawpoint data-toc-label="pathLineToMergeDuplicate(DrawPoint)" }

**Add Point Merging Duplicates**

Appends a point vertex, ignoring it if it matches the previous vertex within epsilon.

| Parameter | Type | Description |
| --- | --- | --- |
| `point` | `DrawPoint` | Vertex point coordinates to append. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathLineToMergeDuplicate(float, float)` {: #pathlinetomergeduplicatefloat-float data-toc-label="pathLineToMergeDuplicate(float, float)" }

**Add Coordinates Merging Duplicates**

Appends coordinates (x, y), ignoring them if identical to the previous vertex.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathFillConvex(ColorView)` {: #pathfillconvexcolorview data-toc-label="pathFillConvex(ColorView)" }

**Fill Convex [Path](<../../widgets/display/path.md>)**

Fills the active convex vector path using the specified ColorView.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathStroke(ColorView, boolean, float)` {: #pathstrokecolorview-boolean-float data-toc-label="pathStroke(ColorView, boolean, float)" }

**Stroke [Path](<../../widgets/display/path.md>) Outline**

Strokes the active vector path with specified color and line thickness.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Direct ColorView for solid fill or stroke without allocating [Paint](<paint.md>). |
| `closed` | `boolean` | Whether the polyline or path loop should be closed. |
| `thickness` | `float` | Line or outline thickness in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathArcTo(float, float, float, float, float, int)` {: #patharctofloat-float-float-float-float-int data-toc-label="pathArcTo(float, float, float, float, float, int)" }

**Append Arc to [Path](<../../widgets/display/path.md>)**

Appends a circular arc segment to the active path between minimum and maximum angles.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `minAngle` | `float` | Starting arc angle in radians. |
| `maxAngle` | `float` | Ending arc angle in radians. |
| `segments` | `int` | Number of segments approximating the curve/polygon. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathArcToFast(float, float, float, int, int)` {: #patharctofastfloat-float-float-int-int data-toc-label="pathArcToFast(float, float, float, int, int)" }

**Append Fast Lookup Arc to [Path](<../../widgets/display/path.md>)**

Appends an arc using precomputed 12-sample trigonometric lookup tables.

| Parameter | Type | Description |
| --- | --- | --- |
| `centerX` | `float` | X coordinate of the circle/polygon center. |
| `centerY` | `float` | Y coordinate of the circle/polygon center. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |
| `minSampleOf12` | `int` | Starting index in the 12-sample precomputed circle table. |
| `maxSampleOf12` | `int` | Ending index in the 12-sample precomputed circle table. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathBezierCubicCurveTo(float, float, float, float, float, float, int)` {: #pathbeziercubiccurvetofloat-float-float-float-float-float-int data-toc-label="pathBezierCubicCurveTo(float, float, float, float, float, float, int)" }

**Append Cubic Bezier Curve to [Path](<../../widgets/display/path.md>)**

Appends a cubic Bezier curve segment defined by two control points and an endpoint.

| Parameter | Type | Description |
| --- | --- | --- |
| `controlX1` | `float` | X coordinate of the first cubic Bezier control point. |
| `controlY1` | `float` | Y coordinate of the first cubic Bezier control point. |
| `controlX2` | `float` | X coordinate of the second cubic Bezier control point. |
| `controlY2` | `float` | Y coordinate of the second cubic Bezier control point. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `segments` | `int` | Number of segments approximating the curve/polygon. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathBezierQuadraticCurveTo(float, float, float, float, int)` {: #pathbezierquadraticcurvetofloat-float-float-float-int data-toc-label="pathBezierQuadraticCurveTo(float, float, float, float, int)" }

**Append Quadratic Bezier Curve to [Path](<../../widgets/display/path.md>)**

Appends a quadratic Bezier curve segment with one control point and an endpoint.

| Parameter | Type | Description |
| --- | --- | --- |
| `controlX` | `float` | X coordinate of the single quadratic Bezier control point. |
| `controlY` | `float` | Y coordinate of the single quadratic Bezier control point. |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `segments` | `int` | Number of segments approximating the curve/polygon. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathRect(float, float, float, float)` {: #pathrectfloat-float-float-float data-toc-label="pathRect(float, float, float, float)" }

**Append Rectangle to [Path](<../../widgets/display/path.md>)**

Appends the 4 vertices of a rectangle to the active vector path.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pathRect(float, float, float, float, float)` {: #pathrectfloat-float-float-float-float data-toc-label="pathRect(float, float, float, float, float)" }

**Append Rounded Rectangle to [Path](<../../widgets/display/path.md>)**

Appends corner arc vertices for a rounded rectangle to the active vector path.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |
| `radius` | `float` | Corner radius for rounded geometry in pixels. |

</div>

### Clipping & Scissor

| Method | Description |
| --- | --- |
| [`pushClip(float, float, float, float)`](#pushclipfloat-float-float-float) | Pushes a rectangular clipping region restricting all subsequent rendering calls. |
| [`pushTextClip(float, float, float, float)`](#pushtextclipfloat-float-float-float) | Pushes a clipping region with protective bleed padding to prevent font antialiasing clipping. |
| [`popClip()`](#popclip) | Restores the previous clipping boundary, popping the top clip frame off the stack. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pushClip(float, float, float, float)` {: #pushclipfloat-float-float-float data-toc-label="pushClip(float, float, float, float)" }

**Push Scissor Clip Region**

Pushes a rectangular clipping region restricting all subsequent rendering calls.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`pushTextClip(float, float, float, float)` {: #pushtextclipfloat-float-float-float data-toc-label="pushTextClip(float, float, float, float)" }

**Push [Text](<../../widgets/display/text.md>)-Safe Clip Region with Bleed**

Pushes a clipping region with protective bleed padding to prevent font antialiasing clipping.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the destination rectangle in local scope space. |
| `y` | `float` | Y coordinate of the destination rectangle in local scope space. |
| `width` | `float` | Width of the bounding area in local pixels. |
| `height` | `float` | Height of the bounding area in local pixels. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">DrawScope::</span>`popClip()` {: #popclip data-toc-label="popClip()" }

**Pop Clip Region**

Restores the previous clipping boundary, popping the top clip frame off the stack.

</div>

