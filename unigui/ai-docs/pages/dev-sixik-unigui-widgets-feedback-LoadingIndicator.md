# `LoadingIndicator` (extends `Box`)

Multi-mode loading and progress feedback widget supporting spinner, bar, pulse, and dot animations.

### API

- `LoadingIndicator()` — Constructs a LoadingIndicator initialized with SPINNER mode and default styling.
- `accentColor()` → MutableColor — Returns the primary accent color.
- `activeDots()` → int — Returns the active highlighted dot count.
- `activeDots(int)` → LoadingIndicator (default: `4`) — Sets the active highlighted dot count.
- `angle()` → float — Returns the arc sweep angle in radians.
- `angle(float)` → LoadingIndicator (default: `4.555`) — Sets the arc sweep angle in radians.
- `arcs()` → int — Returns the arc count for multi-arc styles.
- `arcs(int)` → LoadingIndicator (default: `3`) — Sets the arc count for multi-arc styles.
- `dots()` → int — Returns the total dot count for dotted styles.
- `dots(int)` → LoadingIndicator (default: `8`) — Sets the total dot count for dotted styles.
- `indicatorSize(float)` → LoadingIndicator — Sets both preferred width and height simultaneously in UI pixels.
- `mode()` → Mode — Returns the active display mode (SPINNER, BAR, PULSE, DOTS, etc.).
- `mode(Mode)` → LoadingIndicator (default: `spinner`) — Sets the loading indicator display mode.
- `phase()` → float — Returns the current animation phase normalized between 0.0 and 1.0.
- `phase(float)` → LoadingIndicator (default: `0`) — Manually sets the animation phase value.
- `preferredHeight()` → float — Returns the preferred indicator height.
- `preferredHeight(float)` → LoadingIndicator (default: `24`) — Sets the preferred indicator height in UI pixels.
- `preferredSize(float, float)` → LoadingIndicator — Sets independent preferred width and height.
- `preferredWidth()` → float — Returns the preferred indicator width.
- `preferredWidth(float)` → LoadingIndicator (default: `24`) — Sets the preferred indicator width in UI pixels.
- `radius()` → float — Returns the explicit indicator radius in UI pixels, or 0 if auto-computed.
- `radius(float)` → LoadingIndicator (default: `0`) — Sets an explicit indicator radius in UI pixels.
- `running()` → boolean — Returns true if the animation is currently running.
- `running(boolean)` → LoadingIndicator (default: `true`) — Starts or stops the animation.
- `secondaryColor()` → MutableColor — Returns the secondary highlight color.
- `segments()` → int — Returns the segment count for segmented styles.
- `segments(int)` → LoadingIndicator (default: `8`) — Sets the segment count for segmented styles.
- `speed()` → float — Returns the animation speed multiplier.
- `speed(float)` → LoadingIndicator (default: `1`) — Sets the animation speed multiplier.
- `spinnerStyle()` → Style — Returns the active spinner style preset.
- `spinnerStyle(Style)` → LoadingIndicator (default: `default`) — Sets the spinner style preset.
- `start()` → LoadingIndicator — Starts the indicator animation.
- `stop()` → LoadingIndicator — Stops the indicator animation.
- `thickness()` → float — Returns the stroke thickness in UI pixels.
- `thickness(float)` → LoadingIndicator (default: `3`) — Sets the stroke thickness in UI pixels.
- `trackColor()` → MutableColor — Returns the background track color.
- `useDefaultPreferredSize()` → LoadingIndicator — Resets dimensions to the default preferred size for the current mode.

