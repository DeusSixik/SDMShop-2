# `Shape` (extends `WidgetBase`)

Vector geometric shape widget supporting rectangles, rounded rectangles, circles, ovals, and strokes.

- **Purpose:** Renders hardware-accelerated 2D geometric primitives with antialiasing and customizable strokes.

### USE WHEN

- Drawing notification badges, circular avatars, or ornamental frames.
- Custom geometric accents without importing external PNG texture files.

### CRITICAL RULES & GOTCHAS:

- type selects geometric primitive (RECTANGLE, ROUNDED_RECT, CIRCLE, OVAL).
- stroke toggles between outline drawing and filled geometry.

- **Related:** `Path`, `MutableColor`, `WidgetBase`

