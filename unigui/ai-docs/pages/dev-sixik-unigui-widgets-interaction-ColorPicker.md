# `ColorPicker` (extends `LinearBox`)

Interactive color selection control providing RGBA sliders, hex input, and popup palette.

- **Purpose:** Allows users to inspect and adjust RGB or RGBA color channels visually in an interactive popup.

### USE WHEN

- Custom theme customization and color configuration screens.
- Particle or render effect tint pickers.

### CRITICAL RULES & GOTCHAS:

- Supports packed 32-bit ARGB values and floating-point ColorView instances.
- Emits ColorChangedEvent when channels or hex strings are edited.

- **Related:** `ColorView`, `MutableColor`, `Slider`, `Popup`

