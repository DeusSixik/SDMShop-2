# `TextureLayer`

Mutable single-layer texture descriptor bundling texture handle, UV bounds, tint color, ImageFit, and slice borders.

- **Purpose:** Encapsulates complete rendering parameters for a single layer in multi-textured widget surfaces.

### USE WHEN

- Configure visual layers for composite widget backgrounds in `TextureGroup` or `Surface`.
- Adjust tint color, insets, and nine-slice borders on an individual surface layer.

### CRITICAL RULES & GOTCHAS:

- Null texture represents an empty/invisible layer skipped during rendering.
- `fit` determines how the layer conforms to the widget layout bounds.

- **Related:** `TextureGroup`, `TextureHandle`, `ImageFit`, `SliceBorders`, `ColorView`

