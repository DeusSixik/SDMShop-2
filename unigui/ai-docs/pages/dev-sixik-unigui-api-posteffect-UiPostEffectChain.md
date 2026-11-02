# `UiPostEffectChain`

Immutable pipeline reference encapsulating post-processing shader passes for a UI layer.

- **Purpose:** Allows layers to reference named effects by ID or attach inline shader passes with lazy backend resolution.

### USE WHEN

- Attaching post-processing effects to UniRenderLayer or passing to UiPostEffectRenderer.

- **Related:** `UiPostEffects`, `UiPostEffectDefinition`, `UniRenderLayer`

