# `DebugOverlaySettings`

Mutable settings for the built-in performance overlay.

- **Purpose:** Controls overlay placement, UI scale, chart history length, and viewport margin through `UIContext.debugOverlaySettings()`.

### USE WHEN

- Customize the built-in debug overlay without replacing its renderer.
- Increase or reduce the retained frame history used by overlay charts.

### CRITICAL RULES & GOTCHAS:

- `scale` is clamped to 0.25..4.0 and non-finite values reset to the default.
- `sampleWindow` is clamped to 1..10,000; larger windows retain more frame samples.
- A null anchor falls back to `TOP_LEFT`, and margin cannot be negative.

- **Related:** `UIContext`, `DebugOverlayAnchor`, `UiDebugSnapshot`, `UiRenderSnapshot`

### PLACEMENT

- `anchor(DebugOverlayAnchor)` → DebugOverlaySettings — Sets the viewport corner used to anchor the overlay; null selects `TOP_LEFT`.
- `margin(float)` → DebugOverlaySettings — Sets non-negative distance from the selected viewport corner in UI pixels.

### APPEARANCE

- `scale(float)` → DebugOverlaySettings — Sets overlay scale, clamped to the inclusive range 0.25..4.0.

### SAMPLING

- `sampleWindow(int)` → DebugOverlaySettings — Sets retained chart history in frames, clamped to 1..10,000.

### EXAMPLES

#### Move and resize the overlay

```java
context.debugOverlaySettings()
        .anchor(DebugOverlayAnchor.TOP_RIGHT)
        .scale(1.0f)
        .sampleWindow(240)
        .margin(8.0f);
```

