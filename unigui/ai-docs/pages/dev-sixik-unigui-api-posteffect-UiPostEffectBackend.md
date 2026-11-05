# `UiPostEffectBackend`

Optional capability interface for RenderBackend implementations that execute post-processing pipelines.

- **Purpose:** Allows graphics backends to declare support for multi-pass shaders and offscreen ping-pong buffers.

### USE WHEN

- Implementing custom graphics backends with post-processing support.

- **Related:** `RenderBackend`, `UiPostEffectRenderer`, `UiPostEffectChain`

