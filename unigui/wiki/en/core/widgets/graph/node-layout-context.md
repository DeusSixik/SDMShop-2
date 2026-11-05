---
title: "NodeLayoutContext"
slug: "node-layout-context"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeLayoutContext

Layout services supplied to a node module for measuring widgets, placing them, and anchoring ports.

## Overview

A `NodeLayoutContext` is passed to `NodeModule` measurement and arrangement callbacks. Its coordinates are world units relative to the node origin, independent of viewport zoom. Use `preferredWidth` and `preferredHeight` to measure owned widgets, `place` to arrange them, and `anchor` to position ports.

