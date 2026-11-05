---
title: "PostProcessingLayer"
slug: "post-processing-layer"
category: "Widgets/Effects"
locale: "en"
order: 0
---

# PostProcessingLayer

[Widget](<../widget.md>) wrapper applying post-processing shader effects and rendering scales to its child content.

## Overview

`PostProcessingLayer` is a visual wrapper that intercepts rendering of its child content and applies post-processing shaders, effects, and custom scale factors.

Key capabilities:
- Custom shader and effect chains (`postEffect`, `effect`).
- Rendering scale manipulation (`effectScale`).
- Non-invasive wrapping of complex widget trees without affecting internal event propagation.

## Applying Post-Processing Effect to Child [Widget](<../widget.md>)

```java
PostProcessingLayer layer = new PostProcessingLayer();
layer.effect("unigui:bloom");
layer.addChild(contentWidget);
```

## Related links

- [PanelWidget](<../containers/panel-widget.md>): Base container for multi-child widgets managing hierarchy, deferred mutations, and rendering order.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effect()` {: #effect data-toc-label="effect()" }

Returns the effect identifier of the current post-processing chain, or empty string if none.

Returns `String`: The identifier of the active post-processing effect..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effect(String)` {: #effectstring data-toc-label="effect(String)" }

Configures a registered post-processing effect by its identifier string.

| Parameter | Type | Description |
| --- | --- | --- |
| `effectId` | `String` | Identifier of the post-processing shader effect. |

Returns `PostProcessingLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effectScale()` {: #effectscale data-toc-label="effectScale()" }

Returns the offscreen target rendering scale factor.

Returns `float`: The rendering scale factor applied by the post-processing effect..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`effectScale(float)` {: #effectscalefloat data-toc-label="effectScale(float)" }

Sets the offscreen render target scale factor relative to layer bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `effectScale` | `float` | Post-processing effect rendering scale factor. |

Returns `PostProcessingLayer`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`postEffect()` {: #posteffect data-toc-label="postEffect()" }

Returns a copy of the active post-processing effect chain.

Returns `UiPostEffectChain`: The active post-processing shader effect chain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PostProcessingLayer::</span>`postEffect(UiPostEffectChain)` {: #posteffectuiposteffectchain data-toc-label="postEffect(UiPostEffectChain)" }

Sets the post-processing effect chain applied to this layer's subtree.

| Parameter | Type | Description |
| --- | --- | --- |
| `postEffect` | `UiPostEffectChain` | Post-processing shader effect chain. |

Returns `PostProcessingLayer`: This widget instance for chaining..

</div>

