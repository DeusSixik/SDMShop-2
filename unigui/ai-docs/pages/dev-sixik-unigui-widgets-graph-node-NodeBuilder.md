# `NodeBuilder`

Fluent builder that assembles a modular NodeGraphItem from header, pin rows, widgets and custom modules.

- **Purpose:** Makes declaring Blueprint-like or Shader-Graph-like node types a few readable lines.

### USE WHEN

- Declare a node type factory for NodeTypeRegistry.
- Build one-off nodes in code or tests.

### CRITICAL RULES & GOTCHAS:

- Call order equals top-to-bottom layout order.
- Port ids must be unique within the node.
- `build()` may be called once; the builder owns the node until then.

- **Related:** `NodeGraphItem`, `NodeModule`, `PortRowModule`, `HeaderModule`, `NodeType`

### EXAMPLES

#### Blueprint-style function node

```java
NodeGraphItem node = NodeBuilder.node("spawn")
        .color(0xFF4CAF50)
        .header("Create NPC")
        .flowRow("in", "out")
        .input("pos", "Position", "vec3", positionEditor)
        .input("name", "Name", "string", nameField)
        .output("result", "Result", "entity")
        .build();
```

