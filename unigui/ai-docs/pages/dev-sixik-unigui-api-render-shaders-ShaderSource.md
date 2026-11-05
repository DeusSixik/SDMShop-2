# `ShaderSource`

Resolved GLSL source code container for a ShaderHandle, bundling optional vertex and mandatory fragment shaders.

- **Purpose:** Supplies normalized GLSL source text and unique integer IDs to backend shader compilers.

### USE WHEN

- Return from `ShaderProvider#load(ShaderHandle)` with loaded GLSL source text.
- Construct via `ShaderSource.fragment(id, fragCode)` or `ShaderSource.source(id, vertCode, fragCode)`.

### CRITICAL RULES & GOTCHAS:

- Fragment shader source must not be blank.
- If vertex shader source is null, backends supply their standard 2D UI vertex shader.

- **Related:** `ShaderHandle`, `ShaderProvider`, `ShaderProviders`

