---
title: "ShaderDrawOptions"
slug: "shader-draw-options"
category: "Core/Shaders"
locale: "en"
order: 20
---

# ShaderDrawOptions

Backend-facing configuration for shader draw commands governing blending, built-in uniforms, and vertex offsets.

## Overview & Hardware State

`ShaderDrawOptions` configures GPU render state when executing custom shaders via `DrawScope#shader`.

- **Built-in Uniforms**: Automatically provides resolution, model-view projection matrices, and default inputs when `builtinUniforms = true`.
- **Blending**: Directs the backend whether to enable alpha blending during the shader pass.
- **Vertex Offset**: Adjusts coordinate alignment for fullscreen and sub-rect quad shaders.

## Built-in Uniforms & SquareVertex

When `builtinUniforms()` is enabled, the active backend automatically injects default shader parameters into the execution context:

- `SquareVertex`: Normalized vertex offset (default `-0.25f` preserving legacy quad shader compatibility).
- Standard projection and model-view matrices supplied by the underlying engine.

## Configuring Shader State

```java
ShaderDrawOptions options = ShaderDrawOptions.defaults()
        .blend(true)
        .builtinUniforms(true);

scope.shader(portalShader, x, y, width, height, uniforms, options);
```

## API

### Factory Methods

| Method | Description |
| --- | --- |
| [`defaults()`](#defaults) | Returns a new ShaderDrawOptions instance initialized with default blending and builtins. |
| [`copy()`](#copy) | Creates an independent duplicate of this ShaderDrawOptions instance. |
| [`copyFrom(ShaderDrawOptions)`](#copyfromshaderdrawoptions) | Copies options from another ShaderDrawOptions instance without allocations. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**Get Default Options**

Returns a new ShaderDrawOptions instance initialized with default blending and builtins.

Returns `ShaderDrawOptions`: New ShaderDrawOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`copy()` {: #copy data-toc-label="copy()" }

**Copy Options**

Creates an independent duplicate of this ShaderDrawOptions instance.

Returns `ShaderDrawOptions`: New ShaderDrawOptions duplicate..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`copyFrom(ShaderDrawOptions)` {: #copyfromshaderdrawoptions data-toc-label="copyFrom(ShaderDrawOptions)" }

**Copy from Source**

Copies options from another ShaderDrawOptions instance without allocations.

| Parameter | Type | Description |
| --- | --- | --- |
| `source` | `ShaderDrawOptions` | Source options object, or null for default values. |

Returns `ShaderDrawOptions`: This options object for chaining..

</div>

### Render State Configuration

| Method | Description |
| --- | --- |
| [`builtinUniforms()`](#builtinuniforms) | Returns true if the backend should automatically supply built-in uniforms. |
| [`builtinUniforms(boolean)`](#builtinuniformsboolean) | Enables or disables automatic built-in uniforms injection. |
| [`blend()`](#blend) | Returns true if alpha blending should be enabled during the shader draw pass. |
| [`blend(boolean)`](#blendboolean) | Configures whether alpha blending is active for this shader command. |
| [`squareVertexOffset()`](#squarevertexoffset) | Returns the vertex coordinate offset for the SquareVertex uniform. |
| [`squareVertexOffset(float)`](#squarevertexoffsetfloat) | Sets the vertex coordinate offset for the SquareVertex uniform. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`builtinUniforms()` {: #builtinuniforms data-toc-label="builtinUniforms()" }

**Check Built-in Uniforms**

Returns true if the backend should automatically supply built-in uniforms.

Returns `boolean`: True if built-in uniforms are enabled..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`builtinUniforms(boolean)` {: #builtinuniformsboolean data-toc-label="builtinUniforms(boolean)" }

**Set Built-in Uniforms**

Enables or disables automatic built-in uniforms injection.

| Parameter | Type | Description |
| --- | --- | --- |
| `builtinUniforms` | `boolean` | True to inject default engine uniforms automatically. |

Returns `ShaderDrawOptions`: This options object for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`blend()` {: #blend data-toc-label="blend()" }

**Check Blending Enabled**

Returns true if alpha blending should be enabled during the shader draw pass.

Returns `boolean`: True if blending is enabled..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`blend(boolean)` {: #blendboolean data-toc-label="blend(boolean)" }

**Set Blending**

Configures whether alpha blending is active for this shader command.

| Parameter | Type | Description |
| --- | --- | --- |
| `blend` | `boolean` | True to enable alpha blending. |

Returns `ShaderDrawOptions`: This options object for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`squareVertexOffset()` {: #squarevertexoffset data-toc-label="squareVertexOffset()" }

**Get SquareVertex Offset**

Returns the vertex coordinate offset for the SquareVertex uniform.

Returns `float`: Offset float value..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderDrawOptions::</span>`squareVertexOffset(float)` {: #squarevertexoffsetfloat data-toc-label="squareVertexOffset(float)" }

**Set SquareVertex Offset**

Sets the vertex coordinate offset for the SquareVertex uniform.

| Parameter | Type | Description |
| --- | --- | --- |
| `squareVertexOffset` | `float` | Vertex coordinate offset float value. |

Returns `ShaderDrawOptions`: This options object for chaining..

</div>

