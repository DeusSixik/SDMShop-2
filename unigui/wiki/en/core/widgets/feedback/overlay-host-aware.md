---
title: "OverlayHostAware"
slug: "overlay-host-aware"
category: "Core/Widgets/Feedback"
locale: "en"
order: 0
---

# OverlayHostAware

Interface for widgets that dynamically calculate their position and layout within an overlay host bounds.

## Overview

`OverlayHostAware` is a functional contract implemented by floating widgets (such as tooltips, context menus, popups, and windows) to dynamically arrange and constrain their layout inside an overlay host's boundaries.

Primary method:
- `arrangeInHost(RectView hostBounds)`: Recalculates position, offsets, and dimensions to prevent clipping off-screen.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">OverlayHostAware::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Positions and arranges this floating widget within the given host boundaries.

| Parameter | Type | Description |
| --- | --- | --- |
| `hostBounds` | `RectView` | Available host rectangular boundary. |

</div>

