---
title: "GraphExecutionException"
slug: "graph-execution-exception"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# GraphExecutionException

Runtime exception raised when graph execution cannot complete safely.

## Overview

`GraphExecutionException` reports a missing node or behavior, a pure-data cycle, an exceeded step limit, or a failure thrown by node behavior. Handle it at the execution boundary and present its message to the user or log it for diagnostics.

