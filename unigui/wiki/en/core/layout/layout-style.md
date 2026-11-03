---
title: "LayoutStyle"
slug: "layout-style"
category: "Core/Layout"
locale: "en"
order: 20
---

# LayoutStyle

Rules a parent uses to measure and place a widget: size, spacing, flex, grid, position, and overflow.

## Overview

Configure a widget's geometry using `widget.layout(style -> ...)`. Never manually calculate screen coordinates. Every size is a **request**: the parent container, flex/grid rules, and min/max constraints determine final bounds.

| Concept | Meaning |
| --- | --- |
| **Parent / Child** | The parent container measures and places widgets; children configure their layout behavior. |
| **Preferred Size** | Requested dimensions (`width`, `height`, `size`). May be clamped or redistributed by layout rules. |
| **`SizeValue.auto()`** | Automatically computes size from widget content or available space. Not synonymous with 100%. |
| **Main Axis** | Primary flex direction: width for `ROW`, height for `COLUMN`. |
| **Cross Axis** | Perpendicular axis to the main axis (height for `ROW`, width for `COLUMN`). |
| **Free Space** | Remaining space after placing base sizes and margins. Distributed via `flexGrow` and `flexShrink`. |

!!! warning "UniGUI Layout vs. Full Web CSS"
    While naming mirrors CSS properties, UniGUI implements a predictable, desktop-optimized layout engine. `PositionType.FIXED` is anchored relative to the nearest layout host (window or dialog), not the entire physical screen.

## Size and positioning

Request dimensions using `width`, `height`, `size`, or `sizePercent`. Dimensions can be specified in UI pixels, parent percentages, or `auto`. `minWidth` / `minHeight` and `maxWidth` / `maxHeight` enforce strict bounds.

| Method / Helper | Type | Description |
| --- | --- | --- |
| `width(w)` / `height(h)` | Dimensions | Sets width or height (`px`, `percent`, or `auto`). |
| `size(w, h)` | Shortcut | Sets both width and height in UI pixels simultaneously. |
| `sizePercent(w, h)` | Percentage | Requests percentages of the parent's available size. |
| `fixed(w, h)` | Fixed Bounds | Sets pixel size and disables flex growth and shrink (`0 0 auto`). |
| `fill()` | 100% Parent | Requests 100% width and 100% height (`sizePercent(100, 100)`). |
| `expand()` | Flex Remainder | Consumes remaining free space along the flex parent's **main axis** (`1 1 0px`). |
| `position(ABSOLUTE)` | Out-of-Flow | Removes widget from normal flow; position with `left`, `top`, `right`, `bottom`, or `inset`. |

!!! tip "Choosing between `fill()` and `expand()`"
    Use `fill()` when a widget must occupy 100% of both parent dimensions regardless of siblings.
    Use `expand()` when a flex child (such as a scrollable body) should absorb whatever free space remains after fixed elements (such as headers and footers).

## Spacing and overflow

Spacing governs margins around the widget, padding inside the border, and content clipping when elements exceed bounds.

| Spacing Property | Placement | Purpose |
| --- | --- | --- |
| `margin(...)` | Exterior | Separates the widget from adjacent siblings without affecting internal layout. |
| `marginAutoHorizontal()` / `Vertical()` | Exterior | Absorbs available free space, centering or pushing the widget against edges. |
| `padding(...)` | Interior | Insets children away from the widget's outer boundary. |
| `gap(row, col)` | Between Items | Uniform or independent spacing between flex lines/items or grid tracks. |

| Overflow Mode | Visual Behavior |
| --- | --- |
| `Overflow.VISIBLE` | Content renders beyond widget boundaries without clipping (default). |
| `Overflow.HIDDEN` | Content extending past boundaries is clipped. |
| `Overflow.AUTO` | Flags the axis as scrollable. Actual scrolling interactions are handled by `ScrollView`. |

!!! note "Scrolling Architecture"
    Setting `overflowY(Overflow.AUTO)` communicates intent to the layout engine, but scrolling interaction and scrollbars are provided by wrapping widgets in a `ScrollView`.

## Flex layout

