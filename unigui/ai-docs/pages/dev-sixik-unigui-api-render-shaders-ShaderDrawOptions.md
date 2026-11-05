# `ShaderDrawOptions`

Backend-facing configuration for shader draw commands governing blending, built-in uniforms, and vertex offsets.

- **Purpose:** Decouples shader hardware state parameters from draw commands, enabling zero-allocation pooling.

### USE WHEN

- Pass into `DrawScope#shader` to configure blending and vertex offset.
- Toggle `builtinUniforms` to control automatic injection of projection matrices and SquareVertex.

### CRITICAL RULES & GOTCHAS:

- `squareVertexOffset` defaults to -0.25f to preserve compatibility with existing quad shaders.
- Options are snapshotted or pooled during command replay.

- **Related:** `ShaderHandle`, `ShaderUniforms`, `DrawScope`

### EXAMPLES

#### Configure blending and built-in uniforms

```java
ShaderDrawOptions options = ShaderDrawOptions.defaults()
        .blend(true)
        .builtinUniforms(true);
```

