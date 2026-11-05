# `Spinner` (extends `LoadingIndicator`)

Animated circular or segmented loading spinner widget indicating ongoing background activity.

### API

- `Spinner()` — Constructs a default Spinner initialized to Mode.SPINNER.
- `Spinner(Style)` — Constructs a Spinner with the specified visual style preset.
- `activeDots(int)` → Spinner (default: `4`) — Sets the number of highlighted active dots in dotted styles.
- `angle(float)` → Spinner (default: `4.555`) — Sets the arc sweep angle in radians for arc-based spinner styles.
- `arcs(int)` → Spinner (default: `3`) — Sets the arc count for multi-arc spinner styles.
- `dots(int)` → Spinner (default: `8`) — Sets the dot count for dotted spinner styles.
- `indicatorSize(float)` → Spinner — Sets both preferred width and preferred height simultaneously.
- `radius(float)` → Spinner (default: `0`) — Sets an explicit indicator radius in UI pixels, or 0 to derive from bounds.
- `segments(int)` → Spinner (default: `8`) — Sets the segment count for segmented spinner styles.
- `speed(float)` → Spinner (default: `1`) — Sets the animation speed multiplier.
- `style()` → Style — Returns the active spinner visual style preset.
- `style(Style)` → Spinner (default: `default`) — Sets the spinner visual style preset.
- `thickness(float)` → Spinner (default: `3`) — Sets the stroke thickness in UI pixels.

