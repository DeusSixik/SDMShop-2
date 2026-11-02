# `ShaderHandle`

Identifier and container for UI shaders, referencing external resource paths or embedding GLSL source code.

- **Purpose:** Decouples shader execution commands from graphics driver shader compilation, supporting both static resources and dynamic procedural GLSL.

### USE WHEN

- Execute custom GPU shaders via `DrawScope#shader(ShaderHandle, x, y, w, h, uniforms, options)`.
- Reference external mod or Minecraft shaders with `ShaderHandle.resource(id)`.
- Inject embedded procedural procedural GLSL shaders using `ShaderHandle.fragmentSource(id, code)`.

### CRITICAL RULES & GOTCHAS:

- Shader programs are compiled and cached on GPU by `id()`; keep IDs consistent to prevent continuous recompilation.
- When using embedded shaders, ensure GLSL versions match backend requirements (#version 150 core in modern Minecraft).

- **Related:** `ShaderUniforms`, `ShaderDrawOptions`, `ShaderProvider`, `DrawScope`

### EXAMPLES

#### Create embedded fragment shader

```java
ShaderHandle shader = ShaderHandle.fragmentSource("mod:glow", glslCode);
scope.shader(shader, x, y, width, height, uniforms, ShaderDrawOptions.defaults());
```

