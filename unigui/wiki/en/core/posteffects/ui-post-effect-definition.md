---
title: "UiPostEffectDefinition"
slug: "ui-post-effect-definition"
category: "Core/PostEffects"
locale: "en"
order: 30
---

# UiPostEffectDefinition

Immutable specification of a post-processing effect with ordered shader passes.

## Overview

`UiPostEffectDefinition` describes a named effect containing one or more shader passes (`UiPostEffectPass`) executed in sequence. Multi-pass effects (such as two-pass Gaussian blur or bloom) ping-pong between intermediate texture targets before writing to the final destination.

## Building a Multi-Pass Effect

```java
UiPostEffectDefinition customBloom = UiPostEffectDefinition.create("mymod:bloom")
    .pass("mymod:bright_pass")
    .pass("mymod:blur_h", uniforms -> uniforms.floatValue("radius", 4.0f))
    .pass("mymod:blur_v", uniforms -> uniforms.floatValue("radius", 4.0f))
    .build();
UiPostEffectRegistry.global().register(customBloom);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Returns an independent copy of this definition.

Returns `UiPostEffectDefinition`: Resulting UiPostEffectDefinition..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`create(String)` {: #createstring data-toc-label="create(String)" }

**create(String)**

Begins building a new post-effect definition.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique effect identifier. |

Returns `Builder`: Resulting UiPostEffectDefinition.Builder..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`enabledByDefault()` {: #enabledbydefault data-toc-label="enabledByDefault()" }

**enabledByDefault()**

Returns true if this effect is enabled by default in presets.

Returns `boolean`: Resulting boolean..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`id()` {: #id data-toc-label="id()" }

**id()**

Returns the unique effect identifier.

Returns `String`: Resulting String..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`passes()` {: #passes data-toc-label="passes()" }

**passes()**

Returns an unmodifiable list of shader passes.

Returns `List`: Resulting List<UiPostEffectPass>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectDefinition::</span>`singlePass(String)` {: #singlepassstring data-toc-label="singlePass(String)" }

**singlePass(String)**

Creates a single-pass definition using the effect ID as shader ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Effect identifier. |

Returns `UiPostEffectDefinition`: Resulting UiPostEffectDefinition..

</div>

