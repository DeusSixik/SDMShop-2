# `NodeContext`

Execution environment handed to a NodeBehavior: reads inputs, writes outputs and steers the exec flow.

- **Purpose:** Gives node implementations a small, UI-free API for Blueprint-style and data-flow execution.

### USE WHEN

- Read a wired or default input value with `input`, `number`, `text`, `bool` or `vector`.
- Publish results with `output` and continue the flow with `continueWith` or `runFlow`.

### CRITICAL RULES & GOTCHAS:

- `input` returns the upstream output when the input is wired, otherwise the editor default, otherwise null.
- `continueWith` queues a continuation that runs after the behavior returns, in call order.
- `runFlow` runs the chain behind a flow output immediately, which is how loops and branches are written.
- Pure nodes are re-evaluated for every consuming exec step, so they must not keep state.

- **Related:** `NodeBehavior`, `GraphExecutor`, `GraphModel`

