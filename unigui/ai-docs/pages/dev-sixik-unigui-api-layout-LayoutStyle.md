# `LayoutStyle`

Fluent rules used by the layout system to measure and place a widget.

- **Purpose:** Defines size requests, spacing, flex/grid layout, positioning, and overflow without manually calculating screen coordinates.

### USE WHEN

- Configure a widget through `widget.layout(style -> ...)`.
- Implement a custom container or update several layout properties as one batch.

### CRITICAL RULES & GOTCHAS:

- `width` and `height` are requested sizes, not guaranteed final sizes: the parent, flex/grid rules, and min/max constraints may change them.
- `fill()` requests 100% width and height; `expand()` takes remaining space only on the flex parent's main axis.
- `margin` is outside the widget and separates it from siblings; `padding` is inside and reduces content space.
- Grow/shrink are flex-distribution weights, not percentages or standalone dimensions.
- `justifyContent` works on the main axis, `alignItems` on the cross axis; `alignContent` matters only when wrapping creates multiple lines.
- Grid track placement coordinates are 1-based (`gridColumn(1, 1)`); non-positive values (`0` or less) trigger automatic placement.
- `PositionType.ABSOLUTE` removes the widget from flex/grid flow; position it using `left`, `top`, `right`, `bottom`, or `inset`.
- In this implementation `PositionType.FIXED` is relative to the nearest layout host, not the screen or window.
- `update(style -> ...)` batches multiple property changes to emit a single notification upon completion.
- The CSS-like properties are a UniGUI layout subset and do not promise full CSS compatibility.

- **Related:** `WidgetBase`, `SizeValue`, `EdgeInsets`, `GridTrack`, `FlexDirection`, `PositionType`

### BATCHING & COPIES

- `update(Consumer<LayoutStyle>)` → LayoutStyle — Applies several changes and invokes the change callback at most once after the outer block completes.
- `copy()` → LayoutStyle — Returns an independent copy of the settings without the owner's change callback.
- `copyFrom(LayoutStyle)` → LayoutStyle — Copies all layout properties from another style while preserving this object's callback.

### SIZE & POSITION

- `position(PositionType)` → LayoutStyle — Selects normal-flow participation (`RELATIVE`/`STATIC`) or out-of-flow positioning (`ABSOLUTE`/`FIXED`).
- `size(SizeValue, SizeValue)` → LayoutStyle — Sets width and height as `auto`, pixels, percentages, or intrinsic `SizeValue` values.
- `size(float, float)` → LayoutStyle — Sets width and height in UI pixels; legacy `AUTO` is converted to `auto`.
- `sizePercent(float, float)` → LayoutStyle — Sets width and height as percentages of the parent's available size.
- `fixed(float, float)` → LayoutStyle — Sets the size and disables flex growth and shrink for a fixed element.
- `fill()` → LayoutStyle — Requests 100% of the available width and height; this is not the same as `expand()`.
- `inset(SizeValue, SizeValue, SizeValue, SizeValue)` → LayoutStyle — Sets distances to all parent edges, typically for `ABSOLUTE` positioning.

### SPACING & OVERFLOW

- `margin(EdgeInsets)` → LayoutStyle — Sets outer spacing that separates the widget from siblings without moving its inner content.
- `padding(EdgeInsets)` → LayoutStyle — Sets inner spacing between the container bounds and its content.
- `overflowX(Overflow)` → LayoutStyle — Controls content that exceeds the width (`VISIBLE`, `HIDDEN`, or `AUTO`).
- `overflowY(Overflow)` → LayoutStyle — Controls content that exceeds the height; scrolling is implemented by `ScrollView`.
- `overflow(Overflow)` → LayoutStyle — Sets overflow behavior for both axes at once.

### FLEX LAYOUT

- `flexFlow(FlexDirection, FlexWrap)` → LayoutStyle — Sets the flex container's main axis and wrapping mode together.
- `gap(float)` → LayoutStyle — Sets the same gap between rows and columns.
- `spacing(Orientation, float)` → LayoutStyle — Sets spacing along the selected orientation and clears the cross-axis gap.
- `gap(float, float)` → LayoutStyle — Sets row and column gaps independently.
- `flex(float, float, SizeValue)` → LayoutStyle — Sets flex grow/shrink and the base size before free-space distribution.
  - Rules:
    - Grow and shrink are weights; `basis` is not the final width or height.
- `flexAuto()` → LayoutStyle — Enables `grow = 1`, `shrink = 1`, and `basis = auto`.
- `flexInitial()` → LayoutStyle — Keeps the base size and allows only flex shrink (`0 1 auto`).
- `flexNone()` → LayoutStyle — Disables flex growth and shrink (`0 0 auto`).
- `expand()` → LayoutStyle — Takes remaining free space on the main axis (`1 1 0px`).

### GRID & ALIGNMENT

- `aspectRatio(float)` → LayoutStyle — Sets a positive width-to-height ratio when one axis remains `auto`.
- `gridTemplateColumns(List<GridTrack>)` → LayoutStyle — Sets an explicit column template from `GridTrack`; an empty list keeps automatic sizing.
- `gridTemplateRows(List<GridTrack>)` → LayoutStyle — Sets an explicit row template from `GridTrack`.
- `gridAutoColumns(List<GridTrack>)` → LayoutStyle — Sets sizes for automatically created columns when explicit columns are exhausted.
- `gridAutoRows(List<GridTrack>)` → LayoutStyle — Sets sizes for automatically created rows when explicit rows are exhausted.
- `gridAutoFlow(GridAutoFlow)` → LayoutStyle — Selects the direction used to find free cells during auto-placement.
- `gridColumn(int, int)` → LayoutStyle — Pins a child to a grid column and sets its span; `start <= 0` means auto-placement.
- `gridRow(int, int)` → LayoutStyle — Pins a child to a grid row and sets its span; `start <= 0` means auto-placement.
- `zIndex(int)` → LayoutStyle — Sets draw order among siblings; lower values are drawn earlier.
- `alignItems(Align)` → LayoutStyle — Aligns a container's children on the cross axis.
- `alignSelf(Align)` → LayoutStyle — Overrides the parent's cross-axis alignment for this child.
- `alignContent(AlignContent)` → LayoutStyle — Distributes multiple wrapped lines on the cross axis.
  - Rules:
    - Has no visible effect when wrapping produces only one line.
- `justifyContent(Justify)` → LayoutStyle — Distributes a container's children on the main axis.
- `center()` → LayoutStyle — Centers a container's children on both the main and cross axes.
- `centerSelf()` → LayoutStyle — Centers this flex item on its parent's cross axis.

### EXAMPLES

#### Fixed header and expanding content

```java
VBox root = new VBox();
Label header = new Label("Files");
VBox content = new VBox();

header.layout(style -> style.height(24.0f).flexNone());
content.layout(style -> style.expand().padding(12.0f));
root.addChild(header);
root.addChild(content);
```

#### Grid with fixed and flexible columns

```java
LayoutStyle grid = new LayoutStyle()
        .gridTemplateColumns(GridTrack.px(96.0f), GridTrack.fr(1.0f))
        .gridAutoRowsOf(GridTrack.auto())
        .gap(8.0f, 12.0f);
```

