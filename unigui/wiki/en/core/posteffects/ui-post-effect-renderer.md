---
title: "UiPostEffectRenderer"
slug: "ui-post-effect-renderer"
category: "Core/PostEffects"
locale: "en"
order: 90
---

# UiPostEffectRenderer

Rendering dispatcher with automatic fallback for post-processing UI layers.

## Overview

`UiPostEffectRenderer` dispatches layer rendering to the active backend. If the effect chain is empty or the backend does not implement `UiPostEffectBackend`, it automatically falls back to standard direct `RenderBackend#render` without throwing exceptions.

## Executing Post-Processed Render

```java
UiPostEffectRenderer.render(
    backend,
    drawList,
    UiLayerBounds.viewport(width, height),
    chain
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRenderer::</span>`render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain)` {: #renderrenderbackend-drawlist-uilayerbounds-uiposteffectchain data-toc-label="render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain)" }

**render(RenderBackend, [DrawList](<../render/draw-list.md>), [UiLayerBounds](<ui-layer-bounds.md>), [UiPostEffectChain](<ui-post-effect-chain.md>))**

Renders a draw list with post-processing fallback to active screen target.

| Parameter | Type | Description |
| --- | --- | --- |
| `backend` | `RenderBackend` | Active RenderBackend. |
| `drawList` | `DrawList` | [DrawList](<../render/draw-list.md>) of commands. |
| `bounds` | `UiLayerBounds` | Layer bounds. |
| `chain` | `UiPostEffectChain` | Post effect chain. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRenderer::</span>`render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain, RenderTarget)` {: #renderrenderbackend-drawlist-uilayerbounds-uiposteffectchain-rendertarget data-toc-label="render(RenderBackend, DrawList, UiLayerBounds, UiPostEffectChain, RenderTarget)" }

**render(RenderBackend, [DrawList](<../render/draw-list.md>), [UiLayerBounds](<ui-layer-bounds.md>), [UiPostEffectChain](<ui-post-effect-chain.md>), RenderTarget)**

Renders a draw list with post-processing fallback to a specified render target.

| Parameter | Type | Description |
| --- | --- | --- |
| `backend` | `RenderBackend` | Active RenderBackend. |
| `drawList` | `DrawList` | [DrawList](<../render/draw-list.md>) of commands. |
| `bounds` | `UiLayerBounds` | Layer bounds. |
| `chain` | `UiPostEffectChain` | Post effect chain. |
| `target` | `RenderTarget` | Target RenderTarget or null for active. |

</div>

