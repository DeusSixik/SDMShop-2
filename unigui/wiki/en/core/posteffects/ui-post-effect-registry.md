---
title: "UiPostEffectRegistry"
slug: "ui-post-effect-registry"
category: "Core/PostEffects"
locale: "en"
order: 60
---

# UiPostEffectRegistry

Registry repository storing named post-effect definitions.

## Overview

`UiPostEffectRegistry` stores `UiPostEffectDefinition` instances keyed by string identifier. The singleton instance `UiPostEffectRegistry.global()` is accessed across code, XML layouts, and StylePacks to resolve effect pipelines by name.

## Registering Custom Post Effect

```java
UiPostEffectDefinition crtDef = UiPostEffectDefinition.create("mymod:crt")
    .pass("mymod:scanlines")
    .pass("mymod:curvature")
    .build();
UiPostEffectRegistry.global().register(crtDef);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`definitions()` {: #definitions data-toc-label="definitions()" }

**definitions()**

Returns a snapshot list of all registered definitions.

Returns `List`: Resulting List<UiPostEffectDefinition>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`find(String)` {: #findstring data-toc-label="find(String)" }

**find(String)**

Looks up an effect definition by identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Effect identifier. |

Returns `Optional`: Resulting Optional<UiPostEffectDefinition>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`global()` {: #global data-toc-label="global()" }

**global()**

Returns the global post-effect registry singleton.

Returns `UiPostEffectRegistry`: Resulting UiPostEffectRegistry..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`ids()` {: #ids data-toc-label="ids()" }

**ids()**

Returns an immutable list of all registered effect identifiers.

Returns `List`: Resulting List<String>..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`register(UiPostEffectDefinition)` {: #registeruiposteffectdefinition data-toc-label="register(UiPostEffectDefinition)" }

**register([UiPostEffectDefinition](<ui-post-effect-definition.md>))**

Registers an effect definition replacing any previous registration with the same ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `definition` | `UiPostEffectDefinition` | Definition to register. |

Returns `UiPostEffectRegistry`: Resulting UiPostEffectRegistry..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectRegistry::</span>`unregister(String)` {: #unregisterstring data-toc-label="unregister(String)" }

**unregister(String)**

Removes an effect definition by identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Effect identifier. |

Returns `boolean`: Resulting boolean..

</div>

