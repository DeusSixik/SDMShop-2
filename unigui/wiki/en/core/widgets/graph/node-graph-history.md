---
title: "NodeGraphHistory"
slug: "node-graph-history"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphHistory

Undo and redo history for graph edits grouped into named transactions.

## Overview

`NodeGraphHistory` records reversible operations performed inside a transaction. Wrap related programmatic changes in `NodeGraph.transact(name, action)` to make them a single undo step. The graph handles transactions for common interactive edits; edits outside a transaction are intentionally not recorded.

