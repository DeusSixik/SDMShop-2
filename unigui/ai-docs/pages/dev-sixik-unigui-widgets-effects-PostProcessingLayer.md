# `PostProcessingLayer` (extends `PanelWidget`)

Post-processing layer widget rendering child elements into an offscreen target and executing GPU shader passes such as blur, bloom, or CRT distortion.

### API

- `effect()` → String — Returns the effect identifier of the current post-processing chain, or empty string if none.
- `effect(String)` → PostProcessingLayer (default: `none`) — Configures a registered post-processing effect by its identifier string.
- `effectScale()` → float — Returns the offscreen target rendering scale factor.
- `effectScale(float)` → PostProcessingLayer (default: `1.0`) — Sets the offscreen render target scale factor relative to layer bounds.
- `postEffect()` → UiPostEffectChain — Returns a copy of the active post-processing effect chain.
- `postEffect(UiPostEffectChain)` → PostProcessingLayer — Sets the post-processing effect chain applied to this layer's subtree.