Flexbox arranges children sequentially along a primary main axis (`ROW` or `COLUMN`).

### Container Configuration

| Property | Axis / Scope | Values | Effect |
| --- | --- | --- | --- |
| `flexDirection` | Main Axis | `ROW`, `COLUMN`, `ROW_REVERSE`, `COLUMN_REVERSE` | Direction in which children are placed. |
| `flexWrap` | Wrapping | `NOWRAP`, `WRAP`, `WRAP_REVERSE` | Keeps children on one line or wraps to multiple lines. |
| `justifyContent` | **Main** Axis | `START`, `CENTER`, `END`, `SPACE_BETWEEN`, `SPACE_AROUND`, `SPACE_EVENLY` | Distributes children along the main axis. |
| `alignItems` | **Cross** Axis | `STRETCH`, `START`, `CENTER`, `END`, `BASELINE` | Aligns children perpendicular to the main axis. |
| `alignContent` | Multi-line | `START`, `CENTER`, `END`, `STRETCH`, `SPACE_BETWEEN` | Distributes wrapped lines (only active with `WRAP`). |
| `gap(row, col)` | Spacing | UI pixels (e.g. `gap(8.0f)`) | Space between items and between wrapped lines. |

### Child Item Weights & Presets

| Preset / Helper | Equivalent | Behavior |
| --- | --- | --- |
| `flexNone()` | `0 0 auto` | Rigid: keeps requested/content size; never grows or shrinks. |
| `expand()` | `1 1 0px` | Greedy: consumes all remaining free space on the parent's main axis. |
| `flexAuto()` | `1 1 auto` | Flexible: sizes to content, grows if space is free, shrinks if squeezed. |
| `flexInitial()` | `0 1 auto` | Shrink-only: keeps requested size, shrinks when crowded, does not grow. |
| `flex(grow, shrink, basis)` | Explicit | Custom weights for growth and shrinkage; `basis` is the starting size. |
| `alignSelf(Align)` | Override | Overrides parent's `alignItems` for this specific child. |

## Grid and alignment

Grid layout arranges widgets into a 2D matrix of columns and rows defined by `GridTrack` track specifications.

### Track Definitions (`GridTrack`)

| Track Builder | Type | Description | Example |
| --- | --- | --- | --- |
| `GridTrack.px(n)` | Fixed | Exact track size in UI pixels. | `GridTrack.px(96.0f)` |
| `GridTrack.fr(n)` | Fractional | Proportional share of leftover free space. | `GridTrack.fr(1.0f)` |
| `GridTrack.percent(n)` | Percentage | Percentage of the grid container's dimension. | `GridTrack.percent(33.3f)` |
| `GridTrack.auto()` | Automatic | Sized automatically to fit largest cell content. | `GridTrack.auto()` |
| `GridTrack.minmax(min, max)` | Constrained | Clamps track size within minimum and maximum limits. | `GridTrack.minmax(px(50), fr(1))` |

### Grid Properties & Placement

| Method | Target | Description |
| --- | --- | --- |
| `gridTemplateColumns(...)` | Container | Explicit list of column tracks. |
| `gridTemplateRows(...)` | Container | Explicit list of row tracks. |
| `gridAutoColumns(...)` / `gridAutoRows(...)` | Container | Track sizes applied automatically beyond explicit templates. |
| `gridAutoFlow(GridAutoFlow)` | Container | Traversal direction for auto-placement: `ROW`, `COLUMN`, `ROW_DENSE`, `COLUMN_DENSE`. |
| `gridColumn(start, span)` | Child | 1-based column start index and span width (`<= 0` requests auto-placement). |
| `gridRow(start, span)` | Child | 1-based row start index and span height (`<= 0` requests auto-placement). |
| `alignSelf` / `centerSelf` | Child | Aligns this individual child inside its grid cell. |
| `zIndex(int)` | Child | Layering order among siblings (higher values render on top). |

## Batching and copies

A widget's live `LayoutStyle` automatically triggers layout reflow whenever a property changes. When applying several properties together, group mutations within `update(...)` to recalculate layout only once after the block finishes.

