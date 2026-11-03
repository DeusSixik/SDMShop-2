---
title: "NodePalette"
slug: "node-palette"
category: "Widgets/Graph"
locale: "en"
order: 0
---

# NodePalette

Searchable overlay for browsing registered node types and creating nodes from a graph editor.

## Overview

`NodePalette` presents registered node types as a category tree or a ranked search list. It is hosted by an `OverlayLayer`, opens at screen coordinates, and reports a picked type through `onPick`. Keyboard navigation supports category expansion, selection, activation, and Escape dismissal.

