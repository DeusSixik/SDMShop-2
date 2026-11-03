---
title: "NodeStyle"
slug: "node-style"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeStyle

Per-node colors and geometry settings for modular graph nodes.

## Overview

`NodeStyle` controls header, body, border, title, and text colors along with header, row, padding, width, and corner geometry. Lengths use graph world units and scale with zoom. Styles are mutable and belong to one node; call `copy()` before adapting a shared style.

