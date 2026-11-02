# `ShaderResourcePaths`

Resource path normalizer and candidate generator for resolving shader assets across classpath and mod namespaces.

- **Purpose:** Generates standard asset candidate paths (e.g. .fsh, .vsh, .json) for namespaced and vanilla shader identifiers.

### USE WHEN

- Generate candidate paths when implementing custom ShaderProvider loaders.
- Parse namespaced shader identifiers via `ShaderResourcePaths.parse(id)`.

### CRITICAL RULES & GOTCHAS:

- Preserves order and eliminates duplicates when building candidate lookup lists.

- **Related:** `ClasspathShaderProvider`, `ShaderProvider`, `ShaderHandle`

