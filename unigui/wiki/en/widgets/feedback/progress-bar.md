---
title: "ProgressBar"
slug: "progress-bar"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# ProgressBar

Linear progress bar widget displaying determinate or indeterminate completion state.

## Overview

`ProgressBar` provides visual feedback on task completion through determinate percentage fills or indeterminate animated activity indicators.

Key features:
- Configurable value ranges with `min`, `max`, and current `value`.
- Smooth progress computation (`progress()`).
- Indeterminate mode for tasks with unknown completion time.
- Customizable track and indicator styling.

## Setting Up Determinate and Indeterminate Progress Bar

```java
ProgressBar bar = new ProgressBar();
bar.range(0.0f, 100.0f).value(45.0f);
```

## Related links

- [LoadingIndicator](<loading-indicator.md>): Highly configurable loading animation widget supporting ring, bar, pulse, and dots indicator modes.

## API

<div class="api-member-card" markdown="1">

### `ProgressBar()` {: #progressbar data-toc-label="ProgressBar()" }

Constructs a ProgressBar with default [0, 100] range and styling.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`indeterminate()` {: #indeterminate data-toc-label="indeterminate()" }

Returns true if indeterminate animation mode is enabled.

Returns `boolean`: True if operating in indeterminate mode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`indeterminate(boolean)` {: #indeterminateboolean data-toc-label="indeterminate(boolean)" }

Enables or disables indeterminate continuous activity animation.

| Parameter | Type | Description |
| --- | --- | --- |
| `indeterminate` | `boolean` | Whether the progress bar operates in indeterminate mode. |

Returns `ProgressBar`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`max()` {: #max data-toc-label="max()" }

Returns the maximum progress value.

Returns `float`: Current normalized progress between 0.0 and 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`max(float)` {: #maxfloat data-toc-label="max(float)" }

Sets the maximum progress value.

| Parameter | Type | Description |
| --- | --- | --- |
| `max` | `float` | Maximum range bound. |

Returns `ProgressBar`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`min()` {: #min data-toc-label="min()" }

Returns the minimum progress value.

Returns `float`: Current normalized progress between 0.0 and 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`min(float)` {: #minfloat data-toc-label="min(float)" }

Sets the minimum progress value.

| Parameter | Type | Description |
| --- | --- | --- |
| `min` | `float` | Minimum range bound. |

Returns `ProgressBar`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`progress()` {: #progress data-toc-label="progress()" }

Returns the normalized progress ratio between 0.0 and 1.0.

Returns `float`: Current normalized progress between 0.0 and 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`range(float, float)` {: #rangefloat-float data-toc-label="range(float, float)" }

Sets both minimum and maximum bounds simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `min` | `float` | Minimum range bound. |
| `max` | `float` | Maximum range bound. |

Returns `ProgressBar`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`value()` {: #value data-toc-label="value()" }

Returns the current numeric progress value.

Returns `float`: Current normalized progress between 0.0 and 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ProgressBar::</span>`value(float)` {: #valuefloat data-toc-label="value(float)" }

Sets the current numeric progress value, clamped to [min, max].

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `float` | Progress value. |

Returns `ProgressBar`: This widget instance for chaining..

</div>

