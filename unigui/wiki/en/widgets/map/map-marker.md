---
title: "MapMarker"
slug: "map-marker"
category: "Widgets/Map"
locale: "en"
order: 0
---

# MapMarker

Neutral base marker shell for [MapCanvas](<map-canvas.md>) supporting custom content, text labels, selection, and highlighting.

## Overview

`MapMarker` is a neutral base container widget for displaying map markers on a `MapCanvas`.

Key capabilities:
- Hosts arbitrary nested UI widgets or simple text labels (`content`, `label`).
- Built-in visual states for selection (`selected`) and target highlighting (`highlighted`).
- Unsurfaced transparent style by default for clean icon and label presentation.
- Governed by an underlying `AnchorWidget` for coordinate positioning.

## Creating and Customizing Map Marker

```java
MapMarker marker = new MapMarker("Waypoint");
marker.selected(true);
```

## Related links

- [AnchorWidget](<../../core/widgets/world/anchor-widget.md>): Spatial anchor pinning a screen-space UI widget to 3D/2D world coordinates with zoom scaling, pivot, and viewport culling.
- [MapCanvas](<map-canvas.md>): Interactive 2D map widget extending [WorldCanvas](<../world/world-canvas.md>) with map sizing, texture and grid layers, coordinate projection, and marker handles.
- [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>): Handle for fluent configuration of marker positioning, screen sizing, zoom scaling, pivot, and visibility on [MapCanvas](<map-canvas.md>).

## API

<div class="api-member-card" markdown="1">

### `MapMarker()` {: #mapmarker data-toc-label="MapMarker()" }

Constructs an empty marker shell without default surface styling.

</div>

<div class="api-member-card" markdown="1">

### `MapMarker(String)` {: #mapmarkerstring data-toc-label="MapMarker(String)" }

Constructs a marker shell displaying a plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Plain text string. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Returns the low-level [AnchorWidget](<../../core/widgets/world/anchor-widget.md>) managing this marker positioning.

Returns `AnchorWidget`: The managing [AnchorWidget](<../../core/widgets/world/anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`content()` {: #content data-toc-label="content()" }

Returns the current content widget hosted inside this marker.

Returns `Widget`: The content widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Replaces the content widget hosted inside this marker.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | Hosted content widget. |

Returns `MapMarker`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`highlighted()` {: #highlighted data-toc-label="highlighted()" }

Returns true if this marker is currently highlighted.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`highlighted(boolean)` {: #highlightedboolean data-toc-label="highlighted(boolean)" }

Sets the marker highlighted visual state.

| Parameter | Type | Description |
| --- | --- | --- |
| `highlighted` | `boolean` | Highlighted visual state flag. |

Returns `MapMarker`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`label()` {: #label data-toc-label="label()" }

Returns the hosted [Label](<../display/label.md>) widget if content is a label, or null otherwise.

Returns `Label`: The [Label](<../display/label.md>) widget instance, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`label(String)` {: #labelstring data-toc-label="label(String)" }

Replaces marker content with a plain text [Label](<../display/label.md>) widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Plain text string. |

Returns `MapMarker`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`selected()` {: #selected data-toc-label="selected()" }

Returns true if this marker is currently in selected state.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`selected(boolean)` {: #selectedboolean data-toc-label="selected(boolean)" }

Sets the marker selected visual state.

| Parameter | Type | Description |
| --- | --- | --- |
| `selected` | `boolean` | Selected visual state flag. |

Returns `MapMarker`: This instance for chaining..

</div>

