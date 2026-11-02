# `Sparkline` (extends `WidgetBase`)

Compact mini trendline and telemetry chart widget with fill gradient and hover data points.

- **Purpose:** Displays high-density numerical trends in compact telemetry graphs.

### USE WHEN

- Server TPS, player ping, FPS history, or memory usage monitors.
- Compact metric dashboards embedded into status bars.

### CRITICAL RULES & GOTCHAS:

- Finite values define the vertical range; NaN and infinities split the line into separate runs.
- A series with one value is rendered as one point; a constant series is centered vertically.
- PointMode controls which points are drawn and can be hit-tested; NONE disables point interaction.
- Hovering chooses the nearest point by horizontal coordinate and click emits SparkPointClickEvent.

- **Related:** `Chart`, `WidgetBase`

### API

- `Sparkline()` — Creates an empty compact trend chart using the default preferred dimensions.

### DATA

- `values(List<? extends Number>)` → Sparkline — Replaces the sparkline series from Number values; null clears the series and null entries become zero.
- `values(float[])` → Sparkline — Replaces the sparkline series using primitive floats without boxing.

### APPEARANCE

- `pointMode(PointMode)` → Sparkline (default: `extrema`) — Controls marker visibility and point hit-testing; null disables point rendering and interaction.

### INTERACTION

- `onPointClick(EventListener<? super SparkPointClickEvent>)` → EventSubscription — Subscribes to clicks on interactive sparkline points.

### EXAMPLES

#### Show a compact metric trend

```java
new Sparkline().values(List.of(60, 58, 59, 62, 60)).pointMode(Sparkline.PointMode.ALL);
```

