---
title: "UiPostEffectUniforms"
slug: "ui-post-effect-uniforms"
category: "Core/PostEffects"
locale: "en"
order: 50
---

# UiPostEffectUniforms

Container for post-processing shader uniforms with static values and dynamic suppliers.

## Overview

`UiPostEffectUniforms` provides a fluent builder for binding uniforms to a post-effect pass. Unlike standard static uniforms, it supports dynamic suppliers (`Supplier<Float>`, `Supplier<ShaderUniform>`), allowing real-time animation (such as time-based wave phases or game health percentages) without rebuilding the pass.

## Binding Dynamic Uniforms

```java
UiPostEffectUniforms uniforms = UiPostEffectUniforms.create()
    .floatValue("intensity", 0.85f)
    .vec2("center", 0.5f, 0.5f)
    .floatSupplier("time", () -> (System.currentTimeMillis() % 10000) / 1000.0f);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`boolValue(String, boolean)` {: #boolvaluestring-boolean data-toc-label="boolValue(String, boolean)" }

**boolValue(String, boolean)**

Sets a boolean uniform as 0 or 1.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `value` | `boolean` | Boolean value. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`color(String, ColorView)` {: #colorstring-colorview data-toc-label="color(String, ColorView)" }

**color(String, ColorView)**

Sets a color as a 4-component vector uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `color` | `ColorView` | ColorView instance. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`copy()` {: #copy data-toc-label="copy()" }

**copy()**

Returns an independent copy of this container.

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`create()` {: #create data-toc-label="create()" }

**create()**

Creates a new empty uniforms container.

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`empty()` {: #empty data-toc-label="empty()" }

**empty()**

Returns an empty uniforms container.

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`floatSupplier(String, Supplier)` {: #floatsupplierstring-supplier data-toc-label="floatSupplier(String, Supplier)" }

**floatSupplier(String, Supplier<Float>)**

Sets a dynamic float supplier uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `supplier` | `Supplier` | Float supplier. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`floatValue(String, float)` {: #floatvaluestring-float data-toc-label="floatValue(String, float)" }

**floatValue(String, float)**

Sets a static float uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `value` | `float` | Float value. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`intValue(String, int)` {: #intvaluestring-int data-toc-label="intValue(String, int)" }

**intValue(String, int)**

Sets a static integer uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `value` | `int` | Int value. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`isEmpty()` {: #isempty data-toc-label="isEmpty()" }

**isEmpty()**

Returns true if this container has no uniforms.

Returns `boolean`: Resulting boolean..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`resolve()` {: #resolve data-toc-label="resolve()" }

**resolve()**

Resolves all static and dynamic values into a [ShaderUniforms](<../shaders/shader-uniforms.md>) snapshot.

Returns `ShaderUniforms`: Resulting [ShaderUniforms](<../shaders/shader-uniforms.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`set(String, ShaderUniform)` {: #setstring-shaderuniform data-toc-label="set(String, ShaderUniform)" }

**set(String, ShaderUniform)**

Sets a static shader uniform value.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `uniform` | `ShaderUniform` | ShaderUniform value. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`setDynamic(String, Supplier)` {: #setdynamicstring-supplier data-toc-label="setDynamic(String, Supplier)" }

**setDynamic(String, Supplier<ShaderUniform>)**

Sets a dynamic supplier evaluated before each pass.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `supplier` | `Supplier` | Supplier function. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`vec2(String, float, float)` {: #vec2string-float-float data-toc-label="vec2(String, float, float)" }

**vec2(String, float, float)**

Sets a 2-component vector uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `x` | `float` | X component. |
| `y` | `float` | Y component. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`vec3(String, float, float, float)` {: #vec3string-float-float-float data-toc-label="vec3(String, float, float, float)" }

**vec3(String, float, float, float)**

Sets a 3-component vector uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `x` | `float` | X component. |
| `y` | `float` | Y component. |
| `z` | `float` | Z component. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectUniforms::</span>`vec4(String, float, float, float, float)` {: #vec4string-float-float-float-float data-toc-label="vec4(String, float, float, float, float)" }

**vec4(String, float, float, float, float)**

Sets a 4-component vector uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform name. |
| `x` | `float` | X component. |
| `y` | `float` | Y component. |
| `z` | `float` | Z component. |
| `w` | `float` | W component. |

Returns `UiPostEffectUniforms`: Resulting UiPostEffectUniforms..

</div>

