# `UiDebugSnapshot`

Sanitized per-frame performance and texture-cache metrics captured by `UiDebugCounters`.

- **Purpose:** Provides a stable read-only snapshot for overlays, telemetry, tests, and performance diagnostics.

### USE WHEN

- Read frame timing and draw/batch counts from `UIContext.debugCounters().snapshot()`.
- Investigate texture-cache misses without depending on an implementation counter class.

### CRITICAL RULES & GOTCHAS:

- GPU time can be -1 when the active backend cannot provide a GPU measurement.
- All other numeric counters and timings are sanitized to non-negative values.
- Treat the snapshot as one captured frame; do not combine fields from snapshots captured at different times.

- **Related:** `UiDebugCounters`, `UiRenderSnapshot`, `ProfilerSnapshot`

### FRAME IDENTITY

- `frameIndex()` → long — Returns the monotonically supplied frame index represented by this snapshot.

### FRAME TIMING

- `framesPerSecond()` → float — Returns the measured frame rate, or zero when it is unavailable.
- `frameTotalMillis()` → float — Returns total wall-clock frame time in milliseconds.
- `frameGpuMillis()` → float — Returns GPU frame time in milliseconds, or -1 when unavailable.

### RENDER WORK

- `drawCommandCount()` → int — Returns the number of draw-list commands recorded for the frame.
- `batchCount()` → int — Returns the number of backend batches recorded for the frame.

### TEXTURE CACHE

- `lastTextureCacheMissReason()` → String — Returns the last normalized texture-cache miss reason, or `NONE`.

