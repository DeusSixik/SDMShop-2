# `RibbonMesh`

Procedural textured ribbon mesh generator along polylines for rendering roads, cables, traces, pipelines, and graph edges.

- **Purpose:** Expands a sequence of 2D centerline points into a triangulated strip with miter joins, butt caps, circular joint dots, UV mapping, and viewport clipping.

### USE WHEN

- Draw continuous textured connections like power cables, pipes, or wiring between nodes.
- Render procedural roads, circuit loops, or curved graph traces with seamless texture tiling.
- Generate custom triangulated meshes with miter corner clamping and joint smoothing.

### CRITICAL RULES & GOTCHAS:

- Degenerate segments (zero length or NaN) are safely skipped. Paths with fewer than 2 valid points return an empty mesh.
- Set textureLength equal to the desired world-space length of one texture tile to prevent stretching when using REPEAT wrapping.
- Use swapUv(true) for vertical stripe textures (where gradient/pattern runs vertically) to rotate the texture 90 degrees along the path.
- For sharp turns or loops where miter joins clamp severely, enable jointDots(true) to cover seam gaps with circular disks.

- **Related:** `RibbonOptions`, `DrawMesh`, `DrawPoint`, `DrawVertex`, `DrawScope`, `TextureHandle`

### EXAMPLES

#### Building a Textured Power Cable

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

#### Closed Circuit Loop with Joint Disks

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

