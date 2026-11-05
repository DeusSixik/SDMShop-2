# `ShaderUniform`

Immutable container holding a single typed shader parameter payload (float, int, vector, or mat4).

- **Purpose:** Wraps typed shader uniform values with defensive copying so graphics draw commands remain immutable.

### USE WHEN

- Construct individual uniforms using `ShaderUniform.float1()`, `vec2()`, `vec3()`, `vec4()`, or `mat4()`.
- Inspect uniform types and raw float arrays when writing custom graphics backend adapters.

### CRITICAL RULES & GOTCHAS:

- Array values are defensively copied on creation and extraction.
- `mat4` requires an array of exactly 16 float values.

- **Related:** `ShaderUniforms`, `ShaderHandle`, `DrawScope`

