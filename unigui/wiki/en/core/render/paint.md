---
title: "Paint"
slug: "paint"
category: "Core/Render"
locale: "en"
order: 30
---

# Paint

Styling descriptor for draw commands that configures color, stroke width, fill/stroke modes, Porter-Duff blending, and dashed lines.

## Overview & Architecture

`Paint` describes the visual attributes applied to vector shapes, geometric paths, textured surfaces, and drop shadows in UniGUI.

### Responsibilities

- **Coloring**: Encapsulates RGBA color view (`ColorView`) with fluent mutations.
- **Geometry Styling**: Toggles between solid interior fill (`stroke = false`) and outline perimeter stroke (`stroke = true`) with arbitrary pixel thickness.
- **Alpha Compositing**: Directs GPU hardware blending with `BlendMode` (NORMAL, ADDITIVE, MULTIPLY, SCREEN).
- **Line Patterns**: Configures dashed borders (`dash(length, gap)`) and animated pattern phases (`dashOffset`).

## Deferred Copy Semantics

`Paint` is deliberately designed as a **mutable object** to enable clean, fluent method chaining during widget preparation.

However, UniGUI's deferred draw-list architecture guarantees isolation:

> [!NOTE]
> When `DrawScope` creates a `DrawCommand`, it internally copies or snapshots the current `Paint` state. Any subsequent modifications made to your `Paint` instance will **never** alter or corrupt commands that were already recorded earlier in the frame.

## Fill vs Stroke Modes

Use convenient static factories to construct specialized `Paint` instances:

```java
// Solid interior fill:
Paint fillPaint = Paint.fill(ColorView.rgb(255, 100, 0));

// Outline border stroke (2.0 UI pixels):
Paint borderPaint = Paint.stroke(ColorView.WHITE, 2.0f);
```

For high-framerate rendering loops where memory allocation is critical, prefer `DrawScope#rectFilled(...)`, `DrawScope#rectStroke(...)`, etc., which accept `ColorView` directly with **zero object allocations**.

## Blend Modes & Compositing

Configure Porter-Duff hardware blending with `paint.blend(BlendMode)`:

| Blend Mode | Primary Use Cases | Visual Effect |
| --- | --- | --- |
| `BlendMode.NORMAL` | Standard UI elements, opaque/translucent panels | Alpha-blended alpha-over compositing |
| `BlendMode.ADDITIVE` | Magic glows, particle sparks, neon selection lasers | Source color is added to destination, brightening pixels |
| `BlendMode.MULTIPLY` | Ambient occlusion drops, realistic soft shadows | Colors are multiplied, darkening underlying background |
| `BlendMode.SCREEN` | Soft light highlights, holograms | Inverted multiply creating smooth bright luminescence |

## Dashed Borders & Animated Ants

Stroked paths and shapes support dash patterns specified in UI pixels:

```java
Paint dashPaint = Paint.stroke(ColorView.rgb(0, 180, 255), 1.5f)
        .dash(6.0f, 4.0f) // 6px segment, 4px gap
        .dashOffset((System.currentTimeMillis() / 20.0f) % 10.0f); // animated marching ants
```

Calling `clearDash()` resets the paint back to a continuous solid line.

## Animated Selection [Box](<../../widgets/containers/box.md>) with Marching Ants

```java
// Create animated marching-ants selection outline
float animPhase = (System.currentTimeMillis() % 1000) / 100.0f;
Paint selectionPaint = Paint.stroke(ColorView.rgb(80, 160, 255), 2.0f)
        .dash(5.0f, 5.0f)
        .dashOffset(animPhase);

RectView bounds = widget.layoutBounds();
scope.roundedRect(bounds.x(), bounds.y(), bounds.width(), bounds.height(), 4.0f, selectionPaint);
```

## Additive Glowing Particle / Orb

```java
// Additive glowing aura around icon
Paint glowPaint = Paint.fill(ColorView.rgba(0, 220, 255, 90))
        .blend(BlendMode.ADDITIVE);

scope.circle(centerX - 16, centerY - 16, 32, 32, glowPaint);
```

## API

### Factory Methods & Constructor

