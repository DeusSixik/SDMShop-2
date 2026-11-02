# `SurfaceWidget` (extends `PanelWidget`)

Container with a composable Surface for background color, textures, shaders, borders, and corner radius.

- **Purpose:** Provides full visual chrome (background, border, radius, overlay textures) around child widgets with built-in transition support and Theme/StylePack resolution.

### USE WHEN

- Create cards, panels, buttons, and custom controls that require backgrounds, borders, or textures.
- Animate visual properties such as background color, border width, or corner radius.
- Render procedural backgrounds using custom GLSL shaders via backgroundShader().

### CRITICAL RULES & GOTCHAS:

- Setting background() or border() automatically enables backgroundVisible(true) / borderVisible(true).
- Order of rendering: custom WidgetRender (if set), then surface chrome, then content and children.
- Custom visual renderers override the default surface chrome entirely.

- **Related:** `Box`, `PanelWidget`, `Surface`, `BackgroundKind`

### APPEARANCE

- `backgroundVisible(boolean)` → T (default: `false`) — Controls whether the surface background is rendered.
- `backgroundVisible()` → boolean — Checks if background rendering is active.
- `background(ColorView)` → T (default: `#00000000`) — Sets background fill color and enables background visibility.
- `borderColor(ColorView)` → T (default: `#FFFFFFFF`) — Sets border color and enables border visibility.
- `border(ColorView)` → T (default: `#FFFFFFFF`) — Sets border stroke color and enables border visibility.
- `borderVisible(boolean)` → T (default: `false`) — Controls whether the border stroke is rendered.
- `borderWidth(float)` → T (default: `1`) — Sets stroke thickness of the border.
- `radius(float)` → T (default: `0`) — Sets corner rounding radius in UI pixels.
- `boxVisualEnabled(boolean)` → T (default: `true`) — Enables or disables all surface visual rendering.
- `themeEnabled(boolean)` → T (default: `true`) — Controls whether global Theme styles override surface properties.
- `surface()` → Surface — Returns the underlying composable Surface model.

### EXAMPLES

#### Create styled surface

```java
SurfaceWidget<?> surface = new Box()
        .background(ColorView.ofHex("#202225"))
        .radius(6.0f);
```

