---
title: "NodePaintContext"
slug: "node-paint-context"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodePaintContext

Drawing services and node state supplied to a custom module background painter.

## Overview

A `NodePaintContext` is passed to `NodeModule.paintBackground`. It exposes the active draw scope, node and style, plus hover/selection state. Convert node-local coordinates and lengths with `x`, `y`, and `scale`; these account for the graph's current zoom.

