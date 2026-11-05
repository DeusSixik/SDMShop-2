# `VectorPath`

Mutable 2D vector path builder supporting lines, quadratic/cubic beziers, arcs, rects, and subpath closing.

- **Purpose:** Records sequence of path instructions for vector rendering, stroke outlining, or geometry tessellation.

### USE WHEN

- Constructing arbitrary 2D vector shapes via DrawScope or DrawList#path.
- Defining complex clipping paths or custom icons.

### CRITICAL RULES & GOTCHAS:

- VectorPath does not perform its own tessellation; commands are submitted to DrawList as PATH commands.
- When using the shared DrawList#path builder, invoke DrawList#pathClear() after use.

- **Related:** `DrawList`, `DrawCommand`, `DrawScope`

