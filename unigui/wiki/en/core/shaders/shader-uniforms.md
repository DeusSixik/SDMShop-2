---
title: "ShaderUniforms"
slug: "shader-uniforms"
category: "Core/Shaders"
locale: "en"
order: 30
---

# ShaderUniforms

Mutable collection of named shader parameters (floats, ints, vectors, colors) passed to GPU programs.

## Overview & Insertion Order

`ShaderUniforms` holds named uniform values supplied to a shader draw command.

- **Insertion Order Preserved**: Backends iterate uniforms in stable insertion order, simplifying diagnostics and batching.
- **Defensive Copying**: Values are copied upon insertion, so callers can reuse temporary uniform or color objects.
- **Fluent Convenience API**: Direct setters for `float`, `int`, `vec2`, `vec3`, `vec4`, `ColorView`, and ARGB integers.

## Supported Uniform Types

| Type | Setter Method | GLSL Equivalent |
| --- | --- | --- |
| `float` | `setFloat(name, val)` | `uniform float name;` |
| `int` | `setInt(name, val)` | `uniform int name;` |
| `vec2` | `setVec2(name, x, y)` | `uniform vec2 name;` |
| `vec3` | `setVec3(name, x, y, z)` | `uniform vec3 name;` |
| `vec4` | `setVec4(name, x, y, z, w)` | `uniform vec4 name;` |
| `ColorView` | `setColor(name, color)` | `uniform vec4 name;` (RGBA normalized 0..1) |

## Populating Shader Uniforms

```java
ShaderUniforms uniforms = ShaderUniforms.create()
        .setVec2("u_resolution", width, height)
        .setFloat("u_time", (System.currentTimeMillis() % 10000) / 1000.0f)
        .setColor("u_tint", ColorView.rgb(0, 180, 255));

scope.shader(waterShader, x, y, width, height, uniforms, ShaderDrawOptions.defaults());
```

## API

### Factory Methods

