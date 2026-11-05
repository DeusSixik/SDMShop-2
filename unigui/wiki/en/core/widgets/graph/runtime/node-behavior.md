---
title: "NodeBehavior"
slug: "node-behavior"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# NodeBehavior

Functional contract describing how a node computes values or executes a flow step.

## Overview

Create pure behaviors with `NodeBehavior.pure(...)` for deterministic value calculations and exec behaviors with `NodeBehavior.exec(...)` for events and side effects. A behavior receives a `NodeContext` to read inputs, publish outputs, and select flow continuations; register it under the matching node type id in `GraphExecutor`.

