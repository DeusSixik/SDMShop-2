# `DrawVertex` (extends `Record`)

Individual vertex descriptor combining position (x, y), texture coordinates (u, v), and vertex tint color.

- **Purpose:** Constitutes the basic building block of DrawMesh geometry.

### USE WHEN

- Constructing custom meshes or triangle lists.
- Consuming vertex output from RibbonMesh tessellation.

### CRITICAL RULES & GOTCHAS:

- Vertex colors are defensively snapshotted upon creation to prevent external mutation.
- UV coordinates default to 0.0 when using the position-and-color constructor.

- **Related:** `DrawMesh`, `DrawPoint`, `RibbonMesh`

