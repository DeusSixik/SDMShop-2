---
title: "GraphModel"
slug: "graph-model"
category: "Core/Widgets/Graph/Runtime"
locale: "en"
order: 0
---

# GraphModel

Immutable, widget-free snapshot of nodes, ports, input values, and wires for validation or execution.

## Overview

Obtain a model with `NodeGraph.model()` to separate execution from the editor UI. The resulting snapshot can be validated, executed, or tested without rendering widgets. Later edits to the source graph do not update the model, so create a new snapshot when the graph changes.

