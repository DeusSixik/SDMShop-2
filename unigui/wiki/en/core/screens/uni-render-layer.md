---
title: "UniRenderLayer"
slug: "uni-render-layer"
category: "Core/Screens"
locale: "en"
order: 20
---

# UniRenderLayer

Minecraft-independent configuration for an overlay or HUD UI layer.

## Overview

`UniRenderLayer` represents an isolated layer rendered either in the in-game HUD or over an active GUI screen. It binds a root `Widget`, an optional `UIContext`, custom post-processing shader chains (`UiPostEffectChain`), render policies, and lifecycle hooks.

## Post-Processing Effects

Layers support post-processing filters via `postEffect(...)`, applying Gaussian blur, CRT scanlines, bloom, or color grading directly to the rendered layer buffer before blitting to the framebuffer.

## Cache Invalidation and Lifecycle

Layers implementing `AutoCloseable` can be removed from rendering via `close()`. When rendered with dirty-tracking, `invalidateRenderCache()` signals the runtime to redraw the layer content.

## Configuring a HUD Layer with Bloom

```java
Widget hudWidget = new CompassWidget();
UniRenderLayer layer = new UniRenderLayer(hudWidget)
    .renderPolicy(UiRenderPolicy.continuous())
    .postEffect("unigui:bloom");
UniHudRender.register(layer);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`attachRuntimeControl(RuntimeLayerControl)` {: #attachruntimecontrolruntimelayercontrol data-toc-label="attachRuntimeControl(RuntimeLayerControl)" }

**attachRuntimeControl(RuntimeLayerControl)**

Internal hook attaching a runtime control controller.

| Parameter | Type | Description |
| --- | --- | --- |
| `control` | `RuntimeLayerControl` | RuntimeLayerControl instance. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`clearPostEffect()` {: #clearposteffect data-toc-label="clearPostEffect()" }

**clearPostEffect()**

Clears all post-processing effects on this layer.

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`close()` {: #close data-toc-label="close()" }

**close()**

Closes this layer and detaches it from the runtime.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`closed()` {: #closed data-toc-label="closed()" }

**closed()**

Returns true if this render layer has been closed.

Returns `boolean`: True if closed..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`context()` {: #context data-toc-label="context()" }

**context()**

Returns the UI context bound to this layer.

Returns `UIContext`: UIContext or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`debugHotkeysEnabled()` {: #debughotkeysenabled data-toc-label="debugHotkeysEnabled()" }

**debugHotkeysEnabled()**

Returns true if debug layout hotkeys are enabled for this layer.

Returns `boolean`: True if debug hotkeys are active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`debugHotkeysEnabled(boolean)` {: #debughotkeysenabledboolean data-toc-label="debugHotkeysEnabled(boolean)" }

**debugHotkeysEnabled(boolean)**

Enables or disables debug layout hotkeys for this layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `boolean` | True to enable debug hotkeys. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`defaultFont()` {: #defaultfont data-toc-label="defaultFont()" }

**defaultFont()**

Returns the layer default font face or null.

Returns `FontFace`: FontFace or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`defaultFont(FontFace)` {: #defaultfontfontface data-toc-label="defaultFont(FontFace)" }

**defaultFont(FontFace)**

Sets the layer default font face.

| Parameter | Type | Description |
| --- | --- | --- |
| `font` | `FontFace` | FontFace to use. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`hasCustomUiScale()` {: #hascustomuiscale data-toc-label="hasCustomUiScale()" }

**hasCustomUiScale()**

Returns true if a custom UI scale factor is configured.

Returns `boolean`: True if uiScale is finite..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`invalidateRenderCache()` {: #invalidaterendercache data-toc-label="invalidateRenderCache()" }

**invalidateRenderCache()**

Invalidates cached rendering in the attached runtime layer.

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`postEffect()` {: #posteffect data-toc-label="postEffect()" }

**postEffect()**

Returns a copy of the post-processing effect chain.

Returns `UiPostEffectChain`: [UiPostEffectChain](<../posteffects/ui-post-effect-chain.md>) copy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`postEffect(String)` {: #posteffectstring data-toc-label="postEffect(String)" }

**postEffect(String)**

Sets a single post-effect by identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `effectId` | `String` | Identifier of registered post-effect. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`postEffect(UiPostEffectChain)` {: #posteffectuiposteffectchain data-toc-label="postEffect(UiPostEffectChain)" }

**postEffect([UiPostEffectChain](<../posteffects/ui-post-effect-chain.md>))**

Sets the post-processing effect chain for this layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `chain` | `UiPostEffectChain` | Post-effect chain to apply. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`renderPolicy()` {: #renderpolicy data-toc-label="renderPolicy()" }

**renderPolicy()**

Returns the rendering policy of this layer.

Returns `UiRenderPolicy`: Active [UiRenderPolicy](<ui-render-policy.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`renderPolicy(UiRenderPolicy)` {: #renderpolicyuirenderpolicy data-toc-label="renderPolicy(UiRenderPolicy)" }

**renderPolicy([UiRenderPolicy](<ui-render-policy.md>))**

Configures the rendering policy of this layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `policy` | `UiRenderPolicy` | [UiRenderPolicy](<ui-render-policy.md>) to set. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`root()` {: #root data-toc-label="root()" }

**root()**

Returns the root widget of this layer.

Returns `Widget`: Root widget instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`scaleWithGameGui()` {: #scalewithgamegui data-toc-label="scaleWithGameGui()" }

**scaleWithGameGui()**

Returns true if the layer scales automatically with the game's GUI scale.

Returns `boolean`: True if scaling with game GUI..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`scaleWithGameGui(boolean)` {: #scalewithgameguiboolean data-toc-label="scaleWithGameGui(boolean)" }

**scaleWithGameGui(boolean)**

Configures whether this layer scales automatically with the game's GUI scale.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `boolean` | True to enable auto-scaling with game GUI. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`uiScale()` {: #uiscale data-toc-label="uiScale()" }

**uiScale()**

Returns the custom UI scaling factor or NaN if not set.

Returns `float`: Scale multiplier..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayer::</span>`uiScale(float)` {: #uiscalefloat data-toc-label="uiScale(float)" }

**uiScale(float)**

Enforces a custom UI scale multiplier for this layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `scale` | `float` | Positive finite scale factor. |

Returns `UniRenderLayer`: This layer instance for chaining..

</div>

