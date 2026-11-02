# `Paint`

Styling descriptor for draw commands configuring RGBA colors, stroke widths, fill/stroke modes, Porter-Duff blending, and dashed lines.

- **Purpose:** Decouples visual styling configuration from geometry recording, allowing reusable and fluent paint definition.

### USE WHEN

- Configure colors, stroke thicknesses, or blend modes for `DrawScope#rect`, `DrawScope#circle`, `DrawScope#nineSlice`, or `DrawScope#path`.
- Build animated dashed outlines for selection boxes using `dash()` and `dashOffset()`.
- Enable additive luminescence (glows, sparks) or multiply shadows using `blend(BlendMode)`.

### CRITICAL RULES & GOTCHAS:

- DrawScope copies or snapshots Paint when recording a DrawCommand; subsequent mutations do not retroactively alter earlier commands.
- For high-frequency render loops where memory allocations matter, prefer DrawScope#*Filled overloads accepting ColorView directly.
- Dash length and gap must be finite non-negative numbers; negative values are clamped to 0.0.

- **Related:** `ColorView`, `BlendMode`, `DrawScope`, `DrawCommand`, `TextureHandle`

### EXAMPLES

#### Create dashed outline paint

```java
Paint paint = Paint.stroke(ColorView.rgb(0, 180, 255), 1.5f)
        .dash(6.0f, 4.0f)
        .dashOffset(animPhase);
scope.roundedRect(x, y, w, h, 4.0f, paint);
```

#### Create additive glowing paint

```java
Paint glow = Paint.fill(ColorView.rgba(0, 220, 255, 90))
        .blend(BlendMode.ADDITIVE);
scope.circle(cx - 16, cy - 16, 32, 32, glow);
```

