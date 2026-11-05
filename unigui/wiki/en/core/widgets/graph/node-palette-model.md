---
title: "NodePaletteModel"
slug: "node-palette-model"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodePaletteModel

[Widget](<../../../widgets/widget.md>)-free state model for node category browsing, ranked search, selection, and scrolling.

## Overview

Use `NodePaletteModel` when building a custom node picker or testing palette behavior without rendering widgets. An empty query produces a collapsible category tree; a query produces a filtered, ranked flat list. Selection movement wraps and the visible window follows the selected entry.

