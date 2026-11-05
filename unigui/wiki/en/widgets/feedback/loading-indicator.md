---
title: "LoadingIndicator"
slug: "loading-indicator"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# LoadingIndicator

Highly configurable loading animation widget supporting ring, bar, pulse, and dots indicator modes.

## Overview

`LoadingIndicator` provides dynamic visual feedback for ongoing background operations and asynchronous tasks.

Supported styles and modes:
- Multiple animation modes: `RING`, `BAR`, `PULSE`, `DOTS`.
- Fine-grained control over arc count, speed, phase, thickness, and size.
- Automatic animation tick integration when active.

## Configuring and Displaying Loading Indicator

```java
LoadingIndicator indicator = new LoadingIndicator();
indicator.mode(LoadingIndicator.Mode.SPINNER).start();
```

## Related links

- [ProgressBar](<progress-bar.md>): Linear progress bar widget displaying determinate or indeterminate completion state.
- [Spinner](<spinner.md>): Rotating progress spinner animation widget for indicating ongoing background operations.

## API

<div class="api-member-card" markdown="1">

### `LoadingIndicator()` {: #loadingindicator data-toc-label="LoadingIndicator()" }

Constructs a LoadingIndicator initialized with SPINNER mode and default styling.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`accentColor()` {: #accentcolor data-toc-label="accentColor()" }

Returns the primary accent color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`activeDots()` {: #activedots data-toc-label="activeDots()" }

Returns the active highlighted dot count.

Returns `int`: Count of dots..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`activeDots(int)` {: #activedotsint data-toc-label="activeDots(int)" }

Sets the active highlighted dot count.

| Parameter | Type | Description |
| --- | --- | --- |
| `activeDots` | `int` | Number of highlighted active dots. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`angle()` {: #angle data-toc-label="angle()" }

Returns the arc sweep angle in radians.

Returns `float`: Rotation phase angle in radians..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`angle(float)` {: #anglefloat data-toc-label="angle(float)" }

Sets the arc sweep angle in radians.

| Parameter | Type | Description |
| --- | --- | --- |
| `radians` | `float` | Rotation phase angle in radians. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`arcs()` {: #arcs data-toc-label="arcs()" }

Returns the arc count for multi-arc styles.

Returns `int`: Count of segments or arcs..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`arcs(int)` {: #arcsint data-toc-label="arcs(int)" }

Sets the arc count for multi-arc styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `arcs` | `int` | Number of segments or arcs. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`dots()` {: #dots data-toc-label="dots()" }

Returns the total dot count for dotted styles.

Returns `int`: Count of dots..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`dots(int)` {: #dotsint data-toc-label="dots(int)" }

Sets the total dot count for dotted styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `dots` | `int` | Total number of dots. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`indicatorSize(float)` {: #indicatorsizefloat data-toc-label="indicatorSize(float)" }

Sets both preferred width and height simultaneously in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `size` | `float` | Square dimension or indicator size. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`mode()` {: #mode data-toc-label="mode()" }

Returns the active display mode (SPINNER, BAR, PULSE, DOTS, etc.).

Returns `Mode`: The loading indicator display mode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`mode(Mode)` {: #modemode data-toc-label="mode(Mode)" }

Sets the loading indicator display mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `mode` | `Mode` | Loading indicator display mode. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`phase()` {: #phase data-toc-label="phase()" }

Returns the current animation phase normalized between 0.0 and 1.0.

Returns `float`: Rotation phase angle in radians..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`phase(float)` {: #phasefloat data-toc-label="phase(float)" }

Manually sets the animation phase value.

| Parameter | Type | Description |
| --- | --- | --- |
| `phase` | `float` | Rotation phase angle in radians. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Returns the preferred indicator height.

Returns `float`: The floating-point value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Sets the preferred indicator height in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredHeight` | `float` | Preferred height dimension. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Sets independent preferred width and height.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension in pixels. |
| `height` | `float` | Height dimension in pixels. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Returns the preferred indicator width.

Returns `float`: The floating-point value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Sets the preferred indicator width in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredWidth` | `float` | Preferred width dimension. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`radius()` {: #radius data-toc-label="radius()" }

Returns the explicit indicator radius in UI pixels, or 0 if auto-computed.

Returns `float`: The floating-point value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

Sets an explicit indicator radius in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `radius` | `float` | Circle or arc radius in pixels. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`running()` {: #running data-toc-label="running()" }

Returns true if the animation is currently running.

Returns `boolean`: True if the animation is currently running..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`running(boolean)` {: #runningboolean data-toc-label="running(boolean)" }

Starts or stops the animation.

| Parameter | Type | Description |
| --- | --- | --- |
| `running` | `boolean` | Whether the animation is active and running. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`secondaryColor()` {: #secondarycolor data-toc-label="secondaryColor()" }

Returns the secondary highlight color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`segments()` {: #segments data-toc-label="segments()" }

Returns the segment count for segmented styles.

Returns `int`: Count of segments or arcs..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`segments(int)` {: #segmentsint data-toc-label="segments(int)" }

Sets the segment count for segmented styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `segments` | `int` | Number of segments or arcs. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`speed()` {: #speed data-toc-label="speed()" }

Returns the animation speed multiplier.

Returns `float`: Animation speed multiplier..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`speed(float)` {: #speedfloat data-toc-label="speed(float)" }

Sets the animation speed multiplier.

| Parameter | Type | Description |
| --- | --- | --- |
| `speed` | `float` | Animation speed multiplier. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`spinnerStyle()` {: #spinnerstyle data-toc-label="spinnerStyle()" }

Returns the active spinner style preset.

Returns `Style`: The [Spinner](<spinner.md>).[Style](<../../core/styling/style.md>) value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`spinnerStyle(Style)` {: #spinnerstylestyle data-toc-label="spinnerStyle(Style)" }

Sets the spinner style preset.

| Parameter | Type | Description |
| --- | --- | --- |
| `spinnerStyle` | `Style` | Visual animation style. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`start()` {: #start data-toc-label="start()" }

Starts the indicator animation.

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`stop()` {: #stop data-toc-label="stop()" }

Stops the indicator animation.

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`thickness()` {: #thickness data-toc-label="thickness()" }

Returns the stroke thickness in UI pixels.

Returns `float`: The floating-point value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

Sets the stroke thickness in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `thickness` | `float` | Stroke thickness in pixels. |

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`trackColor()` {: #trackcolor data-toc-label="trackColor()" }

Returns the background track color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LoadingIndicator::</span>`useDefaultPreferredSize()` {: #usedefaultpreferredsize data-toc-label="useDefaultPreferredSize()" }

Resets dimensions to the default preferred size for the current mode.

Returns `LoadingIndicator`: This widget instance for chaining..

</div>

