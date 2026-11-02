# `ShaderProviders`

Global registry and resolution dispatcher for ShaderProvider implementations.

- **Purpose:** Coordinates shader lookup across embedded sources, registered mod providers, ServiceLoader discoveries, and classpath assets.

### USE WHEN

- Register custom shader providers at runtime with `ShaderProviders.register(provider)`.
- Resolve shader source for a ShaderHandle via `ShaderProviders.resolve(handle)`.

### CRITICAL RULES & GOTCHAS:

- Resolution order: 1) Embedded source in ShaderHandle, 2) Preferred providers, 3) Manually registered providers, 4) ServiceLoader, 5) Classpath fallback.
- Exceptions thrown by individual providers are suppressed to prevent crashing the entire UI.

- **Related:** `ShaderProvider`, `ShaderHandle`, `ShaderSource`

