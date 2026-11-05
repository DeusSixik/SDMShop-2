---
title: "RibbonMesh"
slug: "ribbon-mesh"
category: "Core/Render"
locale: "en"
order: 40
---

# RibbonMesh

Procedural textured ribbon mesh generator along polylines for rendering roads, cables, traces, pipelines, and graph edges.

## Overview

`RibbonMesh` expands a 2D polyline (list of center points) into a triangulated strip with defined physical width. Each point produces a vertex pair left and right of the centerline. Adjacent pairs form two triangles with butt cap endpoints and clamped miter joins.

## Geometry & Miter Joins

Internal joints calculate an averaged normal between neighboring segments (miter). To prevent sharp bends or 180° turnarounds from producing infinitely projecting spikes, the miter length is clamped by `miterLimit` (default 4.0 half-widths).

## UV Mapping & Orientation

Coordinates are mapped such that `u` spans across the ribbon width (`0..1`) and `v` advances along the path as `distance / textureLength`. When texture wrapping is set to `REPEAT`, the texture tiles seamlessly without stretching. For vertical stripe textures (like cable or wire patterns), `swapUv(true)` rotates the texture 90° so `u` runs along the path and `v` across width.

## Joint Dots & Corners

At very sharp corners where miter joins degenerate, enabling `jointDots(true)` places circular vertex disks (radii matching half the ribbon width) over joints, completely eliminating visual seams and gaps.

## Culling & Clipping

The ribbon builder supports view rect clipping (`clip(x, y, w, h)`). Segments outside the viewport (expanded by the miter margin) are skipped without breaking the accumulated distance parameter `v`, ensuring textures stay aligned during scrolling.

## Builder API

Construct ribbons via the fluent `RibbonMesh.builder(points)` for detailed customization (width, texture length, miter limit, closed loops, clipping, UV flipping), or use the one-shot convenience overload `RibbonMesh.ribbon(...)` for standard strips.

## Building a Textured Power Cable

```java
List<DrawPoint> path = List.of(
    new DrawPoint(20.0f, 40.0f),
    new DrawPoint(80.0f, 90.0f),
    new DrawPoint(160.0f, 60.0f)
);
DrawMesh cable = RibbonMesh.builder(path)
    .width(8.0f)
    .textureLength(32.0f)
    .swapUv(true)
    .build(ColorView.WHITE);
scope.addMesh(cable, cableTexture);
```

## Closed Circuit Loop with Joint Disks

```java
List<DrawPoint> corners = List.of(
    new DrawPoint(50.0f, 50.0f),
    new DrawPoint(250.0f, 50.0f),
    new DrawPoint(200.0f, 180.0f),
    new DrawPoint(80.0f, 150.0f)
);
DrawMesh track = RibbonMesh.builder(corners)
    .width(16.0f)
    .textureLength(48.0f)
    .closed(true)
    .jointDots(true)
    .jointSegments(16)
    .build(ColorView.rgb(200, 200, 200));
scope.addMesh(track, roadTexture);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonMesh::</span>`builder(List)` {: #builderlist data-toc-label="builder(List)" }

**builder(List)**

Creates a new fluent ribbon mesh builder for the specified path.

| Parameter | Type | Description |
| --- | --- | --- |
| `points` | `List` | Central polyline coordinates in destination space; null points are ignored. |

Returns `Builder`: A new Builder instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonMesh::</span>`ribbon(List, float, float, ColorView)` {: #ribbonlist-float-float-colorview data-toc-label="ribbon(List, float, float, ColorView)" }

**ribbon(List, float, float, ColorView)**

Constructs a textured ribbon mesh in a single operation using default join and UV settings.

| Parameter | Type | Description |
| --- | --- | --- |
| `points` | `List` | Central polyline path. |
| `width` | `float` | Ribbon width in destination pixels. |
| `textureLength` | `float` | Length of a single texture tile along the path. |
| `tint` | `ColorView` | Vertex tint color, or null for opaque white. |

Returns `DrawMesh`: Generated DrawMesh, or empty mesh if path contains fewer than 2 valid points..

</div>

