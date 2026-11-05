# `DrawCommand`

Backend-neutral draw command recorded into a DrawList representing a single procedural drawing primitive, image, text run, mesh, or clip state.

- **Purpose:** Decouples widget rendering logic from the physical RenderBackend, encapsulating geometry bounds, paints, transforms, UVs, and shader bindings.

### USE WHEN

- Low-level rendering routines assemble custom GPU commands.
- Custom RenderBackends iterate over DrawList commands to execute drawing.

### CRITICAL RULES & GOTCHAS:

- DrawCommand defensively copies mutable references (paints, meshes, transforms) upon recording.
- Commands retrieved via DrawList#obtain must not be retained beyond DrawList#clear().

- **Related:** `DrawCommandType`, `DrawList`, `Paint`, `Transform`, `DrawMesh`, `TextureHandle`, `VectorPath`

