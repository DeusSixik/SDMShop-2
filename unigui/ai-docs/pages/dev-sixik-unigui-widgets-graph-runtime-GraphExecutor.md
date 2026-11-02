# `GraphExecutor`

Runs a GraphModel: follows exec wires from an event node and pulls data inputs on demand from pure nodes.

- **Purpose:** Turns a node graph into a program for Blueprint-style scripting and data-flow evaluation.

### USE WHEN

- Register a NodeBehavior per node type, then `fire(graph.model(), startNodeId)`.
- Protect against runaway loops with `maxSteps(...)`.

### CRITICAL RULES & GOTCHAS:

- Every node type that can run needs a registered behavior; reroutes are built in.
- A data cycle among pure nodes throws GraphExecutionException instead of overflowing the stack.
- The step limit counts exec node runs and throws when exceeded, which stops infinite loops.
- The executor holds no state between `fire` calls; results are returned in GraphResult.

- **Related:** `NodeBehavior`, `NodeContext`, `GraphModel`, `GraphValidator`

### EXAMPLES

#### Run a start event that creates and greets an NPC

```java
GraphExecutor executor = new GraphExecutor()
        .register("event.start", NodeBehavior.exec(c -> c.continueWith("then")))
        .register("npc.say", NodeBehavior.exec(c -> {
            log.add(c.text("who") + ": " + c.text("text"));
            c.continueWith("then");
        }));
GraphResult result = executor.fire(graph.model(), "start");
```

