# `PortRowModule` (implements `NodeModule`)

Node row with an optional input pin on the left, an optional output pin on the right, labels and an inline editor shown while the input is unconnected.

- **Purpose:** Provides the standard pin row used by both execution-flow and data-flow node editors.

### USE WHEN

- Declare a data input with a default-value editor via `input(...).inline(widget)`.
- Declare an input/output pair on one row via `row(...)`.
- Declare an exec row via `flow(inId, outId)`.

### CRITICAL RULES & GOTCHAS:

- Data inputs accept one connection; data outputs and flow inputs are unlimited; flow outputs accept one.
- The inline editor is hidden while the input has a connection.
- Ports are created when the module is attached to a node.

- **Related:** `NodeModule`, `NodeBuilder`, `PortType`, `NodeGraphPort`

