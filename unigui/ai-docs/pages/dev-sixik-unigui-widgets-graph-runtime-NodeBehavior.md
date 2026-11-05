# `NodeBehavior`

Runtime behavior of a node type: either a pure computation or an exec-flow step with side effects.

- **Purpose:** Lets users implement what each node does without touching the editor widgets.

### USE WHEN

- Register a behavior per node type with `GraphExecutor.register(typeId, behavior)`.
- Use `NodeBehavior.pure(...)` for math/logic nodes and `NodeBehavior.exec(...)` for events and actions.

### CRITICAL RULES & GOTCHAS:

- A pure behavior is evaluated on demand and must be deterministic for the same inputs.
- An exec behavior is run once per arrival of the flow; its outputs stay readable for later nodes.
- Throwing from a behavior aborts the run with GraphExecutionException naming the node.

- **Related:** `NodeContext`, `GraphExecutor`