| Method | Description |
| --- | --- |
| [`empty()`](#empty) | Returns a new empty ShaderUniforms collection. |
| [`create()`](#create) | Returns a new mutable ShaderUniforms collection. |
| [`clear()`](#clear) | Removes all recorded uniforms without allocating a new collection. |
| [`copy()`](#copy) | Creates an independent deep duplicate of this collection. |
| [`copyFrom(ShaderUniforms)`](#copyfromshaderuniforms) | Copies uniforms from source without allocating new instances. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`empty()` {: #empty data-toc-label="empty()" }

**Create Empty Uniforms**

Returns a new empty ShaderUniforms collection.

Returns `ShaderUniforms`: Empty ShaderUniforms instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`create()` {: #create data-toc-label="create()" }

**Create Uniforms**

Returns a new mutable ShaderUniforms collection.

Returns `ShaderUniforms`: New ShaderUniforms instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`clear()` {: #clear data-toc-label="clear()" }

**Clear Uniforms**

Removes all recorded uniforms without allocating a new collection.

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`copy()` {: #copy data-toc-label="copy()" }

**Copy Uniforms**

Creates an independent deep duplicate of this collection.

Returns `ShaderUniforms`: New ShaderUniforms copy..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`copyFrom(ShaderUniforms)` {: #copyfromshaderuniforms data-toc-label="copyFrom(ShaderUniforms)" }

**Copy from Source Collection**

Copies uniforms from source without allocating new instances.

| Parameter | Type | Description |
| --- | --- | --- |
| `source` | `ShaderUniforms` | Source uniforms collection, or null to clear. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

### Uniform Setters

| Method | Description |
| --- | --- |
| [`set(String, ShaderUniform)`](#setstring-shaderuniform) | Stores a ShaderUniform value under the given name. |
| [`setFloat(String, float)`](#setfloatstring-float) | Stores a single float uniform under the given name. |
| [`setInt(String, int)`](#setintstring-int) | Stores an integer uniform under the given name. |
| [`setVec2(String, float, float)`](#setvec2string-float-float) | Stores a two-component vector (x, y) under the given name. |
| [`setVec3(String, float, float, float)`](#setvec3string-float-float-float) | Stores a three-component vector (x, y, z) under the given name. |
| [`setVec4(String, float, float, float, float)`](#setvec4string-float-float-float-float) | Stores a four-component vector (x, y, z, w) under the given name. |
| [`setColor(String, ColorView)`](#setcolorstring-colorview) | Converts ColorView to a normalized vec4 (r, g, b, a) uniform. |
| [`setColorArgb(String, int)`](#setcolorargbstring-int) | Converts packed ARGB integer into a normalized vec4 uniform. |
| [`setMat4(String, float[])`](#setmat4string-float) | Stores a 4x4 matrix uniform from an array of 16 float values. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`set(String, ShaderUniform)` {: #setstring-shaderuniform data-toc-label="set(String, ShaderUniform)" }

**Set ShaderUniform**

Stores a ShaderUniform value under the given name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `uniform` | `ShaderUniform` | ShaderUniform object containing data payload. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setFloat(String, float)` {: #setfloatstring-float data-toc-label="setFloat(String, float)" }

**Set Float Uniform**

Stores a single float uniform under the given name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `value` | `float` | Primitive float or int value. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setInt(String, int)` {: #setintstring-int data-toc-label="setInt(String, int)" }

**Set Int Uniform**

Stores an integer uniform under the given name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `value` | `int` | Primitive float or int value. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setVec2(String, float, float)` {: #setvec2string-float-float data-toc-label="setVec2(String, float, float)" }

**Set Vec2 Uniform**

Stores a two-component vector (x, y) under the given name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `x` | `float` | First vector component (X). |
| `y` | `float` | Second vector component (Y). |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setVec3(String, float, float, float)` {: #setvec3string-float-float-float data-toc-label="setVec3(String, float, float, float)" }

**Set Vec3 Uniform**

Stores a three-component vector (x, y, z) under the given name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `x` | `float` | First vector component (X). |
| `y` | `float` | Second vector component (Y). |
| `z` | `float` | Third vector component (Z). |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setVec4(String, float, float, float, float)` {: #setvec4string-float-float-float-float data-toc-label="setVec4(String, float, float, float, float)" }

**Set Vec4 Uniform**

Stores a four-component vector (x, y, z, w) under the given name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `x` | `float` | First vector component (X). |
| `y` | `float` | Second vector component (Y). |
| `z` | `float` | Third vector component (Z). |
| `w` | `float` | Fourth vector component (W). |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setColor(String, ColorView)` {: #setcolorstring-colorview data-toc-label="setColor(String, ColorView)" }

**Set Color Uniform**

Converts ColorView to a normalized vec4 (r, g, b, a) uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `color` | `ColorView` | ColorView source for RGBA uniform. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setColorArgb(String, int)` {: #setcolorargbstring-int data-toc-label="setColorArgb(String, int)" }

**Set ARGB Color Uniform**

Converts packed ARGB integer into a normalized vec4 uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `argb` | `int` | Packed ARGB 32-bit integer. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`setMat4(String, float[])` {: #setmat4string-float data-toc-label="setMat4(String, float[])" }

**Set Mat4 Uniform**

Stores a 4x4 matrix uniform from an array of 16 float values.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Uniform parameter identifier name in shader source. |
| `values` | `float[]` | Array of exactly 16 float matrix values. |

Returns `ShaderUniforms`: This collection for fluent chaining..

</div>

### Access & Inspection

| Method | Description |
| --- | --- |
| [`values()`](#values) | Returns an unmodifiable map of all registered uniforms in insertion order. |
| [`isEmpty()`](#isempty) | Returns true if no uniforms have been added. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`values()` {: #values data-toc-label="values()" }

**Get Uniforms Map**

Returns an unmodifiable map of all registered uniforms in insertion order.

Returns `Map`: Unmodifiable Map of names to ShaderUniform instances..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderUniforms::</span>`isEmpty()` {: #isempty data-toc-label="isEmpty()" }

**Check If Empty**

Returns true if no uniforms have been added.

Returns `boolean`: True if collection contains zero uniforms..

</div>

