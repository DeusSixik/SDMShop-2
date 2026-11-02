# `UiPostEffectRenderer`

Rendering dispatcher executing post-processed draw lists with transparent fallback.

- **Purpose:** Dispatches rendering to UiPostEffectBackend when available, falling back to direct render when unsupported or chain is empty.

### USE WHEN

- Rendering a DrawList subject to potential post-processing filters.

- **Related:** `UiPostEffectBackend`, `UiPostEffectChain`, `UiLayerBounds`