| Method | Description |
| --- | --- |
| [`Paint()`](#paint) | Creates a new Paint initialized with solid white fill, normal blend mode, and no stroke. |
| [`fill(ColorView)`](#fillcolorview) | Constructs a Paint configured for solid interior geometry fills with the specified color. |
| [`stroke(ColorView, float)`](#strokecolorview-float) | Constructs a Paint configured for outline stroke rendering with color and thickness. |

<div class="api-member-card" markdown="1">

#### `Paint()` {: #paint data-toc-label="Paint()" }

**Construct Default Paint**

Creates a new Paint initialized with solid white fill, normal blend mode, and no stroke.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`fill(ColorView)` {: #fillcolorview data-toc-label="fill(ColorView)" }

**Create Fill Paint**

Constructs a Paint configured for solid interior geometry fills with the specified color.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Color to apply to geometry or texturing. |

Returns `Paint`: New Paint instance configured for fill mode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`stroke(ColorView, float)` {: #strokecolorview-float data-toc-label="stroke(ColorView, float)" }

**Create Stroke Paint**

Constructs a Paint configured for outline stroke rendering with color and thickness.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Color to apply to geometry or texturing. |
| `width` | `float` | Line stroke width in UI pixels. |

Returns `Paint`: New Paint instance configured for outline stroke mode..

</div>

### Color & Fill Mode

| Method | Description |
| --- | --- |
| [`color()`](#color) | Returns the internal mutable color object of this Paint. |
| [`color(ColorView)`](#colorcolorview) | Updates this Paint's color from the given ColorView. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`color()` {: #color data-toc-label="color()" }

**Get Color**

Returns the internal mutable color object of this Paint.

Returns `MutableColor`: Internal MutableColor reference..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Set Color**

Updates this Paint's color from the given ColorView.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Color to apply to geometry or texturing. |

Returns `Paint`: This Paint instance for chained method calls..

</div>

### Stroke Width & Outlines

| Method | Description |
| --- | --- |
| [`strokeWidth()`](#strokewidth) | Returns the stroke outline thickness in UI pixels. |
| [`strokeWidth(float)`](#strokewidthfloat) | Configures stroke line thickness in UI pixels. |
| [`isStroke()`](#isstroke) | Returns true if this Paint is configured to draw outlines rather than solid fills. |
| [`stroke(boolean)`](#strokeboolean) | Toggles between outline stroke mode and solid interior fill mode. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`strokeWidth()` {: #strokewidth data-toc-label="strokeWidth()" }

**Get Stroke Width**

Returns the stroke outline thickness in UI pixels.

Returns `float`: Stroke width in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`strokeWidth(float)` {: #strokewidthfloat data-toc-label="strokeWidth(float)" }

**Set Stroke Width**

Configures stroke line thickness in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `strokeWidth` | `float` | Line stroke width in UI pixels. |

Returns `Paint`: This Paint instance for chained method calls..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`isStroke()` {: #isstroke data-toc-label="isStroke()" }

**Check Stroke Mode**

Returns true if this Paint is configured to draw outlines rather than solid fills.

Returns `boolean`: True for stroke mode, false for fill mode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`stroke(boolean)` {: #strokeboolean data-toc-label="stroke(boolean)" }

**Set Stroke Mode**

Toggles between outline stroke mode and solid interior fill mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `stroke` | `boolean` | True for stroke mode, false for fill mode. |

Returns `Paint`: This Paint instance for chained method calls..

</div>

### Blending & Alpha Modes

| Method | Description |
| --- | --- |
| [`blendMode()`](#blendmode) | Returns the active Porter-Duff hardware blending mode. |
| [`blend(BlendMode)`](#blendblendmode) | Configures Porter-Duff hardware blending mode. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`blendMode()` {: #blendmode data-toc-label="blendMode()" }

**Get Blend Mode**

Returns the active Porter-Duff hardware blending mode.

Returns `BlendMode`: Configured BlendMode enum constant..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`blend(BlendMode)` {: #blendblendmode data-toc-label="blend(BlendMode)" }

**Set Blend Mode**

Configures Porter-Duff hardware blending mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `blendMode` | `BlendMode` | Porter-Duff hardware blend mode, or null for default NORMAL. |

Returns `Paint`: This Paint instance for chained method calls..

</div>

### Dash Patterns

| Method | Description |
| --- | --- |
| [`dashed()`](#dashed) | Returns true if stroke outlines will render using a dashed pattern. |
| [`dashLength()`](#dashlength) | Returns the length of visible dashed stroke segments. |
| [`dashGap()`](#dashgap) | Returns the length of empty gaps between dashed stroke segments. |
| [`dashOffset()`](#dashoffset) | Returns the current phase offset of the dash pattern. |
| [`dash(float, float)`](#dashfloat-float) | Configures visible segment length and empty gap length for stroked outlines. |
| [`dashOffset(float)`](#dashoffsetfloat) | Sets the phase offset of the dash pattern for animated lines. |
| [`clearDash()`](#cleardash) | Removes dash pattern, restoring continuous solid stroke lines. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashed()` {: #dashed data-toc-label="dashed()" }

**Check Dashed Mode**

Returns true if stroke outlines will render using a dashed pattern.

Returns `boolean`: True if dash length and dash gap are both positive..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashLength()` {: #dashlength data-toc-label="dashLength()" }

**Get Dash Segment Length**

Returns the length of visible dashed stroke segments.

Returns `float`: Dash length in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashGap()` {: #dashgap data-toc-label="dashGap()" }

**Get Dash Gap Length**

Returns the length of empty gaps between dashed stroke segments.

Returns `float`: Dash gap in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashOffset()` {: #dashoffset data-toc-label="dashOffset()" }

**Get Dash Pattern Offset**

Returns the current phase offset of the dash pattern.

Returns `float`: Dash offset phase in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dash(float, float)` {: #dashfloat-float data-toc-label="dash(float, float)" }

**Configure Dash Pattern**

Configures visible segment length and empty gap length for stroked outlines.

| Parameter | Type | Description |
| --- | --- | --- |
| `length` | `float` | Length of visible dash segments in UI pixels. |
| `gap` | `float` | Length of gaps between dash segments in UI pixels. |

Returns `Paint`: This Paint instance for chained method calls..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashOffset(float)` {: #dashoffsetfloat data-toc-label="dashOffset(float)" }

**Set Dash Offset**

Sets the phase offset of the dash pattern for animated lines.

| Parameter | Type | Description |
| --- | --- | --- |
| `dashOffset` | `float` | Pattern phase offset in UI pixels. |

Returns `Paint`: This Paint instance for chained method calls..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`clearDash()` {: #cleardash data-toc-label="clearDash()" }

**Clear Dash Pattern**

Removes dash pattern, restoring continuous solid stroke lines.

Returns `Paint`: This Paint instance for chained method calls..

</div>

### Copy & State Management

| Method | Description |
| --- | --- |
| [`copy()`](#copy) | Creates an independent deep copy of this Paint with all attributes duplicated. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`copy()` {: #copy data-toc-label="copy()" }

**Clone Paint**

Creates an independent deep copy of this Paint with all attributes duplicated.

Returns `Paint`: Independent Paint duplicate..

</div>

