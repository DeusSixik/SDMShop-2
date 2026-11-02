# `ShaderProvider`

SPI interface resolving shader source code (vertex/fragment GLSL) for named shader handles.

- **Purpose:** Decouples shader resource loading from graphics drivers, allowing mods or plugins to supply custom GLSL sources.

### USE WHEN

- Implement a custom shader loader to supply shaders from JAR resources, assets folders, or remote endpoints.
- Register via Java ServiceLoader (`META-INF/services`) or `ShaderProviders#register(ShaderProvider)`.

### CRITICAL RULES & GOTCHAS:

- Prefer overriding `loadOrNull(ShaderHandle)` in high-frequency rendering paths to avoid `Optional` allocation.
- Return null or Optional.empty() if this provider does not recognize the requested shader ID.

- **Related:** `ShaderProviders`, `ShaderHandle`, `ShaderSource`

