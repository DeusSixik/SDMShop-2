# `ProgressBar` (extends `Box`)

Progress bar widget visualizing task progress across a numeric range with support for indeterminate mode and animations.

### API

- `ProgressBar()` — Constructs a ProgressBar with default [0, 100] range and styling.
- `indeterminate()` → boolean — Returns true if indeterminate animation mode is enabled.
- `indeterminate(boolean)` → ProgressBar (default: `false`) — Enables or disables indeterminate continuous activity animation.
- `max()` → float — Returns the maximum progress value.
- `max(float)` → ProgressBar (default: `1`) — Sets the maximum progress value.
- `min()` → float — Returns the minimum progress value.
- `min(float)` → ProgressBar (default: `0`) — Sets the minimum progress value.
- `progress()` → float — Returns the normalized progress ratio between 0.0 and 1.0.
- `range(float, float)` → ProgressBar — Sets both minimum and maximum bounds simultaneously.
- `value()` → float — Returns the current numeric progress value.
- `value(float)` → ProgressBar (default: `0`) — Sets the current numeric progress value, clamped to [min, max].

