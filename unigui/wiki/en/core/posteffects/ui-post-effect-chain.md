---
title: "UiPostEffectChain"
slug: "ui-post-effect-chain"
category: "Core/PostEffects"
locale: "en"
order: 20
---

# UiPostEffectChain

Immutable pipeline reference for post-processing UI layers.

## Overview

`UiPostEffectChain` encapsulates a sequence of post-processing shader passes applied to an offscreen layer buffer. A chain can be empty (`none()`), reference a registered effect ID (`of("unigui:tint")`), or hold inline pass instances.

Backend renderers resolve chains lazily during render time. If a referenced effect ID is unregistered or post-processing is unsupported, rendering seamlessly falls back to normal direct drawing without crashing.

## Creating Effect Chains

```java
// Reference registered effect
UiPostEffectChain vignetteChain = UiPostEffectChain.of("unigui:vignette");

// No-op chain
UiPostEffectChain cleanChain = UiPostEffectChain.none();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Returns an independent copy of this chain.

Returns `UiPostEffectChain`: Resulting UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`effectId()` {: #effectid data-toc-label="effectId()" }

**effectId()**

Returns the referenced effect identifier or null.

Returns `String`: Resulting String..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`inlinePasses()` {: #inlinepasses data-toc-label="inlinePasses()" }

**inlinePasses()**

Returns the list of inline passes if defined directly.

Returns `List`: Resulting List<UiPostEffectPass>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`isNone()` {: #isnone data-toc-label="isNone()" }

**isNone()**

Returns true if this chain performs no post-processing.

Returns `boolean`: Resulting boolean..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`none()` {: #none data-toc-label="none()" }

**none()**

Returns an empty chain representing no post-processing.

Returns `UiPostEffectChain`: Resulting UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`of(List)` {: #oflist data-toc-label="of(List)" }

**of(List<UiPostEffectPass>)**

Creates an inline chain from a list of passes.

| Parameter | Type | Description |
| --- | --- | --- |
| `passes` | `List` | List of [UiPostEffectPass](<ui-post-effect-pass.md>). |

Returns `UiPostEffectChain`: Resulting UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`of(String)` {: #ofstring data-toc-label="of(String)" }

**of(String)**

Creates a chain referencing a registered effect identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `effectId` | `String` | Effect ID string. |

Returns `UiPostEffectChain`: Resulting UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`of(UiPostEffectDefinition)` {: #ofuiposteffectdefinition data-toc-label="of(UiPostEffectDefinition)" }

**of([UiPostEffectDefinition](<ui-post-effect-definition.md>))**

Creates a chain holding passes from a definition.

| Parameter | Type | Description |
| --- | --- | --- |
| `definition` | `UiPostEffectDefinition` | Post-effect definition. |

Returns `UiPostEffectChain`: Resulting UiPostEffectChain..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectChain::</span>`resolve(UiPostEffectRegistry)` {: #resolveuiposteffectregistry data-toc-label="resolve(UiPostEffectRegistry)" }

**resolve([UiPostEffectRegistry](<ui-post-effect-registry.md>))**

Resolves the chain into an ordered list of passes via the registry.

| Parameter | Type | Description |
| --- | --- | --- |
| `registry` | `UiPostEffectRegistry` | [UiPostEffectRegistry](<ui-post-effect-registry.md>) source. |

Returns `List`: Resulting List<UiPostEffectPass>..

</div>

