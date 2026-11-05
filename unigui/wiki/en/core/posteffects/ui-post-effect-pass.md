---
title: "UiPostEffectPass"
slug: "ui-post-effect-pass"
category: "Core/PostEffects"
locale: "en"
order: 40
---

# UiPostEffectPass

Single shader pass configuration within a post-processing pipeline.

## Overview

`UiPostEffectPass` specifies the render state for one shader invocation in a chain. It pairs a `ShaderHandle` with uniform variables (`UiPostEffectUniforms`), optional secondary sampler textures (`TextureHandle`), and blend mode (`UiPostEffectBlendMode`).

Standard inputs (`SourceTexture`, `SourceSize`, `SourceFlipY`) are injected automatically by the rendering backend.

## Configuring a Pass with Uniforms and Textures

```java
UiPostEffectPass pass = UiPostEffectPass.shader("mymod:overlay")
    .blendMode(UiPostEffectBlendMode.ALPHA_BLEND)
    .texture("NoiseTexture", noiseTexHandle)
    .uniforms(u -> u.floatValue("intensity", 0.75f));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`blendMode()` {: #blendmode data-toc-label="blendMode()" }

**blendMode()**

Returns the blend mode of this pass.

Returns `UiPostEffectBlendMode`: Resulting [UiPostEffectBlendMode](<ui-post-effect-blend-mode.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`blendMode(UiPostEffectBlendMode)` {: #blendmodeuiposteffectblendmode data-toc-label="blendMode(UiPostEffectBlendMode)" }

**blendMode([UiPostEffectBlendMode](<ui-post-effect-blend-mode.md>))**

Returns a copy of this pass with updated blend mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `blendMode` | `UiPostEffectBlendMode` | [UiPostEffectBlendMode](<ui-post-effect-blend-mode.md>). |

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Returns an independent copy of this pass.

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shader()` {: #shader data-toc-label="shader()" }

**shader()**

Returns a copy of the shader handle.

Returns `ShaderHandle`: Resulting [ShaderHandle](<../shaders/shader-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shader(ShaderHandle)` {: #shadershaderhandle data-toc-label="shader(ShaderHandle)" }

**shader([ShaderHandle](<../shaders/shader-handle.md>))**

Creates a pass from a [ShaderHandle](<../shaders/shader-handle.md>) instance.

| Parameter | Type | Description |
| --- | --- | --- |
| `shader` | `ShaderHandle` | [ShaderHandle](<../shaders/shader-handle.md>) instance. |

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shader(String)` {: #shaderstring data-toc-label="shader(String)" }

**shader(String)**

Creates a pass from a shader resource identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `shaderId` | `String` | Shader resource ID. |

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`shaderId()` {: #shaderid data-toc-label="shaderId()" }

**shaderId()**

Returns the stable shader identifier.

Returns `String`: Resulting String..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`texture(String, TextureHandle)` {: #texturestring-texturehandle data-toc-label="texture(String, TextureHandle)" }

**texture(String, [TextureHandle](<../textures/texture-handle.md>))**

Returns a copy of this pass with an additional sampler texture.

| Parameter | Type | Description |
| --- | --- | --- |
| `uniformName` | `String` | Sampler uniform name. |
| `texture` | `TextureHandle` | [TextureHandle](<../textures/texture-handle.md>) instance. |

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`textures()` {: #textures data-toc-label="textures()" }

**textures()**

Returns additional sampler textures keyed by uniform name.

Returns `Map`: Resulting Map<String, TextureHandle>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`uniforms()` {: #uniforms data-toc-label="uniforms()" }

**uniforms()**

Returns a copy of the pass uniforms.

Returns `UiPostEffectUniforms`: Resulting [UiPostEffectUniforms](<ui-post-effect-uniforms.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`uniforms(Consumer)` {: #uniformsconsumer data-toc-label="uniforms(Consumer)" }

**uniforms(Consumer<UiPostEffectUniforms>)**

Returns a copy of this pass with uniforms configured via callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `config` | `Consumer` | Configuration consumer. |

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectPass::</span>`uniforms(UiPostEffectUniforms)` {: #uniformsuiposteffectuniforms data-toc-label="uniforms(UiPostEffectUniforms)" }

**uniforms([UiPostEffectUniforms](<ui-post-effect-uniforms.md>))**

Returns a copy of this pass with updated uniforms.

| Parameter | Type | Description |
| --- | --- | --- |
| `uniforms` | `UiPostEffectUniforms` | New uniforms instance. |

Returns `UiPostEffectPass`: Resulting UiPostEffectPass..

</div>

