# `ScrollView` (extends `WidgetBase`)

Scrollable viewport container that hosts a single child content widget with horizontal and vertical scrolling support.

- **Purpose:** Displays large content inside a constrained viewing region, managing scroll offsets, clamping, wheel inputs, and scrollbar rendering.

### USE WHEN

- Child content may exceed visible container boundaries: item lists, text views, large canvas boards.
- Require customizable overlay or reserved scrollbars with smooth wheel scrolling and Shift-axis toggling.

### CRITICAL RULES & GOTCHAS:

- A ScrollView hosts exactly one content widget set via content(Widget).
- Scrolling axes are controlled by layoutStyle().overflowX/Y(...), with Overflow.AUTO showing bars when content overflows, and Overflow.HIDDEN disabling that axis.
- By default overlayScrollBars is enabled, meaning scrollbars float over content rather than shrinking the viewport.

- **Related:** `Box`, `VBox`, `PanelWidget`

### CONTENT

- `content(Widget)` → ScrollView — Sets the single content widget inside this scroll viewport.

### NAVIGATION

- `scrollTo(float, float)` → ScrollView — Scrolls to absolute coordinates clamped to valid bounds.
- `scrollBy(float, float)` → ScrollView — Scrolls relatively by dx and dy offsets.

### BEHAVIOR

- `scrollStep(float)` → ScrollView (default: `16`) — Sets scroll step multiplier in pixels per wheel unit.

### APPEARANCE

- `overlayScrollBars(boolean)` → ScrollView (default: `true`) — Toggles overlay vs reserved scrollbar mode.

### EXAMPLES

#### Create ScrollView with vertical auto scroll

```java
ScrollView scroll = new ScrollView(largeWidget).scrollStep(18.0f);
scroll.layout(style -> style.overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO));
```

