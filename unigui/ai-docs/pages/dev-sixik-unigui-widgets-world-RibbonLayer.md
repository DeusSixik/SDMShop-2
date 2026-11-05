# `RibbonLayer` (implements `WorldLayer`)

World canvas layer rendering textured or solid ribbon lines, paths, and waypoint routes in world coordinates.

### API

- `addPoint(float, float)` → RibbonLayer — Appends a new point in world coordinates to the end of the ribbon path.
- `cacheHits()` → long — Returns the count of successful mesh cache hits.
- `cacheRebuilds()` → long — Returns the count of mesh cache rebuilds.
- `clear()` → RibbonLayer — Clears all points from the ribbon path.
- `closed()` → boolean — Returns true if the ribbon path forms a closed loop.
- `closed(boolean)` → RibbonLayer — Configures whether the ribbon path connects its end back to its start.
- `flipU()` → boolean — Returns true if the horizontal U texture coordinate is inverted.
- `flipU(boolean)` → RibbonLayer — Configures horizontal inversion of the texture U coordinate.
- `flipV()` → boolean — Returns true if the vertical V texture coordinate is inverted.
- `flipV(boolean)` → RibbonLayer — Configures vertical inversion of the texture V coordinate.
- `invalidateCache()` → void — Forces invalidation of the cached mesh geometry.
- `jointDots()` → boolean — Returns true if rounded joint filler dots are rendered at path vertices.
- `jointDots(boolean)` → RibbonLayer — Configures rendering of rounded joint filler dots at path vertices.
- `jointSegments()` → int — Returns the segment count used for rounded joint circles.
- `jointSegments(int)` → RibbonLayer — Sets the segment count used for rounded joint circles.
- `miterLimit()` → float — Returns the miter join bevel threshold.
- `miterLimit(float)` → RibbonLayer — Sets the miter join bevel threshold.
- `options()` → RibbonOptions — Returns the active ribbon generation options.
- `options(RibbonOptions)` → RibbonLayer — Sets the ribbon generation options.
- `path()` → List<ViewportPoint> — Returns an unmodifiable view of all world points along the ribbon path.
- `path(List<ViewportPoint>)` → RibbonLayer — Replaces the entire ribbon path with the given list of world points.
- `render(WorldCanvas, DrawScope)` → void — Renders the ribbon mesh into the canvas draw scope.
- `resetCacheStats()` → void — Resets mesh caching diagnostic statistics.
- `swapUv()` → boolean — Returns true if U and V texture axes are swapped.
- `swapUv(boolean)` → RibbonLayer — Configures swapping of U and V texture axes.
- `texture()` → TextureHandle — Returns the texture handle applied to this ribbon, or null for solid color fill.
- `texture(TextureHandle)` → RibbonLayer — Sets the texture applied to this ribbon.
- `textureLength()` → float — Returns the world-space distance corresponding to one full texture tile repeat.
- `textureLength(float)` → RibbonLayer — Sets the world-space distance for one full texture tile repeat.
- `tint()` → MutableColor — Returns the mutable vertex tint color of the ribbon.
- `tint(ColorView)` → RibbonLayer — Sets the vertex tint color of the ribbon.
- `width()` → float — Returns the ribbon width in world units.
- `width(float)` → RibbonLayer — Sets the ribbon width in world units.

