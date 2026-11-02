# `GraphModel`

Immutable widget-free snapshot of a node graph (nodes, ports, typed input values, wires) used for validation and execution.

- **Purpose:** Decouples running a graph from the editor widget so graphs can be executed and tested headless.

### USE WHEN

- Build a model with `graph.model()` and pass it to GraphExecutor or GraphValidator.
- Assemble a model by hand in tests with `GraphModel.of(nodes, wires)`.

### CRITICAL RULES & GOTCHAS:

- The model is a snapshot: later edits of the graph do not change it.
- Value arrays such as double[] are shared with the snapshot and must not be mutated.
- Flow ports are the ports whose type is a flow type of the graph's PortTypeRegistry.

- **Related:** `GraphExecutor`, `GraphValidator`, `NodeGraph`

