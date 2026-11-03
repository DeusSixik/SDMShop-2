---
title: "ShaderHandle"
slug: "shader-handle"
category: "Core/Shaders"
locale: "en"
order: 10
---

# ShaderHandle

Identifier and container for UI shaders, referencing external resource paths or embedding GLSL source code.

## Architecture & GLSL Resolution

`ShaderHandle` represents a GPU shader pipeline used by `DrawCommandType.SHADER` and `DrawScope#shader`.

### Two Operating Modes

1. **Resource Identifier (`ShaderHandle.resource(id)`)**: Refers to a named shader resource (e.g. `minecraft:rendertype_gui` or a mod shader). The active `RenderBackend` queries registered `ShaderProvider` instances to resolve vertex and fragment source.
2. **Embedded GLSL Source (`ShaderHandle.source(...)` or `fragmentSource(...)`)**: Embeds vertex and/or fragment GLSL source strings directly in the handle for rapid procedural effects without packaging loose resource files.

## Shader Execution Pipeline

When drawing via `DrawScope#shader`:

1. `ShaderHandle` identifies the program.
2. `ShaderUniforms` supplies custom uniforms (resolution, time, colors, vectors).
3. `ShaderDrawOptions` configures blending and automatic built-in uniforms (`SquareVertex`).

Backends compile and cache GLSL programs keyed by `id()`. Redundant recompilation is avoided across frames.

## Procedural Radial Gradient Shader

```java
// Define embedded fragment shader for procedural glowing portal
String glsl = """
#version 150
uniform vec2 u_resolution;
uniform float u_time;
out vec4 fragColor;

void main() {
    vec2 st = gl_FragCoord.xy / u_resolution;
    float dist = distance(st, vec2(0.5));
    float pulse = 0.5 + 0.5 * sin(u_time * 3.0);
    fragColor = vec4(0.2, 0.6, 1.0, (1.0 - smoothstep(0.1, 0.5, dist)) * pulse);
}
""";

ShaderHandle portalShader = ShaderHandle.fragmentSource("mod:portal_effect", glsl);
```

## API

### Factory Methods

| Method | Description |
| --- | --- |
| [`resource(String)`](#resourcestring) | Creates a ShaderHandle referencing an external shader resource resolved by ShaderProviders. |
| [`fragmentSource(String, String)`](#fragmentsourcestring-string) | Creates a ShaderHandle with embedded fragment shader GLSL source. |
| [`source(String, String, String)`](#sourcestring-string-string) | Creates a ShaderHandle with embedded vertex and fragment GLSL source strings. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`resource(String)` {: #resourcestring data-toc-label="resource(String)" }

**Create Resource Handle**

Creates a ShaderHandle referencing an external shader resource resolved by ShaderProviders.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Shader resource or embedded program identifier. |

Returns `ShaderHandle`: New resource ShaderHandle..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`fragmentSource(String, String)` {: #fragmentsourcestring-string data-toc-label="fragmentSource(String, String)" }

**Create Embedded Fragment Handle**

Creates a ShaderHandle with embedded fragment shader GLSL source.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Shader resource or embedded program identifier. |
| `fragmentSource` | `String` | GLSL fragment shader code. |

Returns `ShaderHandle`: New ShaderHandle with embedded fragment source..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`source(String, String, String)` {: #sourcestring-string-string data-toc-label="source(String, String, String)" }

**Create Embedded Vertex and Fragment Handle**

Creates a ShaderHandle with embedded vertex and fragment GLSL source strings.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Shader resource or embedded program identifier. |
| `vertexSource` | `String` | GLSL vertex shader code. |
| `fragmentSource` | `String` | GLSL fragment shader code. |

Returns `ShaderHandle`: New ShaderHandle with full embedded source..

</div>

### Properties & Sources

| Method | Description |
| --- | --- |
| [`id()`](#id) | Returns stable shader identifier string. |
| [`vertexSource()`](#vertexsource) | Returns embedded vertex GLSL code or null. |
| [`fragmentSource()`](#fragmentsource) | Returns embedded fragment GLSL code or null. |
| [`hasEmbeddedVertexSource()`](#hasembeddedvertexsource) | Returns true if this handle contains embedded vertex source. |
| [`hasEmbeddedFragmentSource()`](#hasembeddedfragmentsource) | Returns true if this handle contains embedded fragment source. |
| [`copy()`](#copy) | Creates an independent duplicate of this ShaderHandle. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`id()` {: #id data-toc-label="id()" }

**Get Shader ID**

Returns stable shader identifier string.

Returns `String`: Normalized shader ID string..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`vertexSource()` {: #vertexsource data-toc-label="vertexSource()" }

**Get Vertex Source**

Returns embedded vertex GLSL code or null.

Returns `String`: Vertex GLSL source string, or null..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`fragmentSource()` {: #fragmentsource data-toc-label="fragmentSource()" }

**Get Fragment Source**

Returns embedded fragment GLSL code or null.

Returns `String`: Fragment GLSL source string, or null..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`hasEmbeddedVertexSource()` {: #hasembeddedvertexsource data-toc-label="hasEmbeddedVertexSource()" }

**Has Embedded Vertex Source**

Returns true if this handle contains embedded vertex source.

Returns `boolean`: True if vertex source is present..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`hasEmbeddedFragmentSource()` {: #hasembeddedfragmentsource data-toc-label="hasEmbeddedFragmentSource()" }

**Has Embedded Fragment Source**

Returns true if this handle contains embedded fragment source.

Returns `boolean`: True if fragment source is present..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">ShaderHandle::</span>`copy()` {: #copy data-toc-label="copy()" }

**Copy ShaderHandle**

Creates an independent duplicate of this ShaderHandle.

Returns `ShaderHandle`: New ShaderHandle with identical id and sources..

</div>

