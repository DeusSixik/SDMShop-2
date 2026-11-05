---
title: "NodeTypeRegistry"
slug: "node-type-registry"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeTypeRegistry

Catalog of node types used by palettes, search, and restoring modular graph items.

## Overview

Register each reusable `NodeType` once and share the registry with the graph and node palette. The registry provides lookup, registration-order enumeration, category grouping, and ranked case-insensitive search. Registering an existing id replaces its previous definition.

