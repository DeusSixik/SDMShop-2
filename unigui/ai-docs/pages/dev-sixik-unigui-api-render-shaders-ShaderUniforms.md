# `ShaderUniforms`

Mutable collection of named shader parameters (floats, ints, vectors, colors) passed to GPU programs.

- **Purpose:** Supplies procedural data, animation timers, screen resolutions, and tint colors to custom shaders in a fluent, safe container.

### USE WHEN

- Pass dynamic inputs to shaders in `DrawScope#shader` via `ShaderUniforms.create()`.
- Supply resolution (`setVec2`), time (`setFloat`), or colors (`setColor`) to fragment shaders.

### CRITICAL RULES & GOTCHAS:

- Uniform values are defensively copied upon insertion; caller objects can be safely reused.
- Insertion order is preserved in an internal LinkedHashMap for consistent backend binding.

- **Related:** `ShaderHandle`, `ShaderUniform`, `ShaderDrawOptions`, `DrawScope`

### EXAMPLES

#### Populate shader uniforms

```java
ShaderUniforms uniforms = ShaderUniforms.create()
        .setVec2("u_resolution", width, height)
        .setFloat("u_time", time)
        .setColor("u_tint", ColorView.CYAN);
```

