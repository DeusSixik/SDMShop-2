---
title: "Spinner"
slug: "spinner"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# Spinner

Rotating progress spinner animation widget for indicating ongoing background operations.

## Overview

`Spinner` is a circular loading animation widget that visualizes continuous background processing.

Key features:
- Multiple presentation styles: segmented circular bars, dots, rings.
- Dynamic rotation speed, radius, arc count, and stroke thickness.
- Color customization for active and inactive segments.

## Displaying Animated Spinner

```java
Spinner spinner = new Spinner(Spinner.Style.ARC_SWEEP);
spinner.speed(1.5f);
```

## Related links

- [LoadingIndicator](<loading-indicator.md>): Highly configurable loading animation widget supporting ring, bar, pulse, and dots indicator modes.

## API

<div class="api-member-card" markdown="1">

### `Spinner()` {: #spinner data-toc-label="Spinner()" }

Constructs a default Spinner initialized to Mode.SPINNER.

</div>

<div class="api-member-card" markdown="1">

### `Spinner(Style)` {: #spinnerstyle data-toc-label="Spinner(Style)" }

Constructs a Spinner with the specified visual style preset.

| Parameter | Type | Description |
| --- | --- | --- |
| `style` | `Style` | Visual animation style. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`activeDots(int)` {: #activedotsint data-toc-label="activeDots(int)" }

Sets the number of highlighted active dots in dotted styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `activeDots` | `int` | Number of highlighted active dots. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`angle(float)` {: #anglefloat data-toc-label="angle(float)" }

Sets the arc sweep angle in radians for arc-based spinner styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `radians` | `float` | Rotation phase angle in radians. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`arcs(int)` {: #arcsint data-toc-label="arcs(int)" }

Sets the arc count for multi-arc spinner styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `arcs` | `int` | Number of segments or arcs. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`dots(int)` {: #dotsint data-toc-label="dots(int)" }

Sets the dot count for dotted spinner styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `dots` | `int` | Total number of dots. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`indicatorSize(float)` {: #indicatorsizefloat data-toc-label="indicatorSize(float)" }

Sets both preferred width and preferred height simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `size` | `float` | Square dimension or indicator size. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

Sets an explicit indicator radius in UI pixels, or 0 to derive from bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `radius` | `float` | Circle or arc radius in pixels. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`segments(int)` {: #segmentsint data-toc-label="segments(int)" }

Sets the segment count for segmented spinner styles.

| Parameter | Type | Description |
| --- | --- | --- |
| `segments` | `int` | Number of segments or arcs. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`speed(float)` {: #speedfloat data-toc-label="speed(float)" }

Sets the animation speed multiplier.

| Parameter | Type | Description |
| --- | --- | --- |
| `speed` | `float` | Animation speed multiplier. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`style()` {: #style data-toc-label="style()" }

Returns the active spinner visual style preset.

Returns `Style`: The spinner visual animation style..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`style(Style)` {: #stylestyle data-toc-label="style(Style)" }

Sets the spinner visual style preset.

| Parameter | Type | Description |
| --- | --- | --- |
| `style` | `Style` | Visual animation style. |

Returns `Spinner`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Spinner::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

Sets the stroke thickness in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `thickness` | `float` | Stroke thickness in pixels. |

Returns `Spinner`: This widget instance for chaining..

</div>

