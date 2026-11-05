# `PortType`

Immutable port type: id, display name, ARGB color, marker shape and flow flag.

- **Purpose:** Drives port/wire colors and connection compatibility for Blueprint-style or Shader-Graph-style editors.

### USE WHEN

- Register data types such as float/vec3/bool with distinct colors in a PortTypeRegistry.
- Mark execution-flow types with `flow(...)` so they never connect to data ports.

### CRITICAL RULES & GOTCHAS:

- Flow and data types are mutually incompatible.
- Instances are immutable; `shape(...)` and `displayName(...)` return copies.

- **Related:** `PortTypeRegistry`, `PortShape`, `NodeGraphPort`

