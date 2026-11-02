# `GraphValidator`

Static graph checks: missing required inputs, data cycles among pure nodes and unreachable exec nodes.

- **Purpose:** Finds graphs that cannot run correctly before running them, so the editor can mark broken nodes.

### USE WHEN

- Call `GraphExecutor.validate(model)` to include missing-behavior checks.
- Call `NodeGraph.validate(executor)` to mark nodes with a red or amber border.

### CRITICAL RULES & GOTCHAS:

- Only edges leaving a pure node count as data dependencies; exec outputs are cached state.
- An entry is a node with a flow output and no flow input.
- A required input is satisfied by a wire or by an editor default.

- **Related:** `GraphExecutor`, `GraphModel`, `GraphIssue`

