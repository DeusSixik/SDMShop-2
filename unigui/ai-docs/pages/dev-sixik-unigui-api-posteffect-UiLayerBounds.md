# `UiLayerBounds` (extends `Record`)

Record holding coordinates, dimensions, and resolution scale for an offscreen post-processing layer target.

- **Purpose:** Provides normalized layer boundaries and calculates clamped offscreen texture pixel dimensions.

### USE WHEN

- Allocating intermediate render targets or calculating post-effect viewport UVs.

- **Related:** `UiPostEffectRenderer`, `UiPostEffectBackend`

