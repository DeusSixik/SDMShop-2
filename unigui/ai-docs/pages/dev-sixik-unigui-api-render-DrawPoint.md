# `DrawPoint` (extends `Record`)

Immutable 2D floating-point coordinate point used for polylines, ribbon paths, and vector geometry.

- **Purpose:** Represents an unmodifiable point (x, y) in canvas/screen coordinates.

### USE WHEN

- Supplying polyline nodes to RibbonMesh.builder or DrawScope#ribbon.
- Defining points for line strips and polygon boundaries.

### CRITICAL RULES & GOTCHAS:

- DrawPoint is an immutable record.
- Coordinates should be finite floating-point values.

- **Related:** `RibbonMesh`, `DrawVertex`, `VectorPath`

