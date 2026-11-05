# `UiRenderPolicy`

Frame-rate and invalidation policy controlling how frequently retained UI trees are rebuilt and rendered.

- **Purpose:** Minimizes unnecessary CPU and GPU rendering overhead by switching between continuous rendering, on-dirty invalidation, fixed FPS, or display VSYNC.

### USE WHEN

- Configuring render refresh behavior on Window, Screen, or root UI container.
- Optimizing static HUDs or dialogs to only render when invalidated (onDirty).
- Locking animations or editor viewports to VSYNC or a specific target FPS.

### CRITICAL RULES & GOTCHAS:

- In ON_DIRTY mode, the UI will not redraw unless an explicit dirty/invalidation call occurs.
- fixedFps requires a positive finite float; non-positive values throw IllegalArgumentException.

- **Related:** `RenderContext`, `DrawScope`, `DrawList`

