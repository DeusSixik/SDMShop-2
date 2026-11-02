# `RerouteModule` (implements `NodeModule`)

Compact pass-through node used to bend and fan out wires; has an input on the left and an output on the right.

- **Purpose:** Lets users route wires around nodes and split one output without crossing node bodies.

### USE WHEN

- Insert a reroute into a wire with `NodeGraph.insertReroute`.
- Create one directly with `RerouteModule.createNode`.

### CRITICAL RULES & GOTCHAS:

- A data reroute accepts any data type, an exec reroute only flow; the two never mix.
- Reroute input accepts one wire, data output is unlimited, exec output accepts one.

- **Related:** `NodeModule`, `NodeGraph`, `NodeType`

