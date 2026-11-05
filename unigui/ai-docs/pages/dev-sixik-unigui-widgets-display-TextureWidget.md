# `TextureWidget` (extends `WidgetBase`)

Base widget for rendering textures, UV regions, nine-slice borders, and image handles.

- **Purpose:** Displays GPU textures with controllable sampling, UV coordinate mapping, and scaling.

### USE WHEN

- Rendering item icons, custom GUI backgrounds, or sprite assets.
- Configuring nearest/linear filtering or wrap modes on images.

### CRITICAL RULES & GOTCHAS:

- texture() provides TextureHandle reference resolved by graphics backend.
- Supports nine-slice scaling and mipmapped textures.

- **Related:** `ImageView`, `TextureHandle`, `TextureFilter`, `TextureWrap`

