# `PortTypeRegistry`

Registry of PortTypes plus directed conversion rules that decide whether two ports may connect.

- **Purpose:** Centralizes type colors, shapes and compatibility so every node module shares one definition.

### USE WHEN

- Define the type system of a graph editor: register types, then allow implicit conversions.
- Resolve a port's string type to a PortType for rendering.

### CRITICAL RULES & GOTCHAS:

- Unknown or blank type ids resolve to the neutral `any` type.
- Conversions are directed: allowing `int -> float` does not allow `float -> int`.
- Flow types only connect to flow types.

- **Related:** `PortType`, `NodeGraphConnectionPolicy`

### EXAMPLES

#### Shader-graph style type system

```java
PortTypeRegistry types = new PortTypeRegistry()
        .register(PortType.data("float", 0xFF98E27A))
        .register(PortType.data("vec3", 0xFFF5D547))
        .allowConversion("float", "vec3");
```

