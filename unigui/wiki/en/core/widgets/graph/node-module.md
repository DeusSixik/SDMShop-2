---
title: "NodeModule"
slug: "node-module"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeModule

Composable vertical layout module that owns widgets and ports inside a modular node.

## Overview

Implement `NodeModule` to add a custom row or visual block to a modular node. The graph asks modules to measure and arrange their content in node-local world coordinates, and can call `paintBackground` before drawing their widgets. Register ports through the owning `NodeGraphItem` and keep the module's widget list stable until its contents change.

