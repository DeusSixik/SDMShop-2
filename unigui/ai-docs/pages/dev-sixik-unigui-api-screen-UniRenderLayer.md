# `UniRenderLayer` (implements `AutoCloseable`)

Configuration container for isolated HUD and overlay UI layers in UniGUI.

- **Purpose:** Encapsulates root widget, UIContext, post-processing chains, UI scaling, and lifecycle invalidation.

### USE WHEN

- Defining custom HUD layers or screen overlays before registration.

- **Related:** `UniHudRender`, `UniScreenOverlayRender`, `UniRenderTarget`, `UiPostEffectChain`

