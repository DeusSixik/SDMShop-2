---
title: "GraphExecutor"
slug: "graph-executor"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# GraphExecutor

Runs a widget-free graph model by following execution-flow wires and evaluating data inputs on demand.

## Overview

Register a `NodeBehavior` for each executable node type, then call `fire(model, startNodeId)` on an event node. Pure behaviors compute outputs when requested; exec behaviors run along flow wires and may continue through one or more outputs. Validate first when the graph can be edited by users. A configurable step limit stops runaway flow loops.

## Register behaviors and run a graph

```java
GraphExecutor executor = new GraphExecutor()
        .register("event.start", NodeBehavior.exec(context -> context.continueWith("next")))
        .register("value.add", NodeBehavior.pure(context ->
                context.output("sum", context.number("left") + context.number("right"))));
GraphResult result = executor.fire(graph.model(), "start");
```

