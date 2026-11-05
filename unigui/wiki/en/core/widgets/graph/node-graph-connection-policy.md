---
title: "NodeGraphConnectionPolicy"
slug: "node-graph-connection-policy"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphConnectionPolicy

Strategy interface defining rules and compatibility validation for forming connections between ports.

## Overview

Strategy interface defining rules and compatibility validation for forming connections between ports. Provides extensive layout configuration and interaction capabilities.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnectionPolicy::</span>`validate(NodeGraph, NodeGraphPortRef, NodeGraphPortRef)` {: #validatenodegraph-nodegraphportref-nodegraphportref data-toc-label="validate(NodeGraph, NodeGraphPortRef, NodeGraphPortRef)" }

Validates whether a connection can be established between source and target ports.

| Parameter | Type | Description |
| --- | --- | --- |
| `graph` | `NodeGraph` | The graph parameter. |
| `from` | `NodeGraphPortRef` | The source port reference. |
| `to` | `NodeGraphPortRef` | The destination port reference. |

Returns `NodeGraphConnectionValidation`: The connection validation result..

</div>

