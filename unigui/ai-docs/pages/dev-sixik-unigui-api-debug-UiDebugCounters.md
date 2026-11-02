# `UiDebugCounters`

Optional sink used by render and cache code to collect debug metrics.

- **Purpose:** Lets custom backends report comparable frame, batching, geometry, shader, and texture-cache metrics to the built-in debug tooling.

### USE WHEN

- Integrate a custom render backend or cache implementation with `UIContext.debugCounters()`.

### CRITICAL RULES & GOTCHAS:

- Call `beginFrame` once before recording metrics for a new frame.
- Record non-negative counts for work actually submitted; snapshots sanitize invalid values but cannot repair double-counting.
- Use `NOOP` when collection is disabled instead of adding null checks to hot paths.

- **Related:** `UiDebugSnapshot`, `UiRenderSnapshot`, `DrawCommandType`

### SNAPSHOTS

- `snapshot()` → UiDebugSnapshot — Returns frame-level timing, command, batch, and texture-cache metrics.
- `renderSnapshot()` → UiRenderSnapshot — Returns detailed draw-list and backend-work metrics for the latest frame.

