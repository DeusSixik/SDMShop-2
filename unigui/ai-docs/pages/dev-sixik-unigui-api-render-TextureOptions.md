# `TextureOptions`

Immutable texture sampling configuration defining min/mag filtering, UV wrapping, mipmaps, and premultiplied alpha.

- **Purpose:** Governs GPU texture sampler state, ensuring reproducible visual quality across pixel-art and smooth scaling without owning GPU handles.

### USE WHEN

- Define custom texture sampling for `TextureHandle` via `TextureOptions`.
- Enable UV tiling patterns using `wrap(TextureWrap.REPEAT)`.
- Enable smooth scaling and antialiasing using `sampling(TextureFilter.LINEAR)` and `mipmaps(true)`.
- Eliminate dark edge fringing using `premultipliedAlpha(true)`.

### CRITICAL RULES & GOTCHAS:

- TextureOptions is strictly immutable; modifier methods return new instances or `this` if values match.
- `packed()` provides a 10-bit integer key suitable for O(1) state caching in render backends without heap allocations.
- UniGUI defaults use `NEAREST` filtering and `CLAMP_TO_EDGE` wrapping to preserve crisp Minecraft pixel-art aesthetics.

- **Related:** `TextureHandle`, `TextureFilter`, `TextureWrap`, `DrawScope`

### EXAMPLES

#### Create repeating tiled texture options

```java
TextureOptions options = TextureOptions.defaults()
        .wrap(TextureWrap.REPEAT);
```

#### Create smooth linear options with mipmaps

```java
TextureOptions options = TextureOptions.linear()
        .mipmaps(true)
        .premultipliedAlpha(true);
```