```java
style.update(s -> s
        .width(240.0f)
        .padding(12.0f)
        .flexNone());
```

| Lifecycle Method | Preserves Callback | When to Use |
| --- | --- | --- |
| `update(Consumer<LayoutStyle>)` | Yes | Batch multiple modifications; triggers exactly one recalculation. Safe to nest. |
| `copy()` | No | Creates a detached snapshot of all values with no widget callback attached. |
| `copyFrom(LayoutStyle other)` | Yes | Overwrites all property values from another style, keeping the existing owner callback. |

## Fixed header, expanding body

```java
VBox root = new VBox();
Label header = new Label("Files");
VBox content = new VBox();

header.layout(style -> style.height(24.0f).flexNone());
content.layout(style -> style.expand().padding(12.0f));
root.addChild(header);
root.addChild(content);
```

## Fixed column plus a flexible column

```java
LayoutStyle grid = new LayoutStyle()
        .gridTemplateColumns(GridTrack.px(96.0f), GridTrack.fr(1.0f))
        .gridAutoRowsOf(GridTrack.auto())
        .gap(8.0f, 12.0f);
```

## API

### Size & Position

| Method | Description |
| --- | --- |
| [`position(PositionType)`](#positionpositiontype) | Selects normal-flow participation (RELATIVE/STATIC) or out-of-flow positioning (ABSOLUTE/FIXED). |
| [`width(SizeValue)`](#widthsizevalue) | Requests desired width as auto, UI pixels, or a percentage of the parent. |
| [`width(float)`](#widthfloat) | Requests desired width in UI pixels. |
| [`height(SizeValue)`](#heightsizevalue) | Requests desired height as auto, UI pixels, or a percentage of the parent. |
| [`height(float)`](#heightfloat) | Requests desired height in UI pixels. |
| [`size(SizeValue, SizeValue)`](#sizesizevalue-sizevalue) | Sets both width and height using SizeValue instances. |
| [`size(float, float)`](#sizefloat-float) | Requests width and height in UI pixels. The legacy `LayoutConstraints.AUTO` value becomes `auto`. |
| [`sizePercent(float, float)`](#sizepercentfloat-float) | Requests width and height as percentages of the parent container. |
| [`fixed(float, float)`](#fixedfloat-float) | Sets exact pixel width and height, completely disabling flex growth and shrinkage (0 0 auto). |
| [`fill()`](#fill) | Requests 100% width and 100% height of the parent container. |
| [`minWidth(SizeValue)`](#minwidthsizevalue) | Sets the minimum allowable width to prevent unwanted collapsing. |
| [`maxWidth(SizeValue)`](#maxwidthsizevalue) | Sets the upper allowable width constraint. |
| [`minHeight(SizeValue)`](#minheightsizevalue) | Sets the minimum allowable height. |
| [`maxHeight(SizeValue)`](#maxheightsizevalue) | Sets the upper allowable height constraint. |
| [`inset(SizeValue, SizeValue, SizeValue, SizeValue)`](#insetsizevalue-sizevalue-sizevalue-sizevalue) | Sets distances to all parent edges, typically used for ABSOLUTE positioning. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`position(PositionType)` {: #positionpositiontype data-toc-label="position(PositionType)" }

**Position mode**

Selects normal-flow participation (RELATIVE/STATIC) or out-of-flow positioning (ABSOLUTE/FIXED).

| Parameter | Type | Description |
| --- | --- | --- |
| `position` | `PositionType` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`width(SizeValue)` {: #widthsizevalue data-toc-label="width(SizeValue)" }

**Set width (SizeValue)**

Requests desired width as auto, UI pixels, or a percentage of the parent.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`width(float)` {: #widthfloat data-toc-label="width(float)" }

**Set width in pixels**

Requests desired width in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `pixels` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`height(SizeValue)` {: #heightsizevalue data-toc-label="height(SizeValue)" }

**Set height (SizeValue)**

Requests desired height as auto, UI pixels, or a percentage of the parent.

| Parameter | Type | Description |
| --- | --- | --- |
| `height` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`height(float)` {: #heightfloat data-toc-label="height(float)" }

**Set height in pixels**

Requests desired height in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `pixels` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`size(SizeValue, SizeValue)` {: #sizesizevalue-sizevalue data-toc-label="size(SizeValue, SizeValue)" }

**Set size (SizeValue)**

Sets both width and height using SizeValue instances.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `SizeValue` | — |
| `height` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`size(float, float)` {: #sizefloat-float data-toc-label="size(float, float)" }

**Set pixel size**

Requests width and height in UI pixels. The legacy `LayoutConstraints.AUTO` value becomes `auto`.

This is a size request, not a guaranteed final size. Flex, grid, min/max, and the parent can still change the result. It does not disable flex grow or shrink; use `fixed` for that.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width in UI pixels, or `LayoutConstraints.AUTO` for content-based sizing. |
| `height` | `float` | Height in UI pixels, or `LayoutConstraints.AUTO` for content-based sizing. |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`sizePercent(float, float)` {: #sizepercentfloat-float data-toc-label="sizePercent(float, float)" }

**Set size percent**

Requests width and height as percentages of the parent container.

| Parameter | Type | Description |
| --- | --- | --- |
| `widthPercent` | `float` | — |
| `heightPercent` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`fixed(float, float)` {: #fixedfloat-float data-toc-label="fixed(float, float)" }

**Fixed size**

Sets exact pixel width and height, completely disabling flex growth and shrinkage (0 0 auto).

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | — |
| `height` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`fill()` {: #fill data-toc-label="fill()" }

**Fill parent**

Requests 100% width and 100% height of the parent container.

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`minWidth(SizeValue)` {: #minwidthsizevalue data-toc-label="minWidth(SizeValue)" }

**Minimum width**

Sets the minimum allowable width to prevent unwanted collapsing.

| Parameter | Type | Description |
| --- | --- | --- |
| `minWidth` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`maxWidth(SizeValue)` {: #maxwidthsizevalue data-toc-label="maxWidth(SizeValue)" }

**Maximum width**

Sets the upper allowable width constraint.

| Parameter | Type | Description |
| --- | --- | --- |
| `maxWidth` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`minHeight(SizeValue)` {: #minheightsizevalue data-toc-label="minHeight(SizeValue)" }

**Minimum height**

Sets the minimum allowable height.

| Parameter | Type | Description |
| --- | --- | --- |
| `minHeight` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`maxHeight(SizeValue)` {: #maxheightsizevalue data-toc-label="maxHeight(SizeValue)" }

**Maximum height**

Sets the upper allowable height constraint.

| Parameter | Type | Description |
| --- | --- | --- |
| `maxHeight` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`inset(SizeValue, SizeValue, SizeValue, SizeValue)` {: #insetsizevalue-sizevalue-sizevalue-sizevalue data-toc-label="inset(SizeValue, SizeValue, SizeValue, SizeValue)" }

**Edge insets**

Sets distances to all parent edges, typically used for ABSOLUTE positioning.

| Parameter | Type | Description |
| --- | --- | --- |
| `left` | `SizeValue` | — |
| `top` | `SizeValue` | — |
| `right` | `SizeValue` | — |
| `bottom` | `SizeValue` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

### Spacing & Overflow

| Method | Description |
| --- | --- |
| [`margin(EdgeInsets)`](#marginedgeinsets) | Sets exterior spacing separating the widget from siblings. |
| [`margin(float)`](#marginfloat) | Sets the same exterior spacing on all four sides in UI pixels. |
| [`margin(float, float)`](#marginfloat-float) | Sets horizontal and vertical exterior margins in UI pixels. |
| [`marginAutoHorizontal()`](#marginautohorizontal) | Enables left and right auto-margins to absorb remaining horizontal space. |
| [`marginAutoVertical()`](#marginautovertical) | Enables top and bottom auto-margins to absorb remaining vertical space. |
| [`padding(EdgeInsets)`](#paddingedgeinsets) | Sets interior spacing between widget boundaries and its children. |
| [`padding(float)`](#paddingfloat) | Sets the same inner padding on all four sides, in UI pixels. |
| [`padding(float, float)`](#paddingfloat-float) | Sets horizontal and vertical inner paddings in UI pixels. |
| [`overflowX(Overflow)`](#overflowxoverflow) | Controls content extending past the left and right boundaries (VISIBLE, HIDDEN, AUTO). |
| [`overflowY(Overflow)`](#overflowyoverflow) | Controls content extending past the top and bottom boundaries; scrolling handled by [ScrollView](<../../widgets/containers/scroll-view.md>). |
| [`overflow(Overflow)`](#overflowoverflow) | Sets overflow behavior simultaneously for both horizontal and vertical axes. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`margin(EdgeInsets)` {: #marginedgeinsets data-toc-label="margin(EdgeInsets)" }

**Margin (EdgeInsets)**

Sets exterior spacing separating the widget from siblings.

| Parameter | Type | Description |
| --- | --- | --- |
| `margin` | `EdgeInsets` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`margin(float)` {: #marginfloat data-toc-label="margin(float)" }

**Uniform margin**

Sets the same exterior spacing on all four sides in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `all` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`margin(float, float)` {: #marginfloat-float data-toc-label="margin(float, float)" }

**Margin by axes**

Sets horizontal and vertical exterior margins in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `horizontal` | `float` | — |
| `vertical` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`marginAutoHorizontal()` {: #marginautohorizontal data-toc-label="marginAutoHorizontal()" }

**Auto horizontal margins**

Enables left and right auto-margins to absorb remaining horizontal space.

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`marginAutoVertical()` {: #marginautovertical data-toc-label="marginAutoVertical()" }

**Auto vertical margins**

Enables top and bottom auto-margins to absorb remaining vertical space.

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

**Padding (EdgeInsets)**

Sets interior spacing between widget boundaries and its children.

| Parameter | Type | Description |
| --- | --- | --- |
| `padding` | `EdgeInsets` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`padding(float)` {: #paddingfloat data-toc-label="padding(float)" }

**Uniform padding**

Sets the same inner padding on all four sides, in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `all` | `float` | Padding applied to the left, top, right, and bottom edges. |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`padding(float, float)` {: #paddingfloat-float data-toc-label="padding(float, float)" }

**Padding by axes**

Sets horizontal and vertical inner paddings in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `horizontal` | `float` | — |
| `vertical` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`overflowX(Overflow)` {: #overflowxoverflow data-toc-label="overflowX(Overflow)" }

**Horizontal overflow**

Controls content extending past the left and right boundaries (VISIBLE, HIDDEN, AUTO).

| Parameter | Type | Description |
| --- | --- | --- |
| `overflowX` | `Overflow` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`overflowY(Overflow)` {: #overflowyoverflow data-toc-label="overflowY(Overflow)" }

**Vertical overflow**

Controls content extending past the top and bottom boundaries; scrolling handled by [ScrollView](<../../widgets/containers/scroll-view.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `overflowY` | `Overflow` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`overflow(Overflow)` {: #overflowoverflow data-toc-label="overflow(Overflow)" }

**Uniform overflow**

Sets overflow behavior simultaneously for both horizontal and vertical axes.

| Parameter | Type | Description |
| --- | --- | --- |
| `overflow` | `Overflow` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

### Flex Layout

| Method | Description |
| --- | --- |
| [`flexDirection(FlexDirection)`](#flexdirectionflexdirection) | Sets the main layout axis of the flex container (ROW or COLUMN). |
| [`flexWrap(FlexWrap)`](#flexwrapflexwrap) | Controls single-line (NOWRAP) vs multi-line (WRAP) item flow. |
| [`flexFlow(FlexDirection, FlexWrap)`](#flexflowflexdirection-flexwrap) | Sets flex container direction and wrap mode together. |
| [`gap(float)`](#gapfloat) | Sets uniform spacing between rows and between columns in UI pixels. |
| [`gap(float, float)`](#gapfloat-float) | Sets row gap and column gap independently in UI pixels. |
| [`flex(float, float, SizeValue)`](#flexfloat-float-sizevalue) | Sets grow weight, shrink weight, and the base size before free-space distribution. |
| [`flexNone()`](#flexnone) | Disables flex growth and shrinkage, preserving exact requested/content size (0 0 auto). |
| [`expand()`](#expand) | Greedily consumes all remaining free space along the flex parent's main axis (1 1 0px). |
| [`flexAuto()`](#flexauto) | Sizes widget from content, growing to absorb leftover space or shrinking if crowded (1 1 auto). |
| [`flexInitial()`](#flexinitial) | Sizes widget from base dimensions; shrinks when crowded but never grows (0 1 auto). |
| [`justifyContent(Justify)`](#justifycontentjustify) | Distributes children along the container's main axis (START, CENTER, END, SPACE_BETWEEN). |
| [`alignItems(Align)`](#alignitemsalign) | Aligns children along the container's cross axis (STRETCH, START, CENTER, END). |
| [`alignSelf(Align)`](#alignselfalign) | Overrides the parent's alignItems alignment rule for this specific child widget. |
| [`alignContent(AlignContent)`](#aligncontentaligncontent) | Distributes wrapped lines along the cross axis when flexWrap creates multiple rows. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexDirection(FlexDirection)` {: #flexdirectionflexdirection data-toc-label="flexDirection(FlexDirection)" }

**Flex direction**

Sets the main layout axis of the flex container (ROW or COLUMN).

| Parameter | Type | Description |
| --- | --- | --- |
| `flexDirection` | `FlexDirection` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexWrap(FlexWrap)` {: #flexwrapflexwrap data-toc-label="flexWrap(FlexWrap)" }

**Flex wrap**

Controls single-line (NOWRAP) vs multi-line (WRAP) item flow.

| Parameter | Type | Description |
| --- | --- | --- |
| `flexWrap` | `FlexWrap` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexFlow(FlexDirection, FlexWrap)` {: #flexflowflexdirection-flexwrap data-toc-label="flexFlow(FlexDirection, FlexWrap)" }

**Flex flow**

Sets flex container direction and wrap mode together.

| Parameter | Type | Description |
| --- | --- | --- |
| `direction` | `FlexDirection` | — |
| `wrap` | `FlexWrap` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gap(float)` {: #gapfloat data-toc-label="gap(float)" }

**Uniform gap**

Sets uniform spacing between rows and between columns in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `gap` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gap(float, float)` {: #gapfloat-float data-toc-label="gap(float, float)" }

**Gap by axes**

Sets row gap and column gap independently in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `rowGap` | `float` | — |
| `columnGap` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flex(float, float, SizeValue)` {: #flexfloat-float-sizevalue data-toc-label="flex(float, float, SizeValue)" }

**Flex shorthand**

Sets grow weight, shrink weight, and the base size before free-space distribution.

Grow and shrink are weights, not percentages. `basis` is the starting main-axis size; `auto` uses the width or height request. The parent may still change the final size.

| Parameter | Type | Description |
| --- | --- | --- |
| `grow` | `float` | Share of leftover main-axis space. `0` does not grow. |
| `shrink` | `float` | Share of the deficit when children do not fit. `0` does not shrink. |
| `basis` | `SizeValue` | Base main-axis size (`auto`, pixels, or percent). Null becomes `auto`. |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexNone()` {: #flexnone data-toc-label="flexNone()" }

**Flex none**

Disables flex growth and shrinkage, preserving exact requested/content size (0 0 auto).

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`expand()` {: #expand data-toc-label="expand()" }

**Expand on main axis**

Greedily consumes all remaining free space along the flex parent's main axis (1 1 0px).

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexAuto()` {: #flexauto data-toc-label="flexAuto()" }

**Flex auto**

Sizes widget from content, growing to absorb leftover space or shrinking if crowded (1 1 auto).

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`flexInitial()` {: #flexinitial data-toc-label="flexInitial()" }

**Flex initial**

Sizes widget from base dimensions; shrinks when crowded but never grows (0 1 auto).

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`justifyContent(Justify)` {: #justifycontentjustify data-toc-label="justifyContent(Justify)" }

**Justify content**

Distributes children along the container's main axis (START, CENTER, END, SPACE_BETWEEN).

| Parameter | Type | Description |
| --- | --- | --- |
| `justifyContent` | `Justify` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`alignItems(Align)` {: #alignitemsalign data-toc-label="alignItems(Align)" }

**Align items**

Aligns children along the container's cross axis (STRETCH, START, CENTER, END).

| Parameter | Type | Description |
| --- | --- | --- |
| `alignItems` | `Align` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`alignSelf(Align)` {: #alignselfalign data-toc-label="alignSelf(Align)" }

**Align self**

Overrides the parent's alignItems alignment rule for this specific child widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `alignSelf` | `Align` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`alignContent(AlignContent)` {: #aligncontentaligncontent data-toc-label="alignContent(AlignContent)" }

**Align content**

Distributes wrapped lines along the cross axis when flexWrap creates multiple rows.

| Parameter | Type | Description |
| --- | --- | --- |
| `alignContent` | `AlignContent` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

### Grid & Alignment

| Method | Description |
| --- | --- |
| [`gridTemplateColumns(List)`](#gridtemplatecolumnslist) | Sets the explicit column template. An empty or null list keeps automatic columns. |
| [`gridTemplateColumns(GridTrack[])`](#gridtemplatecolumnsgridtrack) | Sets explicit column template tracks using varargs. |
| [`gridTemplateRows(List)`](#gridtemplaterowslist) | Sets explicit row template tracks. |
| [`gridTemplateRows(GridTrack[])`](#gridtemplaterowsgridtrack) | Sets explicit row template tracks using varargs. |
| [`gridAutoColumns(List)`](#gridautocolumnslist) | Defines track sizes for implicit columns created beyond the explicit template. |
| [`gridAutoRows(List)`](#gridautorowslist) | Defines track sizes for implicit rows created beyond the explicit template. |
| [`gridAutoFlow(GridAutoFlow)`](#gridautoflowgridautoflow) | Controls placement direction during auto-placement (ROW, COLUMN, ROW_DENSE, COLUMN_DENSE). |
| [`gridColumn(int, int)`](#gridcolumnint-int) | Pins child to 1-based column start index and sets column span (<= 0 requests auto-placement). |
| [`gridRow(int, int)`](#gridrowint-int) | Pins child to 1-based row start index and sets row span (<= 0 requests auto-placement). |
| [`aspectRatio(float)`](#aspectratiofloat) | Sets desired width-to-height ratio when one dimension remains auto. |
| [`zIndex(int)`](#zindexint) | Sets drawing layer order among siblings; higher integers render on top. |
| [`center()`](#center) | Centers the container's children on both the main and cross axes. |
| [`centerSelf()`](#centerself) | Centers this flex item on its parent's cross axis (alignSelf(CENTER)). |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateColumns(List)` {: #gridtemplatecolumnslist data-toc-label="gridTemplateColumns(List)" }

**Grid columns**

Sets the explicit column template. An empty or null list keeps automatic columns.

Each `GridTrack` is one column: `px`, `percent`, `fr`, `auto`, or `minmax`. Tracks created beyond this template come from `gridAutoColumns`.

| Parameter | Type | Description |
| --- | --- | --- |
| `tracks` | `List` | Column tracks in order. Null or an empty list clears the template. |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateColumns(GridTrack[])` {: #gridtemplatecolumnsgridtrack data-toc-label="gridTemplateColumns(GridTrack[])" }

**Grid columns (varargs)**

Sets explicit column template tracks using varargs.

| Parameter | Type | Description |
| --- | --- | --- |
| `tracks` | `GridTrack[]` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateRows(List)` {: #gridtemplaterowslist data-toc-label="gridTemplateRows(List)" }

**Grid rows**

Sets explicit row template tracks.

| Parameter | Type | Description |
| --- | --- | --- |
| `tracks` | `List` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridTemplateRows(GridTrack[])` {: #gridtemplaterowsgridtrack data-toc-label="gridTemplateRows(GridTrack[])" }

**Grid rows (varargs)**

Sets explicit row template tracks using varargs.

| Parameter | Type | Description |
| --- | --- | --- |
| `tracks` | `GridTrack[]` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridAutoColumns(List)` {: #gridautocolumnslist data-toc-label="gridAutoColumns(List)" }

**Auto columns**

Defines track sizes for implicit columns created beyond the explicit template.

| Parameter | Type | Description |
| --- | --- | --- |
| `tracks` | `List` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridAutoRows(List)` {: #gridautorowslist data-toc-label="gridAutoRows(List)" }

**Auto rows**

Defines track sizes for implicit rows created beyond the explicit template.

| Parameter | Type | Description |
| --- | --- | --- |
| `tracks` | `List` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridAutoFlow(GridAutoFlow)` {: #gridautoflowgridautoflow data-toc-label="gridAutoFlow(GridAutoFlow)" }

**Grid auto flow**

Controls placement direction during auto-placement (ROW, COLUMN, ROW_DENSE, COLUMN_DENSE).

| Parameter | Type | Description |
| --- | --- | --- |
| `flow` | `GridAutoFlow` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridColumn(int, int)` {: #gridcolumnint-int data-toc-label="gridColumn(int, int)" }

**Grid column placement**

Pins child to 1-based column start index and sets column span (<= 0 requests auto-placement).

| Parameter | Type | Description |
| --- | --- | --- |
| `start` | `int` | — |
| `span` | `int` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`gridRow(int, int)` {: #gridrowint-int data-toc-label="gridRow(int, int)" }

**Grid row placement**

Pins child to 1-based row start index and sets row span (<= 0 requests auto-placement).

| Parameter | Type | Description |
| --- | --- | --- |
| `start` | `int` | — |
| `span` | `int` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`aspectRatio(float)` {: #aspectratiofloat data-toc-label="aspectRatio(float)" }

**Aspect ratio**

Sets desired width-to-height ratio when one dimension remains auto.

| Parameter | Type | Description |
| --- | --- | --- |
| `ratio` | `float` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`zIndex(int)` {: #zindexint data-toc-label="zIndex(int)" }

**Z-Index**

Sets drawing layer order among siblings; higher integers render on top.

| Parameter | Type | Description |
| --- | --- | --- |
| `zIndex` | `int` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`center()` {: #center data-toc-label="center()" }

**Center children**

Centers the container's children on both the main and cross axes.

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`centerSelf()` {: #centerself data-toc-label="centerSelf()" }

**Center self**

Centers this flex item on its parent's cross axis (alignSelf(CENTER)).

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

### Batching & Lifecycle

| Method | Description |
| --- | --- |
| [`update(Consumer)`](#updateconsumer) | Applies several layout changes and notifies the owner once after the outer block finishes. |
| [`copy()`](#copy) | Creates an independent detached copy of all layout properties with no widget callback attached. |
| [`copyFrom(LayoutStyle)`](#copyfromlayoutstyle) | Copies all layout values from another style, retaining this object's change callback. |
| [`onChanged(Runnable)`](#onchangedrunnable) | Registers a callback notified whenever any layout property mutates. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`update(Consumer)` {: #updateconsumer data-toc-label="update(Consumer)" }

**Batch updates**

Applies several layout changes and notifies the owner once after the outer block finishes.

Nested `update` calls are safe. The change callback runs only after the outermost block, and only if a property actually changed. A null consumer is ignored.

| Parameter | Type | Description |
| --- | --- | --- |
| `update` | `Consumer` | Callback that mutates this style; null is a no-op. |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`copy()` {: #copy data-toc-label="copy()" }

**Copy snapshot**

Creates an independent detached copy of all layout properties with no widget callback attached.

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`copyFrom(LayoutStyle)` {: #copyfromlayoutstyle data-toc-label="copyFrom(LayoutStyle)" }

**Copy from another style**

Copies all layout values from another style, retaining this object's change callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `other` | `LayoutStyle` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">LayoutStyle::</span>`onChanged(Runnable)` {: #onchangedrunnable data-toc-label="onChanged(Runnable)" }

**Change callback**

Registers a callback notified whenever any layout property mutates.

| Parameter | Type | Description |
| --- | --- | --- |
| `onChanged` | `Runnable` | — |

Returns `LayoutStyle`: this LayoutStyle for fluent configuration.

</div>

