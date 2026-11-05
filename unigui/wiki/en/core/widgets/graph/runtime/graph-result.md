---
title: "GraphResult"
slug: "graph-result"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# GraphResult

Result of a graph execution, including executed step count and the latest outputs of exec nodes.

## Overview

`GraphResult` is returned by `GraphExecutor.fire`. Use `steps()` to inspect execution work and `output(nodeId, portId)` to read the latest value published by an executed node. Pure-node values are demand-computed and are not included in the exec output map.

