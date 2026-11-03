---
title: "UiPostEffects"
slug: "ui-post-effects"
category: "Core/PostEffects"
locale: "en"
order: 10
---

# UiPostEffects

Built-in catalog of standard post-processing effects and embedded shader sources.

## Overview

`UiPostEffects` provides ready-to-use post-processing effect definitions and embedded GLSL shaders for HUD layers, overlays, and modal screens.

### Built-in Effect Presets

- `unigui:passthrough`: Identity copy pass without visual modification.
- `unigui:tint`: Color overlay tint with customizable color and intensity amount.
- `unigui:vignette`: Radial border darkening with configurable radius, softness, and color.
- `unigui:barrel_distortion`: Curved lens warping for helmet, visor, or CRT monitor simulation.
- `unigui:chromatic_aberration`: RGB color channel separation along directional vectors.
- `unigui:scanline`: Animated horizontal scanlines simulating retro displays.

## Registering and Applying Built-in Effects

Call `UiPostEffects.ensureRegistered()` to register standard definitions and the built-in shader provider into global registries. Applying an effect to a layer is as simple as calling `layer.postEffect("unigui:vignette")`.

## Applying Vignette to a HUD Layer

```java
UniRenderLayer layer = new UniRenderLayer(myHudWidget)
    .postEffect(UiPostEffects.chain(UiPostEffects.VIGNETTE));
UniHudRender.register(layer);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`barrelDistortion()` {: #barreldistortion data-toc-label="barrelDistortion()" }

**barrelDistortion()**

Returns definition for barrel distortion lens effect.

Returns `UiPostEffectDefinition`: Resulting [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`chain(String)` {: #chainstring data-toc-label="chain(String)" }

**chain(String)**

Creates an effect chain referencing the specified effect ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `effectId` | `String` | Identifier of registered effect. |

Returns `UiPostEffectChain`: Resulting [UiPostEffectChain](<ui-post-effect-chain.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`chromaticAberration()` {: #chromaticaberration data-toc-label="chromaticAberration()" }

**chromaticAberration()**

Returns definition for chromatic aberration RGB split effect.

Returns `UiPostEffectDefinition`: Resulting [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`ensureRegistered()` {: #ensureregistered data-toc-label="ensureRegistered()" }

**ensureRegistered()**

Registers all built-in post effects and shaders in the global registry.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`passthrough()` {: #passthrough data-toc-label="passthrough()" }

**passthrough()**

Returns definition for passthrough effect without visual changes.

Returns `UiPostEffectDefinition`: Resulting [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`scanline()` {: #scanline data-toc-label="scanline()" }

**scanline()**

Returns definition for animated scanline retro display effect.

Returns `UiPostEffectDefinition`: Resulting [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`shader(String)` {: #shaderstring data-toc-label="shader(String)" }

**shader(String)**

Returns a shader handle referencing a built-in post-effect shader.

| Parameter | Type | Description |
| --- | --- | --- |
| `effectId` | `String` | Identifier of registered shader. |

Returns `ShaderHandle`: Resulting [ShaderHandle](<../shaders/shader-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`tint()` {: #tint data-toc-label="tint()" }

**tint()**

Returns definition for color tint effect.

Returns `UiPostEffectDefinition`: Resulting [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffects::</span>`vignette()` {: #vignette data-toc-label="vignette()" }

**vignette()**

Returns definition for radial edge vignette effect.

Returns `UiPostEffectDefinition`: Resulting [UiPostEffectDefinition](<ui-post-effect-definition.md>)..

</div>

