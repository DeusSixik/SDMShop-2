# `Path` (extends `WidgetBase`)

Vector path widget drawing complex custom 2D shapes, bezier curves, and polylines.

- **Purpose:** Displays complex procedural vector shapes defined by VectorPath geometry.

### USE WHEN

- Drawing vector icons, custom arrows, stars, or polygon emblems.
- Mathematical curve plotting and dynamic vector lines.

### CRITICAL RULES & GOTCHAS:

- VectorPath must be immutable or re-assigned to update visual geometry.
- strokeWidth applies when stroke() is enabled.

- **Related:** `Shape`, `VectorPath`, `MutableColor`

