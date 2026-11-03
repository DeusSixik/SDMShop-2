---
title: "NodeType"
slug: "node-type"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeType

Reusable node-kind definition with identity, palette metadata, port sketches, and a node factory.

## Overview

A `NodeType` describes what a node is, independently of where it is placed. Register types in `NodeTypeRegistry` to populate the add-node palette and allow snapshot restore. Its factory must return a fresh `NodeGraphItem` for each requested id; the type id is applied to the created item.

