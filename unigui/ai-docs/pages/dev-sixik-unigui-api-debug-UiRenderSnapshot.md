# `UiRenderSnapshot`

Detailed read-only breakdown of draw-list structure and estimated backend work for one frame.

- **Purpose:** Supports batching, geometry, shader, texture-switch, and SDF performance analysis beyond the compact `UiDebugSnapshot`.

### USE WHEN

- Diagnose why command count does not translate into efficient backend batches or draw calls.

### CRITICAL RULES & GOTCHAS:

- Per-command-type accessors return zero for null types and for arrays shorter than the enum.
- Estimated draw calls, vertices, and indices describe expected backend work, not guaranteed GPU measurements.
- SDF milliseconds are derived from recorded nanoseconds and represent SDF runtime only.

- **Related:** `UiDebugCounters`, `UiDebugSnapshot`, `DrawCommandType`

