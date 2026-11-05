---
title: "NodeGraphRegion"
slug: "node-graph-region"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphRegion

A movable, resizable comment frame that groups nodes and regions on a graph canvas.

## Overview

Create a region with `NodeGraph.addRegion` to label and visually group part of the canvas. Its geometry follows the graph viewport; nodes fully inside it move with the region when its header is dragged. The body lets pointer input pass through, while the header and resize corner remain interactive.

