---
title: "NodeGraphClipboard"
slug: "node-graph-clipboard"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphClipboard

Immutable buffer of selected registered node types and their internal wires for copy and paste.

## Overview

`NodeGraphClipboard` is the data transferred by graph copy and paste operations. Only modular nodes with a registered `NodeType` are included, and only wires whose two endpoints are copied. Input editor values and supported node state are preserved; custom content widgets are recreated by the destination type factory.

