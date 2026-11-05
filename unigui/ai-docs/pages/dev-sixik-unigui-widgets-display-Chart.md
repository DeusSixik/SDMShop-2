# `Chart` (extends `Sparkline`)

Data visualization chart widget supporting bar and line visualizations, axes, and interactive hover tooltips.

- **Purpose:** Provides full-featured data chart visualization for metrics, histograms, and statistics.

### USE WHEN

- Statistical reports, recipe yields, or economy transaction history.
- Switching between line graphs and bar histograms.

### CRITICAL RULES & GOTCHAS:

- type() selects between Type.LINE and Type.BAR rendering modes.
- axisColor configures coordinate axis line and tick markings.
- Category label at index i belongs to value at index i; missing labels are allowed.
- Bar charts include zero in the value scale, so positive and negative bars share a zero baseline.
- Non-finite values create gaps and do not participate in the plotted scale.

- **Related:** `Sparkline`, `WidgetBase`

### API

- `Chart()` — Creates a chart with the default preferred dimensions and line rendering mode.

### DATA

- `values(List<? extends Number>)` → Chart — Replaces the chart data from a list of Number values; null clears the data and null elements become zero.
- `categories(List<String>)` → Chart — Sets optional x-axis labels; category i labels value i and labels may be omitted or left null.

### APPEARANCE

- `type(Type)` → Chart (default: `line`) — Selects line or bar rendering; null resets to line mode.
- `barValuesVisible(boolean)` → Chart (default: `true`) — Enables or hides numeric labels on bars; this affects only BAR mode.

### CUSTOM RENDERING

- `barRenderer(BarRenderer)` → Chart — Installs an optional callback that replaces the default bar drawing.

### INTERACTION

- `onBarClick(EventListener<? super ChartBarClickEvent>)` → EventSubscription — Subscribes to clicks on bars and provides the clicked bar's data and rendered geometry.

### EXAMPLES

#### Build a categorized bar chart

```java
new Chart().type(Chart.Type.BAR).values(List.of(12, -4, 18))
        .categories(List.of("A", "B", "C"));
```

